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
import de.unisaarland.cs.se.sopra.crossroad.EquipCrossroad;
import de.unisaarland.cs.se.sopra.crossroad.MoralConsequence;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquipMoralTest {

    final JSONObject crossroad = new JSONObject();
    final JSONObject equip = new JSONObject();
    final JSONObject consequence = new JSONObject();
    final JSONObject changeMoral = new JSONObject();
    final ParamMap crossroad1Params = new JSONParser.JSONParaMap(equip);

    @BeforeEach
    void createJsonFile() {
        changeMoral.put("amount", 2);
        consequence.put("changeMoral", changeMoral);
        equip.put("identifier", 1);
        equip.put("consequence", consequence);
        crossroad.put("equip", equip);
    }

    @Test
    void createMoralTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("changeMoral", consequence);

        assertEquals(1, consequences.size());
        assertTrue(consequences.get(0) instanceof MoralConsequence);

        final MoralConsequence moralConsequence = (MoralConsequence) consequences.get(0);
        assertEquals(2, moralConsequence.getChangeMoral());
    }

    @Test
    void createEquipTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("changeMoral", consequence);
        final CrossroadFactory<Crossroad> crossroadFactory = new CrossroadFactoryImpl();
        final Crossroad
                crossroad =
                crossroadFactory.createCrossroad(1, "equip", crossroad1Params, consequences);

        assertTrue(crossroad instanceof EquipCrossroad);
        assertEquals(consequences, crossroad.getConsequences());
        assertEquals(1, crossroad.getId());
        assertEquals(0, crossroad.getLocId());
        assertFalse(crossroad.hasLoc());
        assertEquals(0, crossroad.getNumTrash());
        assertEquals(CrossroadType.EQUIP, crossroad.getType());
    }
}
