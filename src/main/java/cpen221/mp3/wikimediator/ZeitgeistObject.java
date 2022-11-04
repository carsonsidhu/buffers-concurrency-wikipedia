package cpen221.mp3.wikimediator;

//

import cpen221.mp3.fsftbuffer.Bufferable;

import java.io.Serializable;

public class ZeitgeistObject implements Serializable, Bufferable {
    private final String searchTerm;
    private int numUses;

    //AF:
    // An object that represents a search term and the number of times that is has been used
    // searchTerm: the search term that the number of uses will be recorded for
    // numUses: the number of times searchTerm has been used

    //RI:
    // searchTerm is not null
    // numUses is greater than zero


    /**
     * Creates a new Zeitgeist object with a given search term, and default number of uses as 1
     *
     * @param searchTerm the search term
     *                   is not null
     */
    public ZeitgeistObject(String searchTerm) {
        this.searchTerm = searchTerm;
        numUses = 1;
    }

    /**
     * Creates a new Zeitgeist object with a given search term,
     * and specified number of uses
     *
     * @param searchTerm the search term
     *                   is not null
     * @param numUses    the number of times this searchTerm has been used
     *                   is greater than 0
     */
    public ZeitgeistObject(String searchTerm, int numUses) {
        this.searchTerm = searchTerm;
        this.numUses = numUses;
    }

    /**
     * Increments the number of uses of this Zeitgeist Object by 1
     */
    public void addUse() {
        numUses++;
    }

    /**
     * @return the search term held by this Zeitgeist Object
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * @return the number of times this Zeitgeist Object has been used
     */
    public int getNumUses() {
        return numUses;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ZeitgeistObject)) {
            return false;
        }
        ZeitgeistObject other = (ZeitgeistObject) obj;
        return this.searchTerm.equals(other.searchTerm);
    }

    @Override
    public String id() {
        return searchTerm;
    }
}

