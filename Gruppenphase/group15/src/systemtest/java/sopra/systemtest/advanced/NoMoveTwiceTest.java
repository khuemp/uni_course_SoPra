package sopra.systemtest.advanced;

import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.BaseTest;

public class NoMoveTwiceTest extends BaseTest {


  @Override
  public void run() throws TimeoutException {
    super.run();
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
    this.sendMove(1, 2004, 3000);
    this.assertCommandFailed(1);
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