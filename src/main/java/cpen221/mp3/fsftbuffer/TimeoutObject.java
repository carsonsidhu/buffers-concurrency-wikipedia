package cpen221.mp3.fsftbuffer;

import cpen221.mp3.wikimediator.ZeitgeistObject;

import java.io.Serializable;
import java.time.Instant;

/**
 * A wrapper which associates and instant with an object specified type T. This instant represents the expiration
 * time for the object within and thus the instant must be in the future at creation of the object.
 *
 * @param <T> the type of object being stored within this wrapper.
 */
public class TimeoutObject<T extends Bufferable> implements Serializable {

    //AF:
    //This class represents an object that has a fixed timeout time
    //object is the generic object that you want to record the timeout time of
    //timeoutTime is the Instant at which this object will timeout

    //RI:
    //object is not null
    //When first created or updated, timeoutTime is greater than the current time

    private final T object;
    private Instant timeoutTime;

    /**
     * Creates a new TimeoutObject instance where the timeoutTime is the current time
     * plus the specified timeout
     *
     * @param object  the object to hold in the TimeoutObject
     *                is not null
     * @param timeout the timeout of the FSFT Buffer that this TimeoutObject will be placed in
     *                is greater than 0 and in seconds
     */
    public TimeoutObject(T object, int timeout) {
        this.object = object;
        this.timeoutTime = Instant.now().plusSeconds(timeout);
    }

    /**
     * Creates a new TimeoutObject instance where the expiration instant is set directly.
     * <p>
     * Precondition: expiration Instant must be in the future
     *
     * @param object            the object to hold in the TimeoutObject
     *                          is not null
     * @param expirationInstant the expiration instant for this object
     */
    private TimeoutObject(T object, Instant expirationInstant) {
        this.object = object;
        this.timeoutTime = expirationInstant;
    }

    /**
     * Factory method creating a TimeoutObject with null object, and instant Instant.MAX
     *
     * @return a new TimeoutObject with null contents and instant Instant.MAX
     */
    public static <T extends Bufferable> TimeoutObject<T> createMaxInstant() {
        return new TimeoutObject<T>(null, Instant.MAX);
    }

    /**
     * Returns the Instant when this TimeoutObject times out
     *
     * @return the timeoutTime, the Instant when the TimeoutObject times out
     */
    public Instant getTime() {
        return timeoutTime;
    }


    /**
     * @return the object held by this TimeoutObject
     */
    public T getObject() {
        return object;
    }

    /**
     * Updates the timeoutTime of this TimeoutObject such that the new timeoutTime
     * is the current time plus the specified timeout
     *
     * @param timeout the timeout of the FSFT Buffer that this TimeoutObject will be placed in
     *                is greater than 0 and in seconds
     */
    public void updateTime(int timeout) {
        timeoutTime = Instant.now().plusSeconds(timeout);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TimeoutObject)){
            return false;
        }
        TimeoutObject<T> other = (TimeoutObject<T>) obj;
        return this.getObject().equals(other.getObject());
    }
}
