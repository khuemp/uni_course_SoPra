package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class RegisterBasicTest extends SystemTest {
  public RegisterBasicTest() {
    super(RegisterBasicTest.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(RegisterBasicTest.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 40;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1);
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    final String config = this.createConfig();
    this.sendRegister(1, "Anna");
    this.assertConfig(1, config);
  }
}
