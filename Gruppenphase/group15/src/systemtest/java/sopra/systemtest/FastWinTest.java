package sopra.systemtest;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class FastWinTest extends SystemTest {
  public FastWinTest() {
    super(FastWinTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(FastWinTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 20;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1);
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    this.sendRegister(1, "Jeff");
    this.assertConfig(1, createConfig());
    this.sendStartGame(1);
    this.assertGameStarted(1);
    this.assertPlayer(1, 0, "Jeff");
    this.assertCharacters(1, 2002, 2003, 2011, 2007);
    this.sendSelectCharacters(1, 2002, 2003);
    //this.assertActNow(1);
    this.assertCharacterSpawned(1, 0, 2002);
    this.assertCharacterSpawned(1, 0, 2003);
    this.assertCardDrawn(1, 0, 1041);
    this.assertCardDrawn(1, 0, 1000);
    this.assertCardDrawn(1, 0, 1014);
    this.assertCardDrawn(1, 0, 1011);
    this.assertCardDrawn(1, 0, 1001);
    this.assertNextRound(1, 1);
    this.assertCrisis(1, 4002);
    this.assertDieRolled(1, 0, 4);
    this.assertDieRolled(1, 0, 4);
    this.assertDieRolled(1, 0, 1);
    this.assertActNow(1);
    this.sendMove(1, 2002, 3001);
    this.assertMoved(1, 2002, 3001);
    this.assertDieRolled(1, 0, 0);
    this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);
    this.assertActNow(1);
  }
}
