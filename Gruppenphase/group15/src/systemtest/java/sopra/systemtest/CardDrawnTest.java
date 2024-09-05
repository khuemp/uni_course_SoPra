package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class CardDrawnTest extends SystemTest {
  public CardDrawnTest() {
    super(CardDrawnTest.class, false);
  }

  @Override
  public String createConfig() {
    return Utils.loadResource(CardDrawnTest.class, "basicConfig.json");
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
    this.assertCharacters(1, 2010, 2006, 2007, 2003);
    this.sendSelectCharacters(1, 2010, 2003);
    this.assertCharacterSpawned(1, 0, 2003);
    this.assertCharacterSpawned(1, 0, 2010);
    this.assertCardDrawn(1, 0, 1022);
    this.assertCardDrawn(1, 0, 1001);
    this.assertCardDrawn(1, 0, 1012);
    this.assertCardDrawn(1, 0, 1013);
    this.assertCardDrawn(1, 0, 1034);
  }
}
