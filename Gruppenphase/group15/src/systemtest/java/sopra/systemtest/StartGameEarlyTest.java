package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class StartGameEarlyTest extends SystemTest {
  public StartGameEarlyTest() {
    super(StartGameEarlyTest.class, false);
  }


  @Override
  public String createConfig() {
    return Utils.loadResource(StartGameEarlyTest.class, "basicConfig.json");
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
    this.sendStartGame(1);
    assertCommandFailed(1);
  }
}
