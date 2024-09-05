package sopra.systemtest.advanced;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class RollInfectionTest extends SystemTest {

  public RollInfectionTest() {
    super(RollInfectionTest.class, false);
  }

  @Override
  protected long createSeed() {
    return 420;
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
    this.assertCharacters(1, 2007, 2002, 2003, 2000);
    this.sendSelectCharacters(1, 2000, 2007);
    this.assertCharacterSpawned(1, 0, 2000);
    this.assertCharacterSpawned(1, 0, 2007);


    //draw cards
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);


    this.assertNextRound(1, 1);
    //crisis
    this.assertEvent(1);


    //roll dice
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);

    //todo seed finden um bitten zu treffen um chain testen zu können
    this.assertActNow(1);
    this.sendMove(1, 2000, 3001);
    this.assertMoved(1, 2000, 3001);
    this.assertWounded(1, 2000);
    this.assertActNow(1);

    this.sendMove(1, 2007, 3001);
    this.assertMoved(1, 2007, 3001);
    this.assertBitten(1, 2007);
    this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);

    this.assertBitten(1, 2000);
    this.assertMoralChanged(1, -1, MoralChange.CHARACTER_DIED);
    this.assertCharacterSpawned(1, 0, 2003);

    this.assertActNow(1);
    this.sendEndTurn(1);

    this.assertColonyPhaseStarted(1);
    this.assertStarvationTokenAdded(1);
    this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
    this.assertMoralChanged(1, -2, MoralChange.CRISIS);
    this.assertZombieSpawned(1, 3000, 0);

    this.assertGameEnd(1, true);
  }
}