package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class LeaveTooEarlyTest extends SystemTest {
  public LeaveTooEarlyTest() {
    super(LeaveTooEarlyTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(RegistrationAbortedTest2.class, "basicConfig.json");
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
    this.sendLeave(1);
    this.assertRegistrationAborted(1);
  }
}
