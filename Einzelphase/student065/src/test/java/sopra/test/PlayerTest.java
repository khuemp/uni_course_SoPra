package sopra.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.sopra.crossroad.BarricadedCrossroad;
import de.unisaarland.cs.se.sopra.crossroad.Consequence;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadType;
import de.unisaarland.cs.se.sopra.crossroad.MoralConsequence;
import de.unisaarland.cs.se.sopra.model.Player;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void checkInitialPlayer() {
        final Player player = new Player(1, "Anna");
        assertFalse(player.getCrossroadActivate());
        assertNull(player.getCrossroad());
        assertEquals(0, player.getNumTrashTrigger());
        assertFalse(player.getCrossroadActivate());
    }

    @Test
    void addCrossroadToPlayer() {
        final List<Consequence> consequences = new ArrayList<>();
        final Consequence subCons1 = new MoralConsequence(-1);
        consequences.add(subCons1);
        final Crossroad crossroad =
                new BarricadedCrossroad(0, CrossroadType.BARRICADED, consequences, 42);
        final Player player = new Player(1, "Anna");
        player.setCrossroad(crossroad);
        assertNotNull(player.getCrossroad());
    }

    @Test
    void checkInterrupt() {
        final Player player = new Player(1, "Anna");
        player.setCrossroadActivate(true);
        player.setNumTrashTrigger(2);
        player.setHasVoted(true);
        assertTrue(player.getCrossroadActivate());
        assertEquals(2, player.getNumTrashTrigger());
        assertTrue(player.getCrossroadActivate());
    }
}
