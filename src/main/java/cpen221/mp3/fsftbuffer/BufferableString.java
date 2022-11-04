package cpen221.mp3.fsftbuffer;


import java.io.Serializable;

/**
 * A wrapper which associates an id string with a content string and allows only reading of the content and id
 */
public class BufferableString implements Serializable, Bufferable {

    //AF:
    //This class represents a string which has an additional id field of type String associated with it

    //RI:
    //neither content or id is null

    private static final boolean DEBUG = false;

    private final String contentString;
    private final String idString;

    /**
     * Checks the rep invariant of the current state of the object
     */
    private void checkrep() {
        assert !DEBUG || (contentString != null && idString != null);
    }

    /**
     * Creates a new BufferableString with both the content and id the same.
     *
     * @param content the string to set as content and id
     */
    public BufferableString(String content) {
        contentString = content;
        idString = content;
        checkrep();
    }

    /**
     * Creates a new BufferableString with different content and id information
     *
     * @param content the value to set the content to
     * @param id      the value to set the id to
     */
    public BufferableString(String content, String id) {
        contentString = content;
        idString = id;
        checkrep();
    }

    /**
     * returns the id of the object
     *
     * @return id field
     */
    public String id() {
        return idString;
    }
}
