package cpen221.mp3;


import cpen221.mp3.wikimediator.WikiMediator;
import cpen221.mp3.wikimediator.queries.InvalidQueryException;
import org.junit.Test;


import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;



public class WikiMediatorTests {
    //if LONG_TESTS is false, trendingTest2 will always fail
    private final static boolean LONG_TESTS = true;

    @Test
    public void getPageTest1(){

        WikiMediator w = new WikiMediator();
        assertEquals("{{Infobox settlement\n" +
            "<!--See the Table at Infobox Settlement for all fields and descriptions of usage-->\n" +
            "<!-- Basic info  ---------------->| official_name                   = Kisumu Central\n" +
            "| native_name                     = <!-- for cities whose native name is not in English -->\n" +
            "| other_name                      = \n" +
            "| settlement_type                 = [[List of constituencies of Kenya|constituency]]\n" +
            "| image_skyline                   = \n" +
            "| imagesize                       = \n" +
            "| image_caption                   = \n" +
            "| image_flag                      = \n" +
            "| image_blank_emblem              = \n" +
            "| blank_emblem_type               = \n" +
            "| blank_emblem_size               = \n" +
            "| nickname                        = \n" +
            "| motto                           = <!-- images and maps  ----------->\n" +
            "| image_map                       = \n" +
            "| mapsize                         = \n" +
            "| map_caption                     = \n" +
            "| image_map1                      = \n" +
            "| mapsize1                        = \n" +
            "| map_caption1                    = \n" +
            "| pushpin_map                     = Central African Republic <!-- the name of a location map as per http://en.wikipedia.org/wiki/Template:Location_map -->\n" +
            "| pushpin_label_position          = bottom\n" +
            "| pushpin_map_caption             = <!-- Location ------------------>\n" +
            "| coordinates                     = \n" +
            "| subdivision_type                = Country\n" +
            "| subdivision_name                = {{flag|Kenya}}\n" +
            "| subdivision_type1               = County\n" +
            "| subdivision_type2               = \n" +
            "| subdivision_type3               = \n" +
            "| subdivision_type4               = \n" +
            "| subdivision_name1               = [[Kisumu County]]\n" +
            "| subdivision_name2               = \n" +
            "| subdivision_name3               = \n" +
            "| subdivision_name4               = <!-- Politics ----------------->\n" +
            "| established_title               = <!-- Settled -->\n" +
            "| established_date                = \n" +
            "| established_title2              = <!-- Incorporated (town) -->\n" +
            "| established_date2               = \n" +
            "| established_title3              = <!-- Incorporated (city) -->\n" +
            "| established_date3               = <!-- Area    --------------------->\n" +
            "| government_footnotes            = \n" +
            "| government_type                 = \n" +
            "| leader_title                    = \n" +
            "| leader_name                     = \n" +
            "| leader_title1                   = <!-- for places with, say, both a mayor and a city manager -->\n" +
            "| leader_name1                    = \n" +
            "| unit_pref                       = Imperial <!--Enter: Imperial, if Imperial (metric) is desired-->\n" +
            "| area_footnotes                  = \n" +
            "| area_magnitude                  = \n" +
            "| area_total_km2                  = \n" +
            "| area_total_sq_mi                = \n" +
            "| area_land_km2                   = <!--See table @ Template:Infobox Settlement for details on automatic unit conversion-->\n" +
            "| area_land_sq_mi                 = \n" +
            "| area_water_km2                  = \n" +
            "| area_water_sq_mi                = \n" +
            "| area_water_percent              = \n" +
            "| area_urban_km2                  = \n" +
            "| area_urban_sq_mi                = \n" +
            "| area_metro_km2                  = \n" +
            "| area_metro_sq_mi                = \n" +
            "| area_blank1_title               = \n" +
            "| area_blank1_km2                 = \n" +
            "| area_blank1_sq_mi               = <!-- Population   ----------------------->\n" +
            "| elevation_footnotes             = <!--for references: use <ref> </ref> tags-->\n" +
            "| elevation_m                     = \n" +
            "| elevation_ft                    = <!-- Area/postal codes & others -------->\n" +
            "| population_total                = \n" +
            "| population_as_of                = \n" +
            "| population_footnotes            = \n" +
            "| population_density_km2          = \n" +
            "| population_density_sq_mi        = <!-- General information  --------------->\n" +
            "| population_note                 = \n" +
            "| postal_code_type                = <!-- enter ZIP code, Postcode, Post code, Postal code... -->\n" +
            "| website                         = \n" +
            "| footnotes                       = \n" +
            "| image_dot_map                   = \n" +
            "| dot_mapsize                     = \n" +
            "| dot_map_caption                 = \n" +
            "| dot_x                           = \n" +
            "| dot_y                           = \n" +
            "| leader_title2                   = \n" +
            "| leader_name2                    = \n" +
            "| leader_title3                   = \n" +
            "| leader_name3                    = \n" +
            "| leader_title4                   = \n" +
            "| leader_name4                    = \n" +
            "| timezone                        = \n" +
            "| utc_offset                      = \n" +
            "| timezone_DST                    = \n" +
            "| utc_offset_DST                  = \n" +
            "}} \n" +
            "'''Kisumu Central''' is a [[List of constituencies of Kenya|constituency]] in [[Kenya]]. It is one of seven constituencies in [[Kisumu County]].<ref>{{Cite web|url=https://softkenya.com/kenya/constituencies-in-kenya/|title=Constituency – Constituencies in Kenya|website=softkenya|access-date=9 June 2018}}</ref><ref>{{Cite web|url=http://www.ngcdf.go.ke/index.php/2015-07-28-04-03-42|title=Constituencies|website=The National Government Constituencies Development Fund (NG-CDF)|access-date=9 June 2018}}</ref>\n" +
            "\n" +
            "== References ==\n" +
            "{{reflist}}\n" +
            "{{coord missing|Kenya}}\n" +
            "\n" +
            "[[Category:Constituencies in Kisumu County]]\n" +
            "\n" +
            "{{Kenya-geo-stub}}",w.getPage("Kisumu Central Constituency"));
    }

