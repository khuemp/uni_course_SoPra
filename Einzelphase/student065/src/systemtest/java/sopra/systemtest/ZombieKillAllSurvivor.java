package sopra.systemtest;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class ZombieKillAllSurvivor extends SystemTest {
    public ZombieKillAllSurvivor() {
        super(ZombieKillAllSurvivor.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(ZombieKillAllSurvivor.class,
                "configKillAllSurvivor.json");
    }

    @Override
    protected long createSeed() {
        return 22;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        this.sendRegister(1, "Anna");
        this.assertConfig(1, createConfig());

        this.sendStartGame(1);
        this.assertGameStarted(1);

        this.assertPlayer(1, 0, "Anna");
        this.assertCharacters(1, 23, 3, 19, 30);
        this.sendSelectCharacters(1, 3, 23);
        this.assertCharacterSpawned(1, 0, 3);
        this.assertCharacterSpawned(1, 0, 23);

        this.assertCardDrawn(1, 0, 1120);
        //scissor
        this.assertCardDrawn(1, 0, 1000);
        //blueprint
        this.assertCardDrawn(1, 0, 1097);
        //lock
        this.assertCardDrawn(1, 0, 1019);
        this.assertCardDrawn(1, 0, 1030);

        this.assertNextRound(1, 2);

        this.assertCrisis(1, 21);

        this.assertDieRolled(1, 0, 2);
        this.assertDieRolled(1, 0, 2);
        this.assertDieRolled(1, 0, 2);

        //crossroad 34
        this.assertActNow(1);
        this.sendMove(1, 23, 2);
        this.assertMoved(1, 23, 2);
        this.assertWounded(1, 23);
        this.assertActNow(1);
        this.sendSearch(1, 23);
        this.assertSearched(1, 23, 2);
        this.assertCardDrawn(1, 0, 1023);
        this.assertCrossroad(1, 34);
        this.assertZombieSpawned(1, 42, 0);
        this.assertZombieSpawned(1, 1, 0);
        this.assertZombieSpawned(1, 2, 0);
        this.assertZombieSpawned(1, 3, 0);
        this.assertZombieSpawned(1, 4, 0);
        this.assertZombieSpawned(1, 5, 0);
        this.assertZombieSpawned(1, 6, 0);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertColonyPhaseStarted(1);
        this.assertStarvationTokenAdded(1);
        this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
        this.assertMoralChanged(1, -2, MoralChange.CRISIS);
        this.assertZombieSpawned(1, 42, 0);
        this.assertZombieSpawned(1, 2, 0);

        this.assertNextRound(1, 1);

        this.assertCrisis(1, 11);

        this.assertDieRolled(1, 0, 5);
        this.assertDieRolled(1, 0, 6);
        this.assertDieRolled(1, 0, 4);

        //crossroad 10
        this.assertActNow(1);
        this.sendMove(1, 23, 42);
        this.assertMoved(1, 23, 42);
        this.assertActNow(1);
        this.sendUseCard(1, 1097, 23, 1);
        this.assertCardUsed(1, 1097, 23, 1);
        this.assertWasteChanged(1, 1);
        this.assertCrossroad(1, 10);
        this.assertChildKilled(1);
        this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);
        this.assertZombieSpawned(1, 42, 1);
        this.assertZombieSpawned(1, 42, 2);
        this.assertSurvivorKilled(1, 23);
        this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);
        this.assertBarricadeDestroyed(1, 42, 1);
        this.assertZombieSpawned(1, 42, 2);
        this.assertSurvivorKilled(1, 3);
        this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);
        this.assertCharacterSpawned(1, 0, 22);

        this.assertColonyPhaseStarted(1);
        this.assertStarvationTokenAdded(1);
        this.assertMoralChanged(1, -2, MoralChange.STARVATION_TOKEN);
        this.assertMoralChanged(1, -2, MoralChange.CRISIS);

        this.assertSurvivorKilled(1, 22);
        this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);

        this.assertCharacterSpawned(1, 0, 28);

        this.assertGameEnd(1, true);
    }
}
