package sopra.systemtest;

import java.util.Set;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class AllPlayersLeftTest extends SystemTest {

  public AllPlayersLeftTest() {
    super(AllPlayersLeftTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(AllPlayersLeftTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 11;
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
    //broadcast player1
    this.assertCharacters(1, 2006, 2002, 2004, 2005);

    this.sendSelectCharacters(1, 2006, 2002);
    this.assertCharacterSpawned(1, 0, 2002);
    this.assertCharacterSpawned(1, 0, 2006);

    this.assertCardDrawn(1, 0, 1041);
    this.assertCardDrawn(1, 0, 1011);
    this.assertCardDrawn(1, 0, 1003);
    this.assertCardDrawn(1, 0, 1002);
    this.assertCardDrawn(1, 0, 1031);
    //broadcast player2
    this.assertCharacterSpawned(2, 0, 2002);
    this.assertCharacterSpawned(2, 0, 2006);

    this.assertCardDrawn(2, 0, 1041);
    this.assertCardDrawn(2, 0, 1011);
    this.assertCardDrawn(2, 0, 1003);
    this.assertCardDrawn(2, 0, 1002);
    this.assertCardDrawn(2, 0, 1031);
    //
    //player2 choose characters
    this.assertCharacters(2, 2000, 2011, 2010, 2003);
    this.sendSelectCharacters(2, 2011, 2000);
    //broadcast player1
    this.assertCharacterSpawned(1, 1, 2000);
    this.assertCharacterSpawned(1, 1, 2011);

    this.assertCardDrawn(1, 1, 1022);
    this.assertCardDrawn(1, 1, 1034);
    this.assertCardDrawn(1, 1, 1001);
    this.assertCardDrawn(1, 1, 1014);
    this.assertCardDrawn(1, 1, 1044);
    //broadcast player2
    this.assertCharacterSpawned(2, 1, 2000);
    this.assertCharacterSpawned(2, 1, 2011);

    this.assertCardDrawn(2, 1, 1022);
    this.assertCardDrawn(2, 1, 1034);
    this.assertCardDrawn(2, 1, 1001);
    this.assertCardDrawn(2, 1, 1014);
    this.assertCardDrawn(2, 1, 1044);
    //
    this.assertNextRound(1, 1);
    this.assertCrisis(1, 4006);
    this.assertNextRound(2, 1);
    this.assertCrisis(2, 4006);

    this.assertDieRolled(1, 0, 4);
    this.assertDieRolled(1, 0, 5);
    this.assertDieRolled(1, 0, 4);
    this.assertDieRolled(2, 0, 4);
    this.assertDieRolled(2, 0, 5);
    this.assertDieRolled(2, 0, 4);

    this.assertDieRolled(1, 1, 1);
    this.assertDieRolled(1, 1, 3);
    this.assertDieRolled(1, 1, 6);
    this.assertDieRolled(2, 1, 1);
    this.assertDieRolled(2, 1, 3);
    this.assertDieRolled(2, 1, 6);

    this.assertActNow(1);
    this.sendLeave(1);

    this.assertLeft(1, 0);
    this.assertLeft(2, 0);
    this.assertSurvivorKilled(2, 2002);
    this.assertMoralChanged(2, -1, MoralChange.CHARACTER_DIED);
    this.assertSurvivorKilled(2, 2006);
    this.assertMoralChanged(2, -1, MoralChange.CHARACTER_DIED);

    this.assertActNow(2);
    this.sendLeave(2);

    this.assertLeft(2, 1);
  }
}