    @Test
    public void searchTest1(){
        WikiMediator w = new WikiMediator();
        List<String> searchList = w.search("cookie",10);
        List<String> expected = List.of("Cookie","HTTP cookie", "Chocolate chip cookie", "Cookie Crisp");

        for(String s : expected){
            assertTrue(searchList.contains(s));
        }

    }

    @Test
    public void cachingSpeedTest() throws InterruptedException {
        WikiMediator w = new WikiMediator();
        long startTime = System.nanoTime();
        assertEquals("{{speciesbox\n" +
            "|image = Aspidoscelis tesselata.jpeg\n" +
            "| status = LC\n" +
            "| status_system = IUCN3.1\n" +
            "| genus = Aspidoscelis\n" +
            "| species = tesselatus\n" +
            "| authority = ([[Thomas Say|Say]], 1823)\n" +
            "| synonyms = *''Ameiva tesselata''<br>{{small|Say, 1823}}\n" +
            "*''Cnemidophorus grahamii''<br>{{small|[[Spencer Fullerton Baird|Baird]] & [[Charles Frédéric Girard|Girard]], 1852}}\n" +
            "*''Aspidoscelis tesselata''<br>{{small|[[Tod W. Reeder|Reeder]], 2002}}\n" +
            "*''Cnemidophorus tesselatus''<br>{{small|SMitH & BUrGer 1949}}\n" +
            "}}\n" +
            "\n" +
            "The '''checkered whiptail''' (''Aspidoscelis tesselatus'') is a [[species]] of [[lizard]] found in the southwestern [[United States]] in [[Colorado]], [[Texas]] and [[New Mexico]], and in northern [[Mexico]] in [[Chihuahua (state)|Chihuahua]] and [[Coahuila]]. Many sources believe that the species originated from the [[Hybrid (biology)|hybridization]] of the marbled whiptail, ''[[Aspidoscelis marmoratus]]'', the plateau spotted whiptail, ''[[Aspidoscelis septemvittatus]]'', and possibly the six-lined racerunner, ''[[Aspidoscelis sexlineatus]]''. It is one of many lizard species known to be [[parthenogenesis|parthenogenic]]. It is sometimes referred to as the '''common checkered whiptail''' to differentiate it from several other species known as checkered whiptails.\n" +
            "\n" +
            "== Description ==\n" +
            "The checkered whiptail grows to about 4 inches in length. Their pattern and base coloration varies widely, with brown or black blotching, checkering or striping on a pale yellow or white base color. Their rear legs often have dark spotting, and their underside is usually white with dark flecking on the throat area. They are slender bodied, with a long [[tail]].\n" +
            "\n" +
            "== Behavior ==\n" +
            "Like other species of whiptail lizard, the checkered whiptail is [[diurnal animal|diurnal]] and [[insectivore|insectivorous]]. They are wary, energetic, and fast moving, darting for cover if approached. They are found in semi-arid, rocky [[habitat (ecology)|habitats]], normally in canyon lands or hilled regions. They are [[parthenogenic]], laying up to eight unfertilized [[egg (biology)|eggs]] in mid summer, which hatch in six to eight weeks.\n" +
            "\n" +
            "== References ==\n" +
            "* {{EMBL genus|genus=Aspidoscelis|species=tesselatus}}\n" +
            "* [http://www.zo.utexas.edu/research/txherps/lizards/cnemidophorus.tesselatus.html Herps of Texas: ''Cnemidophorus tesselatus'']\n" +
            "\n" +
            "{{Taxonbar|from=Q2866860}}\n" +
            "\n" +
            "{{DEFAULTSORT:Whiptail, Common Checkered}}\n" +
            "[[Category:Aspidoscelis]]\n" +
            "[[Category:Fauna of the Southwestern United States]]\n" +
            "[[Category:Reptiles of the United States]]\n" +
            "[[Category:Reptiles of Mexico]]\n" +
            "[[Category:Reptiles described in 1823]]\n" +
            "[[Category:Taxa named by Thomas Say]]",w.getPage("Common checkered whiptail"));
        long firstPull = System.nanoTime();
        TimeUnit.MILLISECONDS.sleep(100);
        assertEquals("{{speciesbox\n" +
            "|image = Aspidoscelis tesselata.jpeg\n" +
            "| status = LC\n" +
            "| status_system = IUCN3.1\n" +
            "| genus = Aspidoscelis\n" +
            "| species = tesselatus\n" +
            "| authority = ([[Thomas Say|Say]], 1823)\n" +
            "| synonyms = *''Ameiva tesselata''<br>{{small|Say, 1823}}\n" +
            "*''Cnemidophorus grahamii''<br>{{small|[[Spencer Fullerton Baird|Baird]] & [[Charles Frédéric Girard|Girard]], 1852}}\n" +
            "*''Aspidoscelis tesselata''<br>{{small|[[Tod W. Reeder|Reeder]], 2002}}\n" +
            "*''Cnemidophorus tesselatus''<br>{{small|SMitH & BUrGer 1949}}\n" +
            "}}\n" +
            "\n" +
            "The '''checkered whiptail''' (''Aspidoscelis tesselatus'') is a [[species]] of [[lizard]] found in the southwestern [[United States]] in [[Colorado]], [[Texas]] and [[New Mexico]], and in northern [[Mexico]] in [[Chihuahua (state)|Chihuahua]] and [[Coahuila]]. Many sources believe that the species originated from the [[Hybrid (biology)|hybridization]] of the marbled whiptail, ''[[Aspidoscelis marmoratus]]'', the plateau spotted whiptail, ''[[Aspidoscelis septemvittatus]]'', and possibly the six-lined racerunner, ''[[Aspidoscelis sexlineatus]]''. It is one of many lizard species known to be [[parthenogenesis|parthenogenic]]. It is sometimes referred to as the '''common checkered whiptail''' to differentiate it from several other species known as checkered whiptails.\n" +
            "\n" +
            "== Description ==\n" +
            "The checkered whiptail grows to about 4 inches in length. Their pattern and base coloration varies widely, with brown or black blotching, checkering or striping on a pale yellow or white base color. Their rear legs often have dark spotting, and their underside is usually white with dark flecking on the throat area. They are slender bodied, with a long [[tail]].\n" +
            "\n" +
            "== Behavior ==\n" +
            "Like other species of whiptail lizard, the checkered whiptail is [[diurnal animal|diurnal]] and [[insectivore|insectivorous]]. They are wary, energetic, and fast moving, darting for cover if approached. They are found in semi-arid, rocky [[habitat (ecology)|habitats]], normally in canyon lands or hilled regions. They are [[parthenogenic]], laying up to eight unfertilized [[egg (biology)|eggs]] in mid summer, which hatch in six to eight weeks.\n" +
            "\n" +
            "== References ==\n" +
            "* {{EMBL genus|genus=Aspidoscelis|species=tesselatus}}\n" +
            "* [http://www.zo.utexas.edu/research/txherps/lizards/cnemidophorus.tesselatus.html Herps of Texas: ''Cnemidophorus tesselatus'']\n" +
            "\n" +
            "{{Taxonbar|from=Q2866860}}\n" +
            "\n" +
            "{{DEFAULTSORT:Whiptail, Common Checkered}}\n" +
            "[[Category:Aspidoscelis]]\n" +
            "[[Category:Fauna of the Southwestern United States]]\n" +
            "[[Category:Reptiles of the United States]]\n" +
            "[[Category:Reptiles of Mexico]]\n" +
            "[[Category:Reptiles described in 1823]]\n" +
            "[[Category:Taxa named by Thomas Say]]",w.getPage("Common checkered whiptail"));
        long secondPull = System.nanoTime();

        assertTrue(secondPull-firstPull < firstPull - startTime);
    }

