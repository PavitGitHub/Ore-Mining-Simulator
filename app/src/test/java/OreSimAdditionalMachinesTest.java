import ore.ElementType;
import ore.MapGrid;
import ore.OreSim;
import ore.PropertiesLoader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Team 06
 * Pavit Vathna and Daniel Duong
 */

public class OreSimAdditionalMachinesTest {
    static String[]logLines;
    @BeforeClass
    public static void setupGame() {
        String propertiesPath = "properties/test2.properties";
        final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesPath);

        int model = Integer.parseInt(properties.getProperty("map"));
        MapGrid grid = new MapGrid(model);

        String logResult = new OreSim(properties, grid).runApp(true);
        logLines = logResult.split("\n");
        System.out.println("logResult = " + logResult);
        System.out.println("logLines = " + logLines.length);
        System.out.println("logLines = " + logLines);
    }
/*
    @Test(timeout = 20000)
    public void testEndGame() {
        // Test for the end result
        int END_LINE_NUMBER = 59;
        String endLine = logLines[END_LINE_NUMBER - 1];
        OreTestData oreTestData = new OreTestData(END_LINE_NUMBER, endLine);
        assertEquals(0, oreTestData.actorLocations.get(ElementType.TARGET).size());
        assertEquals(1, oreTestData.actorLocations.get(ElementType.PUSHER).size());
        assertEquals("2-3", oreTestData.actorLocations.get(ElementType.PUSHER).stream().findFirst().get());
        assertEquals(0, oreTestData.actorLocations.get(ElementType.ROCK).size());
        assertEquals(0, oreTestData.actorLocations.get(ElementType.CLAY).size());
    }
*/
    @Test(timeout = 20000)
    public void testOreBlockingExcavator() {
        // Test for the end result
        int BLOCKING_LINE_NUMBER = 7;
        String endLine = logLines[BLOCKING_LINE_NUMBER - 1];
        OreTestData oreTestData = new OreTestData(BLOCKING_LINE_NUMBER, endLine);
        assertEquals(2, oreTestData.actorLocations.get(ElementType.TARGET).size());
        assertEquals(2, oreTestData.actorLocations.get(ElementType.ORE).size());
        assertEquals("8-5", oreTestData.actorLocations.get(ElementType.EXCAVATOR).stream().findFirst().get());
        assertEquals(6, oreTestData.actorLocations.get(ElementType.ROCK).size());
        assertEquals(5, oreTestData.actorLocations.get(ElementType.CLAY).size());
    }

    @Test(timeout = 20000)
    public void testClayBlockingExcavator() {
        // Test for the end result
        int BLOCKING_LINE_NUMBER = 8;
        String endLine = logLines[BLOCKING_LINE_NUMBER - 1];
        OreTestData oreTestData = new OreTestData(BLOCKING_LINE_NUMBER, endLine);
        assertEquals(2, oreTestData.actorLocations.get(ElementType.TARGET).size());
        assertEquals(2, oreTestData.actorLocations.get(ElementType.ORE).size());
        assertEquals("8-5", oreTestData.actorLocations.get(ElementType.EXCAVATOR).stream().findFirst().get());
        assertEquals(6, oreTestData.actorLocations.get(ElementType.ROCK).size());
        assertEquals(5, oreTestData.actorLocations.get(ElementType.CLAY).size());
    }

    @Test(timeout = 20000)
    public void testRockBlockingBulldozer() {
        // Test for the end result
        int BLOCKING_LINE_NUMBER = 16;
        String endLine = logLines[BLOCKING_LINE_NUMBER - 1];
        OreTestData oreTestData = new OreTestData(BLOCKING_LINE_NUMBER, endLine);
        assertEquals(2, oreTestData.actorLocations.get(ElementType.TARGET).size());
        assertEquals(2, oreTestData.actorLocations.get(ElementType.ORE).size());
        assertEquals("6-2", oreTestData.actorLocations.get(ElementType.BULLDOZER).stream().findFirst().get());
        assertEquals(1, oreTestData.actorLocations.get(ElementType.ROCK).size());
        assertEquals(5, oreTestData.actorLocations.get(ElementType.CLAY).size());
    }

    @Test(timeout = 20000)
    public void testEndResult() {
        // Test for the end result
        int END_LINE_NUMBER = 60;
        String endLine = logLines[END_LINE_NUMBER - 1];
        OreTestData oreTestData = new OreTestData(END_LINE_NUMBER, endLine);
        assertEquals(0, oreTestData.actorLocations.get(ElementType.TARGET).size());
        assertEquals(1, oreTestData.actorLocations.get(ElementType.PUSHER).size());
        assertEquals("2-3", oreTestData.actorLocations.get(ElementType.PUSHER).stream().findFirst().get());
        assertEquals(0, oreTestData.actorLocations.get(ElementType.ROCK).size());
        assertEquals(0, oreTestData.actorLocations.get(ElementType.CLAY).size());
    }
}
