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
import de.unisaarland.cs.se.sopra.crossroad.MovedCrossroad;
import de.unisaarland.cs.se.sopra.crossroad.MovedNoLocCrossroad;
import de.unisaarland.cs.se.sopra.crossroad.ZombieConsequence;
import de.unisaarland.cs.se.sopra.crossroad.ZombieNoLocConsequence;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoveZombieTest {

    final JSONObject crossroad1 = new JSONObject();
    final JSONObject moved = new JSONObject();
    final JSONObject consequence1 = new JSONObject();
    final JSONObject spawnZombie = new JSONObject();
    final ParamMap crossroad1Params = new JSONParser.JSONParaMap(moved);

    final JSONObject crossroad2 = new JSONObject();
    final JSONObject movedNoLoc = new JSONObject();
    final JSONObject consequence2 = new JSONObject();
    final JSONObject spawnZombieNoLoc = new JSONObject();
    final ParamMap crossroad2Params = new JSONParser.JSONParaMap(movedNoLoc);

    @BeforeEach
    void createJsonFile() {
        spawnZombie.put("amount", 0);
        spawnZombie.put("locationId", 23);
        consequence1.put("spawnZombies", spawnZombie);
        moved.put("identifier", 1);
        moved.put("locationId", 23);
        moved.put("consequence", consequence1);
        crossroad1.put("moved", moved);

        spawnZombieNoLoc.put("amount", -4);
        consequence2.put("spawnZombies", spawnZombieNoLoc);
        movedNoLoc.put("identifier", 2);
        movedNoLoc.put("consequence", consequence1);
        crossroad2.put("moved", moved);
    }

    @Test
    void createZombieTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("spawnZombies", consequence1);

        assertEquals(1, consequences.size());
        assertTrue(consequences.get(0) instanceof ZombieConsequence);

        final ZombieConsequence zombieConsequence = (ZombieConsequence) consequences.get(0);
        assertEquals(0, zombieConsequence.getSpawnZombies());
        assertEquals(23, zombieConsequence.getLocId());
    }

    @Test
    void createMovedTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("spawnZombies", consequence1);
        final CrossroadFactory<Crossroad> crossroadFactory = new CrossroadFactoryImpl();
        final Crossroad
                crossroad =
                crossroadFactory.createCrossroad(1, "moved", crossroad1Params, consequences);

        assertTrue(crossroad instanceof MovedCrossroad);
        assertEquals(consequences, crossroad.getConsequences());
        assertEquals(1, crossroad.getId());
        assertEquals(23, crossroad.getLocId());
        assertTrue(crossroad.hasLoc());
        assertEquals(0, crossroad.getNumTrash());
        assertEquals(CrossroadType.MOVED, crossroad.getType());
    }

    @Test
    void createZombieNoLocTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("spawnZombies", consequence2);

        assertEquals(1, consequences.size());
        assertTrue(consequences.get(0) instanceof ZombieNoLocConsequence);

        final ZombieNoLocConsequence zombieNoLocConsequence =
                (ZombieNoLocConsequence) consequences.get(0);
        assertEquals(-4, zombieNoLocConsequence.getSpawnZombies());
    }

    @Test
    void createMovedNoLocTest() {
        final ConsequenceFactory<Consequence> consequenceFactory = new ConsequenceFactoryImpl();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence("spawnZombies", consequence2);
        final CrossroadFactory<Crossroad> crossroadFactory = new CrossroadFactoryImpl();
        final Crossroad
                crossroad =
                crossroadFactory.createCrossroad(2, "moved", crossroad2Params, consequences);

        assertTrue(crossroad instanceof MovedNoLocCrossroad);
        assertEquals(consequences, crossroad.getConsequences());
        assertEquals(2, crossroad.getId());
        assertEquals(0, crossroad.getLocId());
        assertFalse(crossroad.hasLoc());
        assertEquals(0, crossroad.getNumTrash());
        assertEquals(CrossroadType.MOVED, crossroad.getType());
    }
}
