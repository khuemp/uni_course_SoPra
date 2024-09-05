package sopra.systemtest.advanced;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class HealAbilityNoWoundTest extends SystemTest {

  public HealAbilityNoWoundTest() {
    super(HealAbilityNoWoundTest.class, false);
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1);
  }

  @Override
  public String createConfig() {
    return Utils.loadResource(BarricadeWinTest.class, "oneOfEachCfg.json");
  }

  @Override
  protected long createSeed() {
    return 40;
  }

  @Override
  public void run() throws TimeoutException {
    this.sendRegister(1, "Jeff");
    this.assertConfig(1, createConfig());
    this.sendStartGame(1);
    this.assertGameStarted(1);
    this.assertPlayer(1, 0, "Jeff");
    this.assertCharacters(1, 2003, 2004, 2017, 2000);
    this.sendSelectCharacters(1, 2000, 2003);
    this.assertCharacterSpawned(1, 0, 2000);
    this.assertCharacterSpawned(1, 0, 2003);
    this.assertCardDrawn(1, 0, 2244);
    this.assertCardDrawn(1, 0, 1131);
    this.assertCardDrawn(1, 0, 1058);
    this.assertCardDrawn(1, 0, 1249);
    this.assertCardDrawn(1, 0, 1106);
    //
    this.assertNextRound(1, 1);
    this.assertCrisis(1, 4032);
    this.assertDieRolled(1, 0, 6);
    this.assertDieRolled(1, 0, 6);
    this.assertDieRolled(1, 0, 3);
    this.assertActNow(1);
    this.sendUseAbility(1, 2000, 2003);
    this.assertCommandFailed(1);
    this.assertActNow(1);
    this.sendEndTurn(1);
    this.assertColonyPhaseStarted(1);
    this.assertStarvationTokenAdded(1);
    this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
    this.assertMoralChanged(1, -2, MoralChange.CRISIS);
    this.assertZombieSpawned(1, 3000, 0);
    this.assertZombieSpawned(1, 3000, 1);
    this.assertZombieSpawned(1, 3000, 2);

    this.assertGameEnd(1, true);
  }
}



