package sopra.systemtest;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class VoteZombieSpawnKillSurvivor extends SystemTest {
    public VoteZombieSpawnKillSurvivor() {
        super(VoteZombieSpawnKillSurvivor.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(VoteZombieSpawnKillSurvivor.class,
                "configKillSurvivor.json");
    }

    @Override
    protected long createSeed() {
        return 11;
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
        this.assertCharacters(1, 15, 2, 14, 8);
        this.sendSelectCharacters(1, 2, 15);
        this.assertCharacterSpawned(1, 0, 2);
        this.assertCharacterSpawned(1, 0, 15);

        this.assertCardDrawn(1, 0, 1000);
        this.assertCardDrawn(1, 0, 1132);
        this.assertCardDrawn(1, 0, 1126);
        this.assertCardDrawn(1, 0, 1020);
        this.assertCardDrawn(1, 0, 1028);

        this.assertNextRound(1, 2);

        this.assertCrisis(1, 34);

        this.assertDieRolled(1, 0, 2);
        this.assertDieRolled(1, 0, 2);
        this.assertDieRolled(1, 0, 6);

        //crossroad 12
        this.assertActNow(1);
        this.sendUseCard(1, 1132);
        this.assertCardUsed(1, 1132);
        this.assertWasteChanged(1, 1);
        this.assertDieRolled(1, 0, 1);

        this.assertActNow(1);
        this.sendUseCard(1, 1126);
        this.assertCardUsed(1, 1126);
        this.assertWasteChanged(1, 1);
        this.assertDieRolled(1, 0, 1);

        this.assertCrossroad(1, 12);
        this.assertVoteNow(1);
        this.sendVote(1, false);
        this.assertVoteResult(1, false);
        this.assertZombieSpawned(1, 42, 0);
        this.assertZombieSpawned(1, 42, 1);
        this.assertZombieSpawned(1, 42, 0);
        this.assertZombieSpawned(1, 42, 1);
        this.assertChildKilled(1);
        this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);
        this.assertSurvivorKilled(1, 15);
        this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertColonyPhaseStarted(1);
        this.assertStarvationTokenAdded(1);
        this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
        this.assertMoralChanged(1, -2, MoralChange.CRISIS);
        this.assertSurvivorKilled(1, 2);
        this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);
        this.assertCharacterSpawned(1, 0, 14);


        this.assertNextRound(1, 1);

        this.assertCrisis(1, 31);

        this.assertDieRolled(1, 0, 3);
        this.assertDieRolled(1, 0, 2);

        //crossroad 29
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertColonyPhaseStarted(1);
        this.assertStarvationTokenAdded(1);
        this.assertMoralChanged(1, -2, MoralChange.STARVATION_TOKEN);
        this.assertMoralChanged(1, -2, MoralChange.CRISIS);
        this.assertSurvivorKilled(1, 14);
        this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);
        this.assertCharacterSpawned(1, 0, 13);

        this.assertGameEnd(1, true);
    }
}
