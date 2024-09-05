package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class TooManyRegisterTest extends SystemTest {

  public TooManyRegisterTest() {
    super(TooManyRegisterTest.class, false);
  }


  @Override
  public String createConfig() {
    return Utils.loadResource(TooManyRegisterTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 20;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1, 2, 3);
  }


  @Override
  protected void run() throws TimeoutException, AssertionError {
    this.sendRegister(1, "Hans");
    this.assertConfig(1, createConfig());
    this.sendRegister(2, "Gunther");
    this.assertConfig(2, createConfig());
    this.assertGameStarted(1);
    this.assertGameStarted(2);
    this.assertPlayer(1, 0, "Hans");
    this.assertPlayer(2, 0, "Hans");
    this.assertPlayer(1, 1, "Gunther");
    this.assertPlayer(2, 1, "Gunther");

    this.assertCharacters(1, 2010, 2006, 2007, 2003);
    this.sendRegister(3, "Dieter");
    this.assertCommandFailed(3);
    this.sendSelectCharacters(1, 2010, 2007);
    this.assertCharacterSpawned(1, 0, 2007);
    this.assertCharacterSpawned(2, 0, 2007);
    this.assertCharacterSpawned(1, 0, 2010);
    this.assertCharacterSpawned(2, 0, 2010);

    this.assertCardDrawn(1, 0, 2005);

  }
}