package cpen221.mp3.wikimediator;

import java.util.Comparator;

//A comparator for zeitgeist objects. Order is by non-increasing number of uses
//If uses are the same for two objects, order is non-descending lexicographical order of searchTerm
public class ZeitgeistComparator implements Comparator<ZeitgeistObject> {

    @Override
    public int compare(ZeitgeistObject o1, ZeitgeistObject o2) {
        if (o2.getNumUses() - o1.getNumUses() != 0) {
            return o2.getNumUses() - o1.getNumUses();
        }
        return o1.getSearchTerm().compareTo(o2.getSearchTerm());
    }
}
