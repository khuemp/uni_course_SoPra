package sopra.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.sopra.config.JSONParser;
import de.unisaarland.cs.se.sopra.config.ParamMap;
import de.unisaarland.cs.se.sopra.crossroad.Consequence;
import de.unisaarland.cs.se.sopra.crossroad.ConsequenceFactory;
import de.unisaarland.cs.se.sopra.crossroad.ConsequenceFactoryImpl;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadFactory;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadFactoryImpl;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadType;
import de.unisaarland.cs.se.sopra.crossroad.SearchedCrossroad;
import de.unisaarland.cs.se.sopra.crossroad.SearchedNoLocCrossroad;
import de.unisaarland.cs.se.sopra.crossroad.SurvivorConsequence;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchSurvivorTest {

    final JSONObject crossroad1 = new JSONObject();
    final JSONObject searched = new JSONObject();
    final JSONObject consequence1 = new JSONObject();
    final JSONObject spawnSurvivorsChild = new JSONObject();
    final ParamMap crossroad1Params = new JSONParser.JSONParaMap(searched);

    final JSONObject crossroad2 = new JSONObject();
    final JSONObject searchedNoLoc = new JSONObject();
    final JSONObject consequence2 = new JSONObject();
    final JSONObject spawnSurvivorNoChild = new JSONObject();
    final ParamMap crossroad2Params = new JSONParser.JSONParaMap(searchedNoLoc);

    @BeforeEach
    void createJsonFile() {
        spawnSurvivorsChild.put("amount", 4);
        spawnSurvivorsChild.put("children", true);
        consequence1.put("spawnSurvivors", spawnSurvivorsChild);
        searched.put("identifier", 3);
        searched.put("locationId", 23);
        searched.put("consequence", consequence1);
        crossroad1.put("searched", searched);

        spawnSurvivorNoChild.put("amount", -4);
        consequence2.put("spawnSurvivors", spawnSurvivorNoChild);
        searchedNoLoc.put("identifier", 4);
        searchedNoLoc.put("consequence", consequence1);
        crossroad2.put("searched", searched);
    }

    @Test
    void createSurvivorChildTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("spawnSurvivors", consequence1);

        assertEquals(1, consequences.size());
        assertTrue(consequences.get(0) instanceof SurvivorConsequence);

        final SurvivorConsequence survivorConsequence = (SurvivorConsequence) consequences.get(0);
        assertEquals(4, survivorConsequence.getSpawnSurvivors());
        assertTrue(survivorConsequence.hasChildren());
    }

    @Test
    void createSearchedTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("spawnSurvivors", consequence1);
        final CrossroadFactory<Crossroad> crossroadFactory = new CrossroadFactoryImpl();
        final Crossroad
                crossroad =
                crossroadFactory.createCrossroad(3, "searched", crossroad1Params, consequences);

        assertTrue(crossroad instanceof SearchedCrossroad);
        assertEquals(consequences, crossroad.getConsequences());
        assertEquals(3, crossroad.getId());
        assertEquals(23, crossroad.getLocId());
        assertTrue(crossroad.hasLoc());
        assertEquals(0, crossroad.getNumTrash());
        assertEquals(CrossroadType.SEARCHED, crossroad.getType());
    }

    @Test
    void createSurvivorNoChildTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("spawnSurvivors", consequence2);

        assertEquals(1, consequences.size());
        assertTrue(consequences.get(0) instanceof SurvivorConsequence);

        final SurvivorConsequence survivorConsequence = (SurvivorConsequence) consequences.get(0);
        assertEquals(-4, survivorConsequence.getSpawnSurvivors());
        assertFalse(survivorConsequence.hasChildren());
    }

    @Test
    void createSearchedNoLocTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("spawnSurvivors", consequence2);
        final CrossroadFactory<Crossroad> crossroadFactory = new CrossroadFactoryImpl();
        final Crossroad
                crossroad =
                crossroadFactory.createCrossroad(4, "searched", crossroad2Params, consequences);

        assertTrue(crossroad instanceof SearchedNoLocCrossroad);
        assertEquals(consequences, crossroad.getConsequences());
        assertEquals(4, crossroad.getId());
        assertEquals(0, crossroad.getLocId());
        assertFalse(crossroad.hasLoc());
        assertEquals(0, crossroad.getNumTrash());
        assertEquals(CrossroadType.SEARCHED, crossroad.getType());
    }
}
