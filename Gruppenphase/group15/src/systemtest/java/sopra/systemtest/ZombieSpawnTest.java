package sopra.systemtest;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class ZombieSpawnTest extends SystemTest {
  public ZombieSpawnTest() {
    super(ZombieSpawnTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(ZombieSpawnTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 32;
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
    this.assertCharacters(1, 2005, 2011, 2006, 2007);

    this.sendSelectCharacters(1, 2005, 2011);
    this.assertCharacterSpawned(1, 0, 2005);
    this.assertCharacterSpawned(1, 0, 2011);


    /* this.assertCardDrawn(1, 0, 1023);
    this.assertCardDrawn(1, 0, 1033);
    this.assertCardDrawn(1, 0, 1032);
    this.assertCardDrawn(1, 0, 1010);
    this.assertCardDrawn(1, 0, 1014);*/

    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);


    this.assertNextRound(1, 1);
    this.assertCrisis(1, 4005);

    /*
    this.assertDieRolled(1, 0, 4);
    this.assertDieRolled(1, 0, 5);

    this.assertDieRolled(1, 0, 3);

    this.assertDieRolled(1, 0, 3);*/

    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);


    this.assertActNow(1);
    this.sendMove(1, 2005, 3001);
    this.assertMoved(1, 2005, 3001);
    this.assertBitten(1, 2005);
    this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);

    this.assertActNow(1);
    this.sendEndTurn(1);

    this.assertColonyPhaseStarted(1);

    this.assertStarvationTokenAdded(1);
    this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
    this.assertMoralChanged(1, -2, MoralChange.CRISIS);

    this.assertZombieSpawned(1, 3000, 0);
    this.assertZombieSpawned(1, 3001, 0);

    this.assertGameEnd(1, true);

  }
}
