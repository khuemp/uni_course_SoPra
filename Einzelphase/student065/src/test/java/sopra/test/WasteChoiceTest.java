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
import de.unisaarland.cs.se.sopra.crossroad.FoodConsequence;
import de.unisaarland.cs.se.sopra.crossroad.MoralConsequence;
import de.unisaarland.cs.se.sopra.crossroad.WasteChangedCrossroad;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WasteChoiceTest {

    final JSONObject crossroad = new JSONObject();
    final JSONObject wasteChanged = new JSONObject();
    final JSONObject consequence = new JSONObject();
    final JSONObject choice = new JSONObject();
    final JSONObject subConsequence1 = new JSONObject();
    final JSONObject subConsequence2 = new JSONObject();
    final JSONObject changeMoral = new JSONObject();
    final JSONObject changeFood = new JSONObject();
    final ParamMap crossroadParams = new JSONParser.JSONParaMap(wasteChanged);

    @BeforeEach
    void createJsonFile() {
        changeMoral.put("amount", 2);
        subConsequence1.put("changeMoral", changeMoral);
        changeFood.put("amount", -2);
        subConsequence2.put("changeFood", changeFood);
        choice.put("consequence1", subConsequence1);
        choice.put("consequence2", subConsequence2);
        consequence.put("choice", choice);
        wasteChanged.put("identifier", 1);
        wasteChanged.put("amount", 4);
        wasteChanged.put("consequence", consequence);
        crossroad.put("wasteChanged", wasteChanged);
    }

    @Test
    void createMoralTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("choice", consequence);

        assertEquals(2, consequences.size());
        assertTrue(consequences.get(0) instanceof MoralConsequence);
        assertTrue(consequences.get(1) instanceof FoodConsequence);

        final MoralConsequence moralConsequence = (MoralConsequence) consequences.get(0);
        final FoodConsequence foodConsequence = (FoodConsequence) consequences.get(1);
        assertEquals(2, moralConsequence.getChangeMoral());
        assertEquals(-2, foodConsequence.getChangeFood());
    }

    @Test
    void createEquipTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("choice", consequence);
        final CrossroadFactory<Crossroad> crossroadFactory = new CrossroadFactoryImpl();
        final Crossroad
                crossroad =
                crossroadFactory.createCrossroad(1, "wasteChanged", crossroadParams, consequences);

        assertTrue(crossroad instanceof WasteChangedCrossroad);
        assertEquals(consequences, crossroad.getConsequences());
        assertEquals(1, crossroad.getId());
        assertEquals(0, crossroad.getLocId());
        assertFalse(crossroad.hasLoc());
        assertEquals(4, crossroad.getNumTrash());
        assertEquals(CrossroadType.WASTECHANGED, crossroad.getType());
    }
}
