package cpen221.mp3.fsftbuffer;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A finite-space finite-time buffer which stores objects of type T. Type T must implement Bufferable. The capacity
 * and timeout are set on creation and final for the life of the buffer. They can be specified in the constructor or
 * left to the defaults of size = 32 and timeout = 3600 seconds.
 *
 * @param <T> The type of object which is being stored in the buffer. Must implement Bufferable.
 */
public class FSFTBuffer<T extends Bufferable> implements Serializable {

    //AF:
    //This class represents a finite-time, finite-space buffer. The capacity and timout restrictions cannot
    //be modified after instantiating the buffer. The storage map stores an expiration time and
    // object under the object's id.

    //RI:
    //object is not null
    //capacity and timeout are non-negative
    //The current number of objects in the buffer must not exceed the capacity

    private static final boolean DEBUG = false;
    public static final int DSIZE = 32;
    public static final int DTIMEOUT = 3600;

    private final Map<String, TimeoutObject<T>> storage;
    private final int capacity;
    private final int timeout;

    /**
     * Verifies that the current state of the object obeys the representation invariant.
     */
    private synchronized void checkrep() {
        if (DEBUG) {
            assert (capacity > 0 && timeout > 0 && storage != null);
            int sum = 0;
            for (TimeoutObject<T> temp : storage.values()) {
                if (!objectTimedOut(temp)) {
                    sum++;
                }
            }
            assert (sum <= capacity);
        }
    }

    /**
     * Create a buffer with a fixed capacity and a timeout value.
     * Objects in the buffer that have not been refreshed within the
     * timeout period are removed from the cache.
     *
     * @param capacity the number of objects the buffer can hold
     * @param timeout  the duration, in seconds, an object should
     *                 be in the buffer before it times out
     */
    public FSFTBuffer(int capacity, int timeout) {
        storage = new ConcurrentHashMap<>();
        this.capacity = capacity;
        this.timeout = timeout;
    }

    /**
     * Create a buffer with default capacity and timeout values.
     */
    public FSFTBuffer() {
        this(DSIZE, DTIMEOUT);
    }

    /**
     * Add a value to the buffer.
     * If the buffer is full then remove the least recently accessed
     * object to make room for the new object.
     */
    public synchronized boolean put(T t) {
        checkrep();
        if (storage.size() >= capacity) {
            int removed = 0;
            TimeoutObject<T> oldestEntry = TimeoutObject.createMaxInstant();

            for (TimeoutObject<T> obj : storage.values()) {
                if (objectTimedOut(obj)) {
                    storage.remove(obj.getObject().id());
                    removed++;
                } else if (removed == 0) {
                    if (obj.getTime().compareTo(oldestEntry.getTime()) < 0) {
                        oldestEntry = obj;
                    }
                }
            }
            if (storage.containsKey(t.id())) {
                checkrep();
                return false;
            }
            if (removed == 0) {
                storage.remove(oldestEntry.getObject().id());
            }
        }
        storage.put(t.id(), new TimeoutObject<>(t, timeout));
        checkrep();
        return true;
    }

    /**
     * @param id the identifier of the object to be retrieved
     * @return the object that matches the identifier from the
     * buffer
     * @throws Exception if no object corresponds to id
     */
    public synchronized T get(String id) throws IdNotFoundException {
        checkrep();
        if (storage.containsKey(id)) {
            TimeoutObject<T> entry = storage.get(id);
            if (!objectTimedOut(entry)) {
                checkrep();
                return entry.getObject();
            } else {
                storage.remove(id);
                throw new IdNotFoundException("No object with that ID");
            }
        } else {
            throw new IdNotFoundException("No object with that ID");
        }
    }

    /**
     * Update the last refresh time for the object with the provided id.
     * This method is used to mark an object as "not stale" so that its
     * timeout is delayed.
     *
     * @param id the identifier of the object to "touch"
     * @return true if successful and false otherwise
     */
    public synchronized boolean touch(String id) {
        checkrep();
        boolean containsKey = storage.containsKey(id);
        if (containsKey && !objectTimedOut(storage.get(id))) {
            storage.get(id).updateTime(timeout);
            checkrep();
            return true;
        }
        if (containsKey) {
            storage.remove(id);
        }
        checkrep();
        return false;
    }

    /**
     * Update an object in the buffer.
     * This method updates an object and acts like a "touch" to
     * renew the object in the cache.
     *
     * @param t the object to update
     * @return true if successful and false otherwise
     */
    public synchronized boolean update(T t) {
        checkrep();
        boolean containsKey = storage.containsKey(t.id());
        if (containsKey && !objectTimedOut(storage.get(t.id()))) {
            storage.put(t.id(), new TimeoutObject<T>(t, timeout));
            checkrep();
            return true;
        }
        if (containsKey) {
            storage.remove(t.id());
        }
        checkrep();
        return false;
    }

    /**
     * Checks if the specified object in the buffer has expired
     *
     * @param t the object to check
     * @return true if the object has expired, false otherwise.
     */
    private synchronized boolean objectTimedOut(TimeoutObject<T> t) {
        return t.getTime().compareTo(Instant.now()) < 0;
    }

    /**
     * @return the current number of elements in the buffer whether expired or not.
     */
    public synchronized int getNumItems() {
        checkrep();
        return storage.size();
    }
}
