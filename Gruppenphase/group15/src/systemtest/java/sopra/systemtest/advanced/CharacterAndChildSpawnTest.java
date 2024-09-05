package sopra.systemtest.advanced;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class CharacterAndChildSpawnTest extends SystemTest {
  public CharacterAndChildSpawnTest() {
    super(CharacterAndChildSpawnTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(CharacterAndChildSpawnTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 15;
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
    this.assertCharacters(1, 2004, 2001, 2005, 2009);

    this.sendSelectCharacters(1, 2004, 2001);
    this.assertCharacterSpawned(1, 0, 2001);
    this.assertCharacterSpawned(1, 0, 2004);

    // card drawn
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);

    this.assertNextRound(1, 1);
    this.assertEvent(1);

    this.assertEvent(1);
    this.assertEvent(1);
    this.assertEvent(1);


    //move to other location not colony
    //wrong character
    this.assertActNow(1);
    this.sendMove(1, 2001, 3002);
    this.assertCommandFailed(1);

    this.assertActNow(1);
    this.sendMove(1, 2001, 3001);
    this.assertMoved(1, 2001, 3001);
    //start searching
    //wrong character
    this.assertActNow(1);
    this.sendSearch(1, 2013);
    this.assertCommandFailed(1);

    this.assertActNow(1);
    this.sendSearch(1, 2001);
    // currently nullpointer exception
    this.assertSearched(1, 2001, 3001);

    this.assertCharacterSpawned(1, 0, 2003);
    this.assertChildSpawned(1);

    this.assertActNow(1);
    this.sendEndTurn(1);

    this.assertColonyPhaseStarted(1);

    this.assertStarvationTokenAdded(1);
    this.assertMoralChanged(1, -1, MoralChange.STARVATION_TOKEN);
    this.assertMoralChanged(1, -2, MoralChange.CRISIS);
    this.assertZombieSpawned(1, 3000, 0);
    this.assertZombieSpawned(1, 3000, 1);
    this.assertZombieSpawned(1, 3001, 0);


    this.assertGameEnd(1, true);
  }
}
