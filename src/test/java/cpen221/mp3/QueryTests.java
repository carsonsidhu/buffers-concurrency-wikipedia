package cpen221.mp3;
import cpen221.mp3.wikimediator.WikiMediator;
import cpen221.mp3.wikimediator.queries.InvalidQueryException;
import org.fastily.jwiki.core.Wiki;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class QueryTests {
    WikiMediator w;

    @Before
    public void startMediator(){
        w = new WikiMediator();
    }


    @Test
    public void queryTest1() throws InvalidQueryException {
        String query = "get author where author is 'Napoleon'";
        assertEquals(List.of("Napoleon"),w.executeQuery(query));
    }
    @Test
    public void queryTest2() throws InvalidQueryException{
        String query = "get category where category is 'testing'";
        assertEquals(List.of("testing"),w.executeQuery(query));
    }
    @Test
    public void queryTest3()throws InvalidQueryException {
        String query = "get page where page is 'Obscure Reference'";
        assertEquals(List.of("Obscure Reference"),w.executeQuery(query));
    }

    @Test
    public void queryTest4()throws InvalidQueryException{
        String query = "get page where category is 'Airports in East Java' asc";
        List<String> expected = List.of("Abdul Rachman Saleh Airport","Banyuwangi International Airport",
            "Bawean Airport","Iswahyudi Air Force Base","Juanda International Airport","Kediri Airport",
            "Notohadinegoro Airport","Trunojoyo Airport");
        assertEquals(expected,w.executeQuery(query));
    }

    @Test
    public void queryTest5()throws InvalidQueryException{
        String query = "get page where ((category is 'Airports in East Java' " +
            "and category is 'Transport in East Java') or category is 'Stonehouse, South Lanarkshire') asc";
        List<String> expected = List.of("Abdul Rachman Saleh Airport","Banyuwangi International Airport",
            "Category:People from Stonehouse, South Lanarkshire",
            "Juanda International Airport","List of listed buildings in Stonehouse, South Lanarkshire", "Notohadinegoro Airport",
            "Royal Albert F.C.", "Stonehouse Hospital","Stonehouse Violet F.C.",
            "Stonehouse, South Lanarkshire","Trunojoyo Airport");
        assertEquals(expected,w.executeQuery(query));
    }

    @Test
    public void queryTest6()throws InvalidQueryException{
        String query = "get page where (category is 'Sporting goods manufacturers of Germany' and" +
            " category is 'Tropical cyclones in 2012')";
        assertTrue(w.executeQuery(query).isEmpty());
    }

    @Test
    public void queryTest7()throws InvalidQueryException{
        String query = "get author where category is 'Novels by Anne Enright'";
        List<String> expected = List.of("Monkbot","Gobonobo");
        assertEquals(expected,w.executeQuery(query));
    }

    @Test
    public void queryTest8()throws InvalidQueryException{
        String query = "get category where page is 'Leica III'";
        List<String> expected = List.of("Category:Leica rangefinder cameras",
            "Category:Leica thread-mount cameras");
        List<String> result = w.executeQuery(query);
        for(String s: expected){
            assertTrue(result.contains(s));
        }
    }

    @Test
    public void queryTest9()throws InvalidQueryException{
        String query = "get author where page is 'Leica III'";
        List<String> expected = List.of("InternetArchiveBot");
        assertEquals(expected,w.executeQuery(query));
    }


    @Test
    public void queryTest10() throws InvalidQueryException{
        String query = "get author where page is 'Javier Gurruchaga'";
        List<String> expected = List.of("DefinitelyNotCarson");
        assertEquals(expected,w.executeQuery(query));
    }

    @Test
    public void queryTest11()throws InvalidQueryException{
        String query = "get page where author is 'DefinitelyNotCarson'";
        List<String> expected = List.of("Javier Gurruchaga");
        assertEquals(expected,w.executeQuery(query));
    }

    @Test
    public void queryTest12() throws InvalidQueryException {
        String query = "get category where author is 'DefinitelyNotCarson'";
        List<String> expected = List.of("Category:Spanish film actors","Category:1958 births",
            "Category:Spanish pop singers");
        List<String> result = w.executeQuery(query);
        for (String s : expected) {
            assertTrue(result.contains(s));
        }
    }

    @Test(expected = InvalidQueryException.class)
    public void queryTest13()throws InvalidQueryException{
        String query = "alksdjf";
        w.executeQuery(query);
    }

    @Test(expected = InvalidQueryException.class)
    public void queryTest14() throws InvalidQueryException {
        String query = "get author where page is Transport in Panama";
        w.executeQuery(query);
    }

    @Test
    public void queryTest15()throws InvalidQueryException{
        String query = "get page where author is 'jfalsdjf'";
       assertTrue(w.executeQuery(query).isEmpty());
    }

    @Test
    public void queryTest16()throws InvalidQueryException{
        String query = "get page where category is 'jfalsdjf'";
        assertTrue(w.executeQuery(query).isEmpty());
    }

    @Test
    public void queryTest17()throws InvalidQueryException{
        String query = "get author where page is 'jfalsdjf'";
        assertTrue(w.executeQuery(query).isEmpty());
    }
    @Test
    public void queryTest18()throws InvalidQueryException{
        String query = "get author where category is 'sdfjkl'";
        assertTrue(w.executeQuery(query).isEmpty());
    }

    @Test
    public void queryTest19()throws InvalidQueryException{
        String query = "get category where page is 'sdfjkl'";
        assertTrue(w.executeQuery(query).isEmpty());
    }

    @Test
    public void queryTest20()throws InvalidQueryException{
        String query = "get category where author is 'sdfjsklw'";
        assertTrue(w.executeQuery(query).isEmpty());
    }






}
