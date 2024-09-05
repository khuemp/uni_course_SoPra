package sopra.systemtest.advanced;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class RegisterTwiceTest extends SystemTest {
  public RegisterTwiceTest() {
    super(RegisterTwiceTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(BarricadeWinTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 40;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1);
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    this.sendRegister(1, "Anna");
    this.assertConfig(1, createConfig());
    this.sendRegister(1, "Jeff");
    this.assertCommandFailed(1);

    this.sendStartGame(1);
    this.assertGameStarted(1);
    this.assertPlayer(1, 0, "Anna");
    this.assertCharacters(1, 2002, 2003, 2000, 2004);
    this.sendSelectCharacters(1, 2000, 2003);
    this.assertCharacterSpawned(1, 0, 2000);
    this.assertCharacterSpawned(1, 0, 2003);
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);
    //
    this.assertNextRound(1, 1);
    this.assertEvent(1);

    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);

    this.assertActNow(1);
    this.sendEndTurn(1);
    this.assertColonyPhaseStarted(1);
    this.assertStarvationTokenAdded(1);
    this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
    this.assertMoralChanged(1, -2, MoralChange.CRISIS);
    this.assertZombieSpawned(1, 3000, 0);
    this.assertZombieSpawned(1, 3000, 1);

    this.assertGameEnd(1, true);
  }
}
