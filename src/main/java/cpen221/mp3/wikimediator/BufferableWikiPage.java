package cpen221.mp3.wikimediator;

import cpen221.mp3.fsftbuffer.Bufferable;

public class BufferableWikiPage implements Bufferable {

    //AF:
    // Represents an Wiki Page that implements Bufferable, for use in a FSFT Buffer or other datatypes
    // title represents the title of the wiki page
    // content represents the content of the wiki page with page title title

    //RI:
    // title and content are not null
    // content matches a String representation of the wiki page associated with title

    private final String title;
    private final String content;

    public BufferableWikiPage(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String id() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
