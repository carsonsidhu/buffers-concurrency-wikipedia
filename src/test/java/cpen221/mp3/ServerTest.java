package cpen221.mp3;

import cpen221.mp3.server.WikiMediatorClient;
import cpen221.mp3.server.WikiMediatorServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

public class ServerTest {
    private static final int WIKIMEDIATOR_PORT = 4949;
    private static Thread serverThread;

    @BeforeClass
    public static void startServer() {
        serverThread = new Thread(new Runnable() {
            public void run() {
                WikiMediatorServer server = new WikiMediatorServer(
                            WIKIMEDIATOR_PORT,1);
            }
        });
        serverThread.start();
    }

    @Test
    public void sequential_test_1() throws InterruptedException {
        try {
            WikiMediatorClient client = new WikiMediatorClient("localhost",
                WikiMediatorServer.WIKIMEDIATOR_PORT);

            String input = "{\n\t\"id\": \"1\",\n\t\"type\": \"search\",\n\t\"query\": \"Barack Obama\",\n\t\"limit\": \"12\"\n}";
            String input2 = "{\n\t\"id\": \"2\",\n\t\"type\": \"search\",\n\t\"query\": \"Barack Obama\",\n\t\"limit\": \"12\"\n}";

            client.sendRequest(input);
            client.sendRequest(input2);
            String reply = client.getReply();
            String reply2 = client.getReply();
            client.close();
            assertEquals("{ \"id\": \"1\", \"status\": \"success\", \"response\": [\"Barack Obama\", \"Barack Obama Sr.\", \"Barack Obama in comics\", \"Barack Obama on social media\", \"Barack Obama: Der schwarze Kennedy\", \"Bibliography of Barack Obama\", \"Family of Barack Obama\", \"Inauguration of Barack Obama\", \"List of things named after Barack Obama\", \"Public image of Barack Obama\", \"Speeches of Barack Obama\", \"Timeline of the Barack Obama presidency\"] }\n",reply);
            assertEquals("{ \"id\": \"2\", \"status\": \"success\", \"response\": [\"Barack Obama\", \"Barack Obama Sr.\", \"Barack Obama in comics\", \"Barack Obama on social media\", \"Barack Obama: Der schwarze Kennedy\", \"Bibliography of Barack Obama\", \"Family of Barack Obama\", \"Inauguration of Barack Obama\", \"List of things named after Barack Obama\", \"Presidential transition of Barack Obama\", \"Public image of Barack Obama\", \"Speeches of Barack Obama\"] }\n",reply);
            //System.out.println(reply);
            //System.out.println(reply2);

        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Test
    public void sequential_test_2() throws InterruptedException {
        try {
            WikiMediatorClient client = new WikiMediatorClient("localhost",
                    WikiMediatorServer.WIKIMEDIATOR_PORT);

            String input = "{\n\t\"id\": \"1\",\n\t\"timeout\": \"0\",\n\t\"type\": \"search\",\n\t\"query\": \"Barack Obama\",\n\t\"limit\": \"12\"\n}";

            client.sendRequest(input);
            String reply = client.getReply();
            client.close();
            assertEquals("{ \"id\": \"1\", \"status\": \"failed\", \"response\": \"Operation timed out\" }",reply);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Test
    public void sequential_test_3() throws InterruptedException {
        try {
            WikiMediatorClient client = new WikiMediatorClient("localhost",
                WikiMediatorServer.WIKIMEDIATOR_PORT);

            String input = "{\n\t\"id\": \"1\",\n\t\"type\": \"search\",\n\t\"query\": \"Donald Trump\",\n\t\"limit\": \"12\"\n}";
            String input2 = "{\n\t\"id\": \"2\",\n\t\"type\": \"search\",\n\t\"query\": \"Donald Trump\",\n\t\"limit\": \"12\"\n}";

            client.sendRequest(input);
            client.sendRequest(input2);
            String reply = client.getReply();
            String reply2 = client.getReply();
            client.close();
            assertEquals("{ \"id\": \"1\", \"status\": \"success\", \"response\": [\"Bibliography of Donald Trump\", \"Donald L. Trump\", \"Donald Trump\", \"Donald Trump 2016 presidential campaign\", \"Donald Trump Jr.\", \"Donald Trump filmography\", \"Donald Trump in music\", \"Donald Trump in popular culture\", \"Donald Trump on social media\", \"Family of Donald Trump\", \"Legal affairs of Donald Trump\", \"Protests against Donald Trump\"] }\n",reply);
            assertEquals("{ \"id\": \"2\", \"status\": \"success\", \"response\": [\"Bibliography of Donald Trump\", \"Donald L. Trump\", \"Donald Trump\", \"Donald Trump 2016 presidential campaign\", \"Donald Trump Jr.\", \"Donald Trump filmography\", \"Donald Trump in music\", \"Donald Trump in popular culture\", \"Donald Trump on social media\", \"Family of Donald Trump\", \"Legal affairs of Donald Trump\", \"Protests against Donald Trump\"] }\n",reply2);
            //System.out.println(reply2);
            //System.out.println(reply);

        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Test
    public void multi_test_1() throws InterruptedException {
        Thread serverThread = new Thread(new Runnable() {
            public void run() {
                try {
                    WikiMediatorClient client = new WikiMediatorClient("localhost",
                            WikiMediatorServer.WIKIMEDIATOR_PORT);

                    String input = "{\n\t\"id\": \"1\",\n\t\"type\": \"search\",\n\t\"query\": \"Barack Obama\",\n\t\"limit\": \"12\"\n}";
                    String input2 = "{\n\t\"id\": \"2\",\n\t\"type\": \"search\",\n\t\"query\": \"Barack Obama\",\n\t\"limit\": \"12\"\n}";

                    client.sendRequest(input);
                    client.sendRequest(input2);
                    String reply = client.getReply();
                    String reply2 = client.getReply();
                    client.close();
                    assertEquals("{ \"id\": \"1\", \"status\": \"success\", \"response\": [\"Barack Obama\", \"Barack Obama Sr.\", \"Barack Obama in comics\", \"Barack Obama on social media\", \"Barack Obama: Der schwarze Kennedy\", \"Bibliography of Barack Obama\", \"Family of Barack Obama\", \"Inauguration of Barack Obama\", \"List of things named after Barack Obama\", \"Presidential transition of Barack Obama\", \"Public image of Barack Obama\", \"Speeches of Barack Obama\"] }\n",reply);
                    assertEquals("{ \"id\": \"2\", \"status\": \"success\", \"response\": [\"Barack Obama\", \"Barack Obama Sr.\", \"Barack Obama in comics\", \"Barack Obama on social media\", \"Barack Obama: Der schwarze Kennedy\", \"Bibliography of Barack Obama\", \"Family of Barack Obama\", \"Inauguration of Barack Obama\", \"List of things named after Barack Obama\", \"Presidential transition of Barack Obama\", \"Public image of Barack Obama\", \"Speeches of Barack Obama\"] }\n",reply2);
                    //System.out.println(reply);
                    //System.out.println(reply2);

                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        serverThread.start();

        try {
            WikiMediatorClient client = new WikiMediatorClient("localhost",
                    WikiMediatorServer.WIKIMEDIATOR_PORT);

            String input = "{\n\t\"id\": \"3\",\n\t\"type\": \"search\",\n\t\"query\": \"Barack Obama\",\n\t\"limit\": \"12\"\n}";
            String input2 = "{\n\t\"id\": \"4\",\n\t\"type\": \"search\",\n\t\"query\": \"Barack Obama\",\n\t\"limit\": \"12\"\n}";

            client.sendRequest(input);
            client.sendRequest(input2);
            String reply = client.getReply();
            String reply2 = client.getReply();
            client.close();
            assertEquals("{ \"id\": \"3\", \"status\": \"success\", \"response\": [\"Barack Obama\", \"Barack Obama Sr.\", \"Barack Obama in comics\", \"Barack Obama on social media\", \"Barack Obama: Der schwarze Kennedy\", \"Bibliography of Barack Obama\", \"Family of Barack Obama\", \"Inauguration of Barack Obama\", \"List of things named after Barack Obama\", \"Presidential transition of Barack Obama\", \"Public image of Barack Obama\", \"Speeches of Barack Obama\"] }",reply);
            assertEquals("{ \"id\": \"4\", \"status\": \"success\", \"response\": [\"Barack Obama\", \"Barack Obama Sr.\", \"Barack Obama in comics\", \"Barack Obama on social media\", \"Barack Obama: Der schwarze Kennedy\", \"Bibliography of Barack Obama\", \"Family of Barack Obama\", \"Inauguration of Barack Obama\", \"List of things named after Barack Obama\", \"Presidential transition of Barack Obama\", \"Public image of Barack Obama\", \"Speeches of Barack Obama\"] }",reply2);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @Test
    public void getPageTest(){
        try {
            WikiMediatorClient client = new WikiMediatorClient("localhost",
                    WikiMediatorServer.WIKIMEDIATOR_PORT);

            String input = "{\n\t\"id\": \"getPageTest\",\n\t\"type\": \"getPage\",\n\t\"pageTitle\": \"Kisumu Central Constituency\"\n}";

            client.sendRequest(input);
            String reply = client.getReply();
            client.close();
            //System.out.println(reply);
            assertEquals("{ \"id\": \"getPageTest\", \"status\": \"success\", \"response\": \"{{Infobox settlement<!--See the Table at Infobox Settlement for all fields and descriptions of usage--><!-- Basic info  ---------------->| official_name                   = Kisumu Central| native_name                     = <!-- for cities whose native name is not in English -->| other_name                      = | settlement_type                 = [[List of constituencies of Kenya|constituency]]| image_skyline                   = | imagesize                       = | image_caption                   = | image_flag                      = | image_blank_emblem              = | blank_emblem_type               = | blank_emblem_size               = | nickname                        = | motto                           = <!-- images and maps  ----------->| image_map                       = | mapsize                         = | map_caption                     = | image_map1                      = | mapsize1                        = | map_caption1                    = | pushpin_map                     = Central African Republic <!-- the name of a location map as per http://en.wikipedia.org/wiki/Template:Location_map -->| pushpin_label_position          = bottom| pushpin_map_caption             = <!-- Location ------------------>| coordinates                     = | subdivision_type                = Country| subdivision_name                = {{flag|Kenya}}| subdivision_type1               = County| subdivision_type2               = | subdivision_type3               = | subdivision_type4               = | subdivision_name1               = [[Kisumu County]]| subdivision_name2               = | subdivision_name3               = | subdivision_name4               = <!-- Politics ----------------->| established_title               = <!-- Settled -->| established_date                = | established_title2              = <!-- Incorporated (town) -->| established_date2               = | established_title3              = <!-- Incorporated (city) -->| established_date3               = <!-- Area    --------------------->| government_footnotes            = | government_type                 = | leader_title                    = | leader_name                     = | leader_title1                   = <!-- for places with, say, both a mayor and a city manager -->| leader_name1                    = | unit_pref                       = Imperial <!--Enter: Imperial, if Imperial (metric) is desired-->| area_footnotes                  = | area_magnitude                  = | area_total_km2                  = | area_total_sq_mi                = | area_land_km2                   = <!--See table @ Template:Infobox Settlement for details on automatic unit conversion-->| area_land_sq_mi                 = | area_water_km2                  = | area_water_sq_mi                = | area_water_percent              = | area_urban_km2                  = | area_urban_sq_mi                = | area_metro_km2                  = | area_metro_sq_mi                = | area_blank1_title               = | area_blank1_km2                 = | area_blank1_sq_mi               = <!-- Population   ----------------------->| elevation_footnotes             = <!--for references: use <ref> </ref> tags-->| elevation_m                     = | elevation_ft                    = <!-- Area/postal codes & others -------->| population_total                = | population_as_of                = | population_footnotes            = | population_density_km2          = | population_density_sq_mi        = <!-- General information  --------------->| population_note                 = | postal_code_type                = <!-- enter ZIP code, Postcode, Post code, Postal code... -->| website                         = | footnotes                       = | image_dot_map                   = | dot_mapsize                     = | dot_map_caption                 = | dot_x                           = | dot_y                           = | leader_title2                   = | leader_name2                    = | leader_title3                   = | leader_name3                    = | leader_title4                   = | leader_name4                    = | timezone                        = | utc_offset                      = | timezone_DST                    = | utc_offset_DST                  = }} '''Kisumu Central''' is a [[List of constituencies of Kenya|constituency]] in [[Kenya]]. It is one of seven constituencies in [[Kisumu County]].<ref>{{Cite web|url=https://softkenya.com/kenya/constituencies-in-kenya/|title=Constituency – Constituencies in Kenya|website=softkenya|access-date=9 June 2018}}</ref><ref>{{Cite web|url=http://www.ngcdf.go.ke/index.php/2015-07-28-04-03-42|title=Constituencies|website=The National Government Constituencies Development Fund (NG-CDF)|access-date=9 June 2018}}</ref>== References =={{reflist}}{{coord missing|Kenya}}[[Category:Constituencies in Kisumu County]]{{Kenya-geo-stub}}\" }",reply);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Test
    public void searchTest1(){
        try {
            WikiMediatorClient client = new WikiMediatorClient("localhost",
                    WikiMediatorServer.WIKIMEDIATOR_PORT);

            String input = "{\n\t\"id\": \"1\",\n\t\"type\": \"search\",\n\t\"query\": \"Donald Trump\",\n\t\"limit\": \"12\"\n}";
            String input2 = "{\n\t\"id\": \"2\",\n\t\"type\": \"search\",\n\t\"query\": \"Donald Trump\",\n\t\"limit\": \"12\"\n}";
            String input3 = "{\n\t\"id\": \"1\",\n\t\"type\": \"zeitgeist\",\n\t\"limit\": \"8\"\n}";
            String input4 = "{\n\t\"id\": \"1\",\n\t\"type\": \"peakLoad30s\"\n}";

            client.sendRequest(input);
            client.sendRequest(input2);
            String reply = client.getReply();
            String reply2 = client.getReply();

            client.sendRequest(input3);
            String reply3 = client.getReply();
            client.sendRequest(input4);
            String reply4 = client.getReply();
            client.close();
            assertEquals("{ \"id\": \"1\", \"status\": \"success\", \"response\": [\"Bibliography of Donald Trump\", \"Donald L. Trump\", \"Donald Trump\", \"Donald Trump 2016 presidential campaign\", \"Donald Trump Jr.\", \"Donald Trump filmography\", \"Donald Trump in music\", \"Donald Trump in popular culture\", \"Donald Trump on social media\", \"Family of Donald Trump\", \"Legal affairs of Donald Trump\", \"Protests against Donald Trump\"] }\n",reply);
            assertEquals("{ \"id\": \"2\", \"status\": \"success\", \"response\": [\"Bibliography of Donald Trump\", \"Donald L. Trump\", \"Donald Trump\", \"Donald Trump 2016 presidential campaign\", \"Donald Trump Jr.\", \"Donald Trump filmography\", \"Donald Trump in music\", \"Donald Trump in popular culture\", \"Donald Trump on social media\", \"Family of Donald Trump\", \"Legal affairs of Donald Trump\", \"Protests against Donald Trump\"] }\n",reply2);
            assertEquals("{ \"id\": \"1\", \"status\": \"success\", \"response\": [\"Barack Obama\", \"Donald Trump\", \"Kisumu Central Constituency\"] }",reply3);
            assertEquals("{ \"id\": \"1\", \"status\": \"success\", \"response\": \"12\" }",reply4);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    @AfterClass
    public static void stopServer() throws IOException {
        WikiMediatorClient client = new WikiMediatorClient("localhost",
                WikiMediatorServer.WIKIMEDIATOR_PORT);

        String input = "{\n\t\"id\": \"5\",\n\t\"type\": \"stop\"\n}";

        client.sendRequest(input);
        String reply = client.getReply();
        client.close();
        assertEquals(reply,"{ \"id\": \"5\", \"type\": \"bye\" }");
        //System.out.println(reply);

    }
}