    @Test
    public void zeitgeistTest1(){
        WikiMediator w = new WikiMediator();
        int expectedLoad = 15;
        w.getPage("Engineering physics");
        w.getPage("Engineering physics");
        w.getPage("Engineering physics");
        w.getPage("SpaceX");
        w.getPage("SpaceX");
        w.search("Engineering physics",5);
        w.search("Engineering physics",5);
        w.search("Engineering",2);
        w.search("Engineering",2);
        w.search("Engineering",2);
        w.search("physics", 8);
        w.getPage("Lexicographic order");
        w.search("physics is hard",1);

        assertEquals(List.of("Engineering physics","Engineering","SpaceX", "Lexicographic order",
            "physics", "physics is hard"),w.zeitgeist(8));
        assertEquals(expectedLoad,w.peakLoad30s());
    }
    @Test
    public void zeitgeistTest2(){
        WikiMediator w = new WikiMediator();
        int endExpectedLoad = 14;
        int midExpectedLoad = 6;
        w.getPage("Engineering physics");
        w.getPage("Engineering physics");
        w.getPage("Engineering physics");
        w.getPage("SpaceX");
        w.getPage("SpaceX");

        assertEquals(midExpectedLoad, w.peakLoad30s());

        w.search("Engineering physics",5);
        w.search("Engineering physics",5);
        w.search("Engineering",2);
        w.search("Engineering",2);
        w.search("Engineering",2);
        w.search("physics", 8);

        assertEquals(List.of("Engineering physics","Engineering"),w.zeitgeist(2));
        assertEquals(endExpectedLoad, w.peakLoad30s());
    }

