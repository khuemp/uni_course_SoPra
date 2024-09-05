package sopra.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.sopra.config.JSONParser;
import de.unisaarland.cs.se.sopra.config.ParamMap;
import de.unisaarland.cs.se.sopra.crossroad.BarricadedCrossroad;
import de.unisaarland.cs.se.sopra.crossroad.BarricadedNoLocCrossroad;
import de.unisaarland.cs.se.sopra.crossroad.Consequence;
import de.unisaarland.cs.se.sopra.crossroad.ConsequenceFactory;
import de.unisaarland.cs.se.sopra.crossroad.ConsequenceFactoryImpl;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadFactory;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadFactoryImpl;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadType;
import de.unisaarland.cs.se.sopra.crossroad.FoodConsequence;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BarricadeFoodTest {

    final JSONObject crossroad1 = new JSONObject();
    final JSONObject barricaded = new JSONObject();
    final JSONObject consequence1 = new JSONObject();
    final JSONObject changeFoodPositive = new JSONObject();
    final ParamMap crossroad1Params = new JSONParser.JSONParaMap(barricaded);

    final JSONObject crossroad2 = new JSONObject();
    final JSONObject barricadedNoLoc = new JSONObject();
    final JSONObject consequence2 = new JSONObject();
    final JSONObject changeFoodNegative = new JSONObject();
    final ParamMap crossroad2Params = new JSONParser.JSONParaMap(barricadedNoLoc);

    @BeforeEach
    void createJsonFile() {
        changeFoodPositive.put("amount", 2);
        consequence1.put("changeFood", changeFoodPositive);
        barricaded.put("identifier", 1);
        barricaded.put("locationId", 42);
        barricaded.put("consequence", consequence1);
        crossroad1.put("barricaded", barricaded);

        changeFoodNegative.put("amount", -2);
        consequence2.put("changeFood", changeFoodNegative);
        barricadedNoLoc.put("identifier", 2);
        barricadedNoLoc.put("consequence", consequence1);
        crossroad2.put("barricaded", barricaded);
    }

    @Test
    void createFoodPositiveTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("changeFood", consequence1);

        assertEquals(1, consequences.size());
        assertTrue(consequences.get(0) instanceof FoodConsequence);

        final FoodConsequence foodConsequence = (FoodConsequence) consequences.get(0);
        assertEquals(2, foodConsequence.getChangeFood());
    }

    @Test
    void createBarricadedTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("changeFood", consequence1);
        final CrossroadFactory<Crossroad> crossroadFactory = new CrossroadFactoryImpl();
        final Crossroad
                crossroad =
                crossroadFactory.createCrossroad(1, "barricaded", crossroad1Params, consequences);

        assertTrue(crossroad instanceof BarricadedCrossroad);
        assertEquals(consequences, crossroad.getConsequences());
        assertEquals(1, crossroad.getId());
        assertEquals(42, crossroad.getLocId());
        assertTrue(crossroad.hasLoc());
        assertEquals(0, crossroad.getNumTrash());
        assertEquals(CrossroadType.BARRICADED, crossroad.getType());
    }

    @Test
    void createFoodNegativeTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("changeFood", consequence2);

        assertEquals(1, consequences.size());
        assertTrue(consequences.get(0) instanceof FoodConsequence);

        final FoodConsequence foodConsequence = (FoodConsequence) consequences.get(0);
        assertEquals(-2, foodConsequence.getChangeFood());
    }

    @Test
    void createBarricadedNoLocTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("changeFood", consequence2);
        final CrossroadFactory<Crossroad> crossroadFactory = new CrossroadFactoryImpl();
        final Crossroad
                crossroad =
                crossroadFactory.createCrossroad(2, "barricaded", crossroad2Params, consequences);

        assertTrue(crossroad instanceof BarricadedNoLocCrossroad);
        assertEquals(consequences, crossroad.getConsequences());
        assertEquals(2, crossroad.getId());
        assertEquals(0, crossroad.getLocId());
        assertFalse(crossroad.hasLoc());
        assertEquals(0, crossroad.getNumTrash());
        assertEquals(CrossroadType.BARRICADED, crossroad.getType());
    }
}
