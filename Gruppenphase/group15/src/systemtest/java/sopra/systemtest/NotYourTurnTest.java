package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class NotYourTurnTest extends SystemTest {
  public NotYourTurnTest() {
    super(NotYourTurnTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(NotYourTurnTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 20;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1, 2);
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    this.sendRegister(1, "Jeff");
    this.assertConfig(1, createConfig());
    this.sendRegister(2, "Dieter");
    this.assertConfig(2, createConfig());
    this.assertGameStarted(1);
    this.assertGameStarted(2);
    this.assertPlayer(1, 0, "Jeff");
    this.assertPlayer(2, 0, "Jeff");
    this.assertPlayer(1, 1, "Dieter");
    this.assertPlayer(2, 1, "Dieter");
    this.assertCharacters(1, 2010, 2006, 2007, 2003);
    this.sendSelectCharacters(1, 2010, 2003);
    this.assertCharacterSpawned(1, 0, 2003);
    this.assertCharacterSpawned(2, 0, 2003);
    this.assertCharacterSpawned(1, 0, 2010);
    this.assertCharacterSpawned(2, 0, 2010);
    this.assertCardDrawn(1, 0, 1022);
    this.assertCardDrawn(2, 0, 1022);
    this.assertCardDrawn(1, 0, 1001);
    this.assertCardDrawn(2, 0, 1001);
    this.assertCardDrawn(1, 0, 1012);
    this.assertCardDrawn(2, 0, 1012);
    this.assertCardDrawn(1, 0, 1013);
    this.assertCardDrawn(2, 0, 1013);
    this.assertCardDrawn(1, 0, 1034);
    this.assertCardDrawn(2, 0, 1034);
    this.assertCharacters(2, 2011, 2000, 2007, 2004);
    this.sendSelectCharacters(2, 2000, 2011);
    this.assertCharacterSpawned(1, 1, 2000);
    this.assertCharacterSpawned(2, 1, 2000);
    this.assertCharacterSpawned(1, 1, 2011);
    this.assertCharacterSpawned(2, 1, 2011);
    this.assertCardDrawn(1, 1, 1000);
    this.assertCardDrawn(2, 1, 1000);
    this.assertCardDrawn(1, 1, 1023);
    this.assertCardDrawn(2, 1, 1023);
    this.assertCardDrawn(1, 1, 1002);
    this.assertCardDrawn(2, 1, 1002);
    this.assertCardDrawn(1, 1, 1014);
    this.assertCardDrawn(2, 1, 1014);
    this.assertCardDrawn(1, 1, 1033);
    this.assertCardDrawn(2, 1, 1033);
    this.assertNextRound(1, 1);
    this.assertNextRound(2, 1);
    this.assertCrisis(1, 4010);
    this.assertCrisis(2, 4010);
    this.assertEvent(1);
    this.assertEvent(2);
    this.assertEvent(1);
    this.assertEvent(2);
    this.assertEvent(1);
    this.assertEvent(2);
    this.assertEvent(1);
    this.assertEvent(2);
    this.assertEvent(1);
    this.assertEvent(2);
    this.assertEvent(1);
    this.assertEvent(2);
    this.assertActNow(1);
    this.sendMove(2, 2000, 3001);
    this.assertCommandFailed(2);
  }
}