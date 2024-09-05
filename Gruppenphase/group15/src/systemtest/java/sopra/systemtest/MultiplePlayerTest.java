package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class MultiplePlayerTest extends SystemTest {
  public MultiplePlayerTest() {
    super(MultiplePlayerTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(MultiplePlayerTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 20;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1, 2, 3, 4);
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    this.sendRegister(1, "Jeff");
    this.assertConfig(1, createConfig());
    this.sendRegister(2, "Joff");
    this.assertConfig(2, createConfig());
    this.assertGameStarted(1);
    this.assertGameStarted(2);
    this.assertPlayer(1, 0, "Jeff");
    this.assertPlayer(2, 0, "Jeff");
    this.assertPlayer(1, 1, "Joff");
    this.assertPlayer(2, 1, "Joff");
  }

}