    @Test
    public void trendingTest1(){
        WikiMediator w = new WikiMediator();
        int expectedLoad = 13;
        w.getPage("Engineering physics");
        w.getPage("Engineering physics");
        w.getPage("Engineering physics");
        w.getPage("SpaceX");
        w.getPage("SpaceX");
        w.search("Engineering physics",5);
        w.search("Engineering physics",5);
        w.search("Engineering",2);
        w.search("Engineering",2);
        w.search("Engineering",2);
        w.search("physics", 8);

        assertEquals(List.of("Engineering physics","Engineering","SpaceX","physics"),w.trending(5));
        assertEquals(expectedLoad,w.peakLoad30s());

    }

    @Test
    public void trendingTest2() throws InterruptedException {

        if(LONG_TESTS) {

            WikiMediator w = new WikiMediator();

            assertEquals(1, w.peakLoad30s());
            w.getPage("Engineering physics");
            w.getPage("Engineering physics");
            w.getPage("Engineering physics");
            w.getPage("SpaceX");
            w.getPage("SpaceX");
            w.search("Engineering physics", 5);
            w.search("Engineering physics", 5);
            w.search("Engineering", 2);
            w.search("Engineering", 2);
            w.search("Engineering", 2);
            w.search("physics", 8);
            assertEquals(List.of("Engineering physics", "Engineering"),w.trending(2));

            TimeUnit.SECONDS.sleep(30);

            w.getPage("Coq");
            w.getPage("Coq");
            w.search("Coq", 4);
            w.search("Coq", 4);
            w.search("Object-oriented programming", 9);
            w.getPage("MATLAB");
            w.search("MATLAB", 1);


            assertEquals(List.of("Coq", "MATLAB", "Object-oriented programming"), w.trending(5));
            assertEquals(List.of("Engineering physics", "Coq", "Engineering"), w.zeitgeist(3));
            int expectedLoad = 13;
            assertEquals(expectedLoad, w.peakLoad30s());
        }
        else{
            assertEquals(1, 0);
        }
    }


    @Test
    public void peakLoad30Test1() throws InvalidQueryException {
        WikiMediator w = new WikiMediator();
        int firstExpectedLoad = 6;
        int secondExpectedLoad = 11;
        int thirdExpectedLoad = 12;

        w.executeQuery("get page where category is 'Novels by Anne Enright'");
        w.search("test",8);
        w.getPage("MATLAB");
        w.trending(5);
        w.zeitgeist(6);

        assertEquals(firstExpectedLoad,w.peakLoad30s());

        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.executeQuery("get author where page is 'Leica III'");
        w.search("testing", 1);
        w.zeitgeist(3);
        w.trending(2);
        assertEquals(secondExpectedLoad,w.peakLoad30s());

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.zeitgeist(3);
        w.trending(2);
        w.zeitgeist(3);
        w.trending(2);
        w.peakLoad30s();
        w.search("pain",2);
        assertEquals(thirdExpectedLoad,w.peakLoad30s());

    }


    @Test
    public void invalidPageTest(){
        WikiMediator w = new WikiMediator();
        assertEquals("",w.getPage("Sjaklf"));
    }


}
