package sopra.systemtest;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class AllPlayersLeft extends SystemTest {
    public AllPlayersLeft() {
        super(AllPlayersLeft.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(AllPlayersLeft.class,
                "configKillSurvivor.json");
    }

    @Override
    protected long createSeed() {
        return 11;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        this.sendRegister(1, "Anna");
        this.assertConfig(1, createConfig());
        this.sendRegister(2, "Sven");
        this.assertConfig(2, createConfig());

        this.sendStartGame(1);
        this.assertGameStarted(1);
        this.assertGameStarted(2);

        this.assertPlayer(1, 0, "Anna");
        this.assertPlayer(1, 1, "Sven");
        this.assertPlayer(2, 0, "Anna");
        this.assertPlayer(2, 1, "Sven");

        this.assertCharacters(1, 15, 2, 14, 8);
        this.sendSelectCharacters(1, 2, 15);
        this.assertCharacterSpawned(1, 0, 2);
        this.assertCharacterSpawned(1, 0, 15);

        this.assertCardDrawn(1, 0, 1000);
        this.assertCardDrawn(1, 0, 1132);
        this.assertCardDrawn(1, 0, 1126);
        this.assertCardDrawn(1, 0, 1020);
        this.assertCardDrawn(1, 0, 1028);

        this.assertCharacterSpawned(2, 0, 2);
        this.assertCharacterSpawned(2, 0, 15);

        this.assertCardDrawn(2, 0, 1000);
        this.assertCardDrawn(2, 0, 1132);
        this.assertCardDrawn(2, 0, 1126);
        this.assertCardDrawn(2, 0, 1020);
        this.assertCardDrawn(2, 0, 1028);

        this.assertCharacters(2, 14, 13, 24, 30);
        this.sendSelectCharacters(2, 14, 13);
        this.assertCharacterSpawned(1, 1, 13);
        this.assertCharacterSpawned(1, 1, 14);

        this.assertCardDrawn(1, 1, 1029);
        this.assertCardDrawn(1, 1, 1064);
        this.assertCardDrawn(1, 1, 1129);
        this.assertCardDrawn(1, 1, 1065);
        this.assertCardDrawn(1, 1, 1066);

        this.assertCharacterSpawned(2, 1, 13);
        this.assertCharacterSpawned(2, 1, 14);

        this.assertCardDrawn(2, 1, 1029);
        this.assertCardDrawn(2, 1, 1064);
        this.assertCardDrawn(2, 1, 1129);
        this.assertCardDrawn(2, 1, 1065);
        this.assertCardDrawn(2, 1, 1066);

        this.assertNextRound(1, 2);
        this.assertCrisis(1, 34);
        this.assertNextRound(2, 2);
        this.assertCrisis(2, 34);

        this.assertDieRolled(1, 0, 5);
        this.assertDieRolled(1, 0, 3);
        this.assertDieRolled(1, 0, 2);
        this.assertDieRolled(2, 0, 5);
        this.assertDieRolled(2, 0, 3);
        this.assertDieRolled(2, 0, 2);

        this.assertDieRolled(1, 1, 2);
        this.assertDieRolled(1, 1, 1);
        this.assertDieRolled(1, 1, 5);
        this.assertDieRolled(2, 1, 2);
        this.assertDieRolled(2, 1, 1);
        this.assertDieRolled(2, 1, 5);

        this.assertActNow(1);
        this.sendLeave(1);
        this.assertLeft(1, 0);
        this.assertLeft(2, 0);
        this.assertSurvivorKilled(2, 2);
        this.assertMoralChanged(2, -1, MoralChange.CHARACTER_DIED);
        this.assertSurvivorKilled(2, 15);
        this.assertMoralChanged(2, -1, MoralChange.CHARACTER_DIED);

        this.assertActNow(2);
        this.sendLeave(2);
        this.assertLeft(2, 1);
    }
}
