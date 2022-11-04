package cpen221.mp3.wikimediator;

import cpen221.mp3.fsftbuffer.BufferableString;
import cpen221.mp3.fsftbuffer.TimeoutObject;

import java.io.Serializable;
import java.util.List;

public class WikiMediatorSave implements Serializable {

    private final List<ZeitgeistObject> zeitgeistList;
    private final List<TimeoutObject<BufferableString>> trendingList;
    private final List<TimeoutObject<BufferableString>> peakLoadList;
    private final int peakLoad;

    public WikiMediatorSave(List<ZeitgeistObject> zeitgeistList,
                            List<TimeoutObject<BufferableString>> trendingList,
                            List<TimeoutObject<BufferableString>> peakLoadList, int peakLoad) {
        this.zeitgeistList = zeitgeistList;
        this.trendingList = trendingList;
        this.peakLoadList = peakLoadList;
        this.peakLoad = peakLoad;
    }

    public int getPeakLoad() {
        return peakLoad;
    }

    public List<TimeoutObject<BufferableString>> getPeakLoadList() {
        return peakLoadList;
    }

    public List<TimeoutObject<BufferableString>> getTrendingList() {
        return trendingList;
    }

    public List<ZeitgeistObject> getZeitgeistList() {
        return zeitgeistList;
    }
}
