package sopra.systemtest;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class EndTurnButNotActivePlayerTest extends SystemTest {
  public EndTurnButNotActivePlayerTest() {
    super(EndTurnButNotActivePlayerTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(EndTurnButNotActivePlayerTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 56;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1, 2);
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    this.sendRegister(1, "Anna");
    this.assertConfig(1, createConfig());
    this.sendRegister(2, "Steve");
    this.assertConfig(2, createConfig());

    this.assertGameStarted(1);
    this.assertGameStarted(2);

    this.assertPlayer(1, 0, "Anna");
    this.assertPlayer(1, 1, "Steve");
    this.assertPlayer(2, 0, "Anna");
    this.assertPlayer(2, 1, "Steve");

    //player1 choose characters
    this.assertCharacters(1, 2008, 2000, 2005, 2001);

    this.sendSelectCharacters(1, 2000, 2001);
    for (int i = 1; i <= 2; i++) {
      this.assertCharacterSpawned(i, 0, 2000);
      this.assertCharacterSpawned(i, 0, 2001);

      this.assertCardDrawn(i, 0, 1014);
      this.assertCardDrawn(i, 0, 1024);
      this.assertCardDrawn(i, 0, 1030);
      this.assertCardDrawn(i, 0, 1020);
      this.assertCardDrawn(i, 0, 1013);
    }
    //player2 choose characters
    this.assertCharacters(2, 2008, 2011, 2004, 2010);
    this.sendSelectCharacters(2, 2011, 2008);

    for (int i = 1; i <= 2; i++) {
      this.assertCharacterSpawned(i, 1, 2008);
      this.assertCharacterSpawned(i, 1, 2011);

      this.assertCardDrawn(i, 1, 1040);
      this.assertCardDrawn(i, 1, 1003);
      this.assertCardDrawn(i, 1, 1002);
      this.assertCardDrawn(i, 1, 1022);
      this.assertCardDrawn(i, 1, 1004);
    }

    this.assertNextRound(1, 1);
    this.assertNextRound(2, 1);
    this.assertCrisis(1, 4001);
    this.assertCrisis(2, 4001);

    this.assertDieRolled(1, 0, 6);
    this.assertDieRolled(1, 0, 4);
    this.assertDieRolled(1, 0, 6);
    this.assertDieRolled(2, 0, 6);
    this.assertDieRolled(2, 0, 4);
    this.assertDieRolled(2, 0, 6);

    this.assertDieRolled(1, 1, 3);
    this.assertDieRolled(1, 1, 4);
    this.assertDieRolled(1, 1, 4);
    this.assertDieRolled(2, 1, 3);
    this.assertDieRolled(2, 1, 4);
    this.assertDieRolled(2, 1, 4);

    this.assertActNow(1);

    this.sendEndTurn(2);
    this.assertCommandFailed(2);

    this.sendEndTurn(1);

    this.assertActNow(2);
    this.sendEndTurn(2);

    this.assertColonyPhaseStarted(1);
    this.assertColonyPhaseStarted(2);

    this.assertStarvationTokenAdded(1);
    this.assertStarvationTokenAdded(2);

    this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
    this.assertMoralChanged(2, -1, MoralChange.STARVATION_TOKEN);
    this.assertMoralChanged(1, -2, MoralChange.CRISIS);
    this.assertMoralChanged(2, -2, MoralChange.CRISIS);

    this.assertZombieSpawned(1, 3000, 0);
    this.assertZombieSpawned(2, 3000, 0);
    this.assertZombieSpawned(1, 3000, 1);
    this.assertZombieSpawned(2, 3000, 1);
    this.assertZombieSpawned(1, 3000, 2);
    this.assertZombieSpawned(2, 3000, 2);

    this.assertGameEnd(1, true);
    this.assertGameEnd(2, true);
  }
}
