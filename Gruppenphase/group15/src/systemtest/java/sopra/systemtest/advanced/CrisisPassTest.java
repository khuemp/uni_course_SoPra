package sopra.systemtest.advanced;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class CrisisPassTest extends SystemTest {

  public CrisisPassTest() {
    super(CrisisPassTest.class, false);
  }

  @Override
  protected long createSeed() {
    return 0;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1);
  }

  @Override
  public String createConfig() {
    return Utils.loadResource(BarricadeWinTest.class, "basicConfigEasyCrisis.json");
  }

  @Override
  public void run() throws TimeoutException {
    this.sendRegister(1, "Jeff");
    this.assertConfig(1, createConfig());
    this.sendStartGame(1);
    this.assertGameStarted(1);
    this.assertPlayer(1, 0, "Jeff");
    this.assertCharacters(1, 2002, 2000, 2006, 2004);
    this.sendSelectCharacters(1, 2004, 2006);
    this.assertCharacterSpawned(1, 0, 2004);
    this.assertCharacterSpawned(1, 0, 2006);
    this.assertCardDrawn(1, 0, 1030);
    this.assertCardDrawn(1, 0, 1012);
    this.assertCardDrawn(1, 0, 1033);
    this.assertCardDrawn(1, 0, 1031);
    this.assertCardDrawn(1, 0, 1032);
    this.assertNextRound(1, 1);
    this.assertCrisis(1, 4007);
    this.assertDieRolled(1, 0, 5);
    this.assertDieRolled(1, 0, 2);
    this.assertDieRolled(1, 0, 4);
    this.assertActNow(1);
    this.sendMove(1, 2004, 3001);
    this.assertMoved(1, 2004, 3001);
    this.assertFrostbitten(1, 2004);
    this.assertActNow(1);
    this.sendContributeCard(1, 1030);
    this.assertContributed(1, 0, 1030);
    this.assertActNow(1);
    this.sendEndTurn(1);
    this.assertColonyPhaseStarted(1);
    this.assertStarvationTokenAdded(1);
    this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
    this.assertZombieSpawned(1, 3000, 0);
    this.assertZombieSpawned(1, 3001, 0);


    this.assertGameEnd(1, true);
  }
}