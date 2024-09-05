package sopra.systemtest;

import java.util.Set;
import sopra.comm.FoodChange;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class BarricadedFoodSearchedZombie extends SystemTest {
    public BarricadedFoodSearchedZombie() {
        super(BarricadedFoodSearchedZombie.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(BarricadedFoodSearchedZombie.class,
                "configuration2rounds.json");
    }

    @Override
    protected long createSeed() {
        return 23;
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
        this.assertCharacters(1, 1, 10, 29, 20);
        this.sendSelectCharacters(1, 10, 1);
        this.assertCharacterSpawned(1, 0, 1);
        this.assertCharacterSpawned(1, 0, 10);

        this.assertCardDrawn(1, 0, 1065);
        this.assertCardDrawn(1, 0, 1031);
        this.assertCardDrawn(1, 0, 1008);
        this.assertCardDrawn(1, 0, 1195);
        //swab
        this.assertCardDrawn(1, 0, 1126);

        this.assertNextRound(1, 2);

        this.assertCrisis(1, 33);

        this.assertDieRolled(1, 0, 1);
        this.assertDieRolled(1, 0, 1);
        this.assertDieRolled(1, 0, 5);

        //crossroad 33
        this.assertActNow(1);
        this.sendBarricade(1, 10, 1);
        this.assertBarricaded(1, 10, 42, 1);
        this.assertCrossroad(1, 33);
        this.assertFoodChanged(1, 2, FoodChange.CROSSROAD);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertColonyPhaseStarted(1);
        this.assertStarvationTokenAdded(1);
        this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
        this.assertMoralChanged(1, -2, MoralChange.CRISIS);
        this.assertZombieSpawned(1, 42, 0);
        this.assertZombieSpawned(1, 42, 1);
        this.assertZombieSpawned(1, 42, 2);


        this.assertNextRound(1, 1);

        this.assertCrisis(1, 11);

        this.assertDieRolled(1, 0, 3);
        this.assertDieRolled(1, 0, 3);
        this.assertDieRolled(1, 0, 5);

        //crossroad 30
        this.assertActNow(1);
        this.sendMove(1, 10, 1);
        this.assertMoved(1, 10, 1);
        this.assertActNow(1);
        this.sendSearch(1, 10);
        this.assertSearched(1, 10, 1);
        this.assertCardDrawn(1, 0, 1036);
        this.assertCrossroad(1, 30);
        this.assertZombieSpawned(1, 3, 0);
        this.assertZombieSpawned(1, 3, 1);
        this.assertZombieSpawned(1, 3, 0);
        this.assertZombieSpawned(1, 3, 1);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertColonyPhaseStarted(1);
        this.assertStarvationTokenAdded(1);
        this.assertMoralChanged(1, -2, MoralChange.STARVATION_TOKEN);
        this.assertMoralChanged(1, -2, MoralChange.CRISIS);

        this.assertGameEnd(1, false);
    }
}
