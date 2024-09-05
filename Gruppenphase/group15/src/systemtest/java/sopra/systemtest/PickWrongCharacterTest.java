package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class PickWrongCharacterTest extends SystemTest {
  public PickWrongCharacterTest() {
    super(PickWrongCharacterTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(PickWrongCharacterTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 44;
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
    this.assertCharacters(1, 2011, 2009, 2006, 2005);

    this.sendSelectCharacters(1, 2000, 2005);
    this.assertCommandFailed(1);
    this.assertCharacterSpawned(1, 0, 2005);
  }
}
