package sopra.systemtest.advanced;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class TooManyBarricadeAndNoMoreActivationTest extends SystemTest {
  public TooManyBarricadeAndNoMoreActivationTest() {
    super(TooManyBarricadeAndNoMoreActivationTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(TooManyBarricadeAndNoMoreActivationTest.class, "oneOfEachCfg.json");
  }

  @Override
  protected long createSeed() {
    return 41;
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
    this.assertCharacters(1, 2011, 2005, 2013, 2006);

    this.sendSelectCharacters(1, 2006, 2013);
    this.assertCharacterSpawned(1, 0, 2006);
    this.assertCharacterSpawned(1, 0, 2013);

    this.assertCardDrawn(1, 0, 1368);
    this.assertCardDrawn(1, 0, 2634);
    this.assertCardDrawn(1, 0, 2285);
    this.assertCardDrawn(1, 0, 2474);
    this.assertCardDrawn(1, 0, 1039);

    this.assertNextRound(1, 1);
    this.assertCrisis(1, 4015);

    this.assertDieRolled(1, 0, 3);
    this.assertDieRolled(1, 0, 5);
    this.assertDieRolled(1, 0, 2);

    //pick wrong character
    this.assertActNow(1);
    this.sendUseAbility(1, 2000, 1);
    this.assertCommandFailed(1);

    this.assertActNow(1);
    this.sendUseAbility(1, 2006, 1);
    this.assertAbilityUsed(1, 2006, 1);

    //run out of activations
    this.assertActNow(1);
    this.sendUseAbility(1, 2006, 1);
    this.assertCommandFailed(1);

    //too many barricades
    this.assertActNow(1);
    this.sendUseAbility(1, 2013, 1);
    this.assertCommandFailed(1);

    this.assertActNow(1);
    this.sendEndTurn(1);

    this.assertColonyPhaseStarted(1);

    this.assertStarvationTokenAdded(1);
    this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
    this.assertMoralChanged(1, -2, MoralChange.CRISIS);
    this.assertZombieSpawned(1, 3000, 0);
    this.assertBarricadeDestroyed(1, 3000, 1);
    this.assertZombieSpawned(1, 3000, 2);

    this.assertGameEnd(1, true);
  }
}
