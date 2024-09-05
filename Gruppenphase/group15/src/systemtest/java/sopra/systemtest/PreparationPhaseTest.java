package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class PreparationPhaseTest extends SystemTest {
  public PreparationPhaseTest() {
    super(PreparationPhaseTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(PreparationPhaseTest.class, "configuration.json");
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
    this.sendRegister(1, "Hans");
    this.assertConfig(1, createConfig());
    this.sendStartGame(1);
    this.assertGameStarted(1);
    this.assertPlayer(1, 0, "Hans");
  }
}
