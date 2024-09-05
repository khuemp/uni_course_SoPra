package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class DoesStartGameEarlySendRegistrationAbortedTest extends SystemTest {
  public DoesStartGameEarlySendRegistrationAbortedTest() {
    super(DoesStartGameEarlySendRegistrationAbortedTest.class, false);
  }


  @Override
  public String createConfig() {
    return Utils.loadResource(DoesStartGameEarlySendRegistrationAbortedTest.class,
        "basicConfig.json");
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
    this.sendStartGame(1);
    assertCommandFailed(1);
    //assertRegistrationAborted(1);
  }
}
