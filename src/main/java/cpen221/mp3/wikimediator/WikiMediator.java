package cpen221.mp3.wikimediator;

import cpen221.mp3.fsftbuffer.BufferableString;
import cpen221.mp3.fsftbuffer.FSFTBuffer;
import cpen221.mp3.fsftbuffer.IdNotFoundException;
import cpen221.mp3.fsftbuffer.TimeoutObject;
import cpen221.mp3.wikimediator.queries.InvalidQueryException;
import cpen221.mp3.wikimediator.queries.QueryEvaluator;
import cpen221.mp3.wikimediator.queries.queryGrammarLexer;
import cpen221.mp3.wikimediator.queries.queryGrammarParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.fastily.jwiki.core.Wiki;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WikiMediator {

    private static final int CACHE_CAPACITY = 30;
    private static final int CACHE_TIME = 300;
    private static final int STATISTIC_TIMEOUT = 30;

    FSFTBuffer<BufferableWikiPage> cache;
    Wiki wiki;
    List<ZeitgeistObject> zeitgeistList;
    List<TimeoutObject<BufferableString>> trendingList;
    List<TimeoutObject<BufferableString>> peakLoadList;
    int peakLoad;

    //AF:
    // An object that acts as a mediator to access Wikipedia
    // wiki: represents the access to Wikipedia through jwiki
    // cache: represents a temporary storage location for previously searched pages in order to reduce network accesses
    // zeitgeistList: represents all of the searched terms from search and getPage
    // and the number of times they have been searched
    // trendingList: represents all of the searched terms from search and getPage in the past 30 seconds
    // and the number of times they have been searched
    // peakLoadList: represents all of the usages of any public method in this WikiMediator within the past 30 seconds
    // peakLoad: represents the maximum number of usages of any public method in this WikiMediator in a 30 second period

    //RI:
    // all fields are not null
    // cache: all objects within cache have been previously searched using getPage
    // zeitgeistList: every search term from search and getPage is represented by a ZeitgeistObject with uses greater than 0
    // trendingList: holds a representation of every search term from search and getPage used in the past 30s
    // peakLoadList: holds a representation of every method usage used in the past 30s
    // peakLoad: is 0 or greater


    /**
     * Creates a new wikiMediator object with no statistics and cache
     */
    public WikiMediator() {
        wiki = new Wiki.Builder().build();
        cache = new FSFTBuffer<>(CACHE_CAPACITY, CACHE_TIME);
        zeitgeistList = new ArrayList<>();
        trendingList = new ArrayList<>();
        peakLoadList = new ArrayList<>();
        peakLoad = 0;
    }

    public WikiMediator(WikiMediatorSave saveObj) {
        wiki = new Wiki.Builder().build();
        cache = new FSFTBuffer<>(CACHE_CAPACITY, CACHE_TIME);
        this.zeitgeistList = saveObj.getZeitgeistList();
        this.trendingList = saveObj.getTrendingList();
        this.peakLoadList = saveObj.getPeakLoadList();
        this.peakLoad = saveObj.getPeakLoad();
    }

    /**
     * Searches Wikipedia for the query, returning up to limit page titles that match the
     * query string
     *
     * @param query the string to search wikipedia for
     *              is not null
     * @param limit the number of page titles to return
     * @return the list of page titles that match the query,
     * ordered by non-descending lexicographical order
     */
    public synchronized List<String> search(String query, int limit) {
        updateStatistics(query);
        checkPeakLoad();

        List<String> searchList = wiki.search(query, limit);
        searchList.sort(Comparator.naturalOrder());
        return searchList;
    }

    /**
     * Given a page title, return the text associated with the Wikipedia page that matches that page title
     *
     * @param pageTitle the pageTitle to return the text of
     *                  is not null
     * @return a String matching the text of the Wikipedia page associated with pageTitle
     * if pageTitle is not a valid Wikipedia page, returns an empty string
     */
    public synchronized String getPage(String pageTitle) {
        updateStatistics(pageTitle);
        checkPeakLoad();

        try {
            return cache.get(pageTitle).getContent();

        } catch (IdNotFoundException e) {

        }
        if (wiki.exists(pageTitle)) {
            String content = wiki.getPageText(pageTitle);
            cache.put(new BufferableWikiPage(pageTitle, content));
            return content;
        }
        return "";
    }

    /**
     * Returns a list of the most used search terms from search() and getPage() for this
     * WikiMediator object
     *
     * @param limit the number of search terms to return
     *              must be greater than 0
     * @return a list of the most used search terms, ordered by non-increasing number of uses
     * if two terms have the same number of uses,
     * they are ordered by non-descending lexicographical order of searchTerms
     */
    public synchronized List<String> zeitgeist(int limit) {
        zeitgeistList.sort(new ZeitgeistComparator());
        checkPeakLoad();

        if (zeitgeistList.size() < limit) {
            return zeitgeistList.stream().map(z -> z.getSearchTerm()).collect(Collectors.toList());
        }
        List<String> returnList = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            returnList.add(zeitgeistList.get(i).getSearchTerm());
        }

        return returnList;
    }

    /**
     * Returns a list of the most used search terms from search() and getPage() for this
     * WikiMediator object in the last 30 seconds
     *
     * @param limit the number of search terms to return
     *              must be greater than 0
     * @return a list of the most used search terms in the past 30 seconds,
     * ordered by non-increasing number of uses if two terms have the same number of uses,
     * they are ordered by non-descending lexicographical order
     */
    public synchronized List<String> trending(int limit) {

        checkPeakLoad();
        trendingList =
            trendingList.stream().filter(t1 -> t1.getTime().compareTo(Instant.now()) > 0).collect(
                Collectors.toList());

        HashMap<String, Integer> searchMap = new HashMap<>();

        for (TimeoutObject<BufferableString> t : trendingList) {
            String searchTerm = t.getObject().id();
            if (searchMap.containsKey(searchTerm)) {
                searchMap.put(searchTerm, searchMap.get(searchTerm) + 1);
            } else {
                searchMap.put(searchTerm, 1);
            }
        }

        List<ZeitgeistObject> returnList = new ArrayList<>();
        for (String s : searchMap.keySet()) {
            returnList.add(new ZeitgeistObject(s, searchMap.get(s)));
        }
        returnList.sort(new ZeitgeistComparator());

        if (returnList.size() < limit) {
            return returnList.stream().map(z -> z.getSearchTerm()).collect(Collectors.toList());
        }

        return returnList.subList(0, limit).stream().map(z -> z.getSearchTerm())
            .collect(Collectors.toList());

    }

    /**
     * Tracks the maximum number of requests made to this WikiMediator object in any 30s period
     * All 5 public methods, including this one, are considered requests. Calling this method
     * counts as a use in the returned value
     * Ex. calling peakLoad30s() before any other methods on a new WikiMediator object will return 1
     *
     * @return the maximum number of requests in any 30s period
     */
    public synchronized int peakLoad30s() {
        checkPeakLoad();

        return peakLoad;
    }

    /**
     * Method that allows for the evaluation structured queries. Evaluates the given query and
     * returns a list of Strings that satisfy the query conditions
     *
     * @param query the String representation of the query
     *              must be well-formed query as defined in ReadMe/queryGrammar.g4
     *              is not null
     * @return a list of Strings that satisfy the conditions of the query
     * If query includes a SORTED token the returned list is sorted as follows:
     * SORTED = "asc": Strings are sorted in lexicographically ascending order
     * SORTED = "desc": Strings are sorted in lexicographically descending order
     * If query satisfies grammar but does not produce meaningful results, an empty list is returned
     * @throws InvalidQueryException when query does not satisfy grammar i.e. cannot be parsed
     */
    public synchronized List<String> executeQuery(String query) throws InvalidQueryException {
        checkPeakLoad();
        CharStream stream = new ANTLRInputStream(query);

        queryGrammarLexer lexer;
        TokenStream tokens;
        queryGrammarParser parser;
        queryGrammarParser.QueryContext context;
        try {
            lexer = new queryGrammarLexer(stream);
            lexer.reportErrorsAsExceptions();
            tokens = new CommonTokenStream(lexer);
        } catch (RuntimeException e) {
            throw new InvalidQueryException("Query could not be lexed");
        }

        try {
            parser = new queryGrammarParser(tokens);
            parser.reportErrorsAsExceptions();
            context = parser.query();
        } catch (RuntimeException e) {
            throw new InvalidQueryException("Query could not be parsed");
        }

        QueryEvaluator evaluator = new QueryEvaluator();

        return evaluator.visitQuery(context, wiki);


    }

    public synchronized WikiMediatorSave getSave() {
        return new WikiMediatorSave(new ArrayList<>(zeitgeistList), new ArrayList<>(trendingList),
            new ArrayList<>(peakLoadList), peakLoad);
    }


    /**
     * Helper method to update the internal handling of statistics for this WikiMediator
     * Updates the number of uses of a searchTerm for the zeitgeist method and adds a timestamped
     * search for use in the trending method
     *
     * @param searchTerm the search term to update statistics for
     *                   is not null
     */
    private void updateStatistics(String searchTerm) {
        ZeitgeistObject tempObj = new ZeitgeistObject(searchTerm);
        if (zeitgeistList.contains(tempObj)) {
            zeitgeistList.get(zeitgeistList.indexOf(tempObj)).addUse();
        } else {
            zeitgeistList.add(tempObj);
        }
        trendingList.add(new TimeoutObject<>(new BufferableString(searchTerm), STATISTIC_TIMEOUT));
    }

    /**
     * Helper method to check what the peakLoad for this WikiMediator object should be
     * Should be called in every public method.
     * Adds a timestamped use and checks if the uses in the last 30s
     * are greater than the previous peakLoad. If so replaces the value of peakLoad
     */
    private void checkPeakLoad() {
        peakLoadList.add(new TimeoutObject<>(new BufferableString("use"), STATISTIC_TIMEOUT));
        peakLoadList =
            peakLoadList.stream().filter(t -> t.getTime().compareTo(Instant.now()) > 0).collect(
                Collectors.toList());
        peakLoad = Math.max(peakLoadList.size(), peakLoad);
    }

}
