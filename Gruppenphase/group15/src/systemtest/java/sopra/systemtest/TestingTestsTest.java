package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

/**
 * Example for how to write a systemtest using our framework.
 */
public class TestingTestsTest extends SystemTest {

  TestingTestsTest() {
    super(TestingTestsTest.class, false);
  }

  @Override
  public String createConfig() {
    return Utils.loadResource(ExampleTest.class, "configuration.json");
  }

  @Override
  public long createSeed() {
    return 42;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1);
  }

  @Override
  public void run() throws TimeoutException {
    this.sendRegister(1, "Lauritz");
    this.assertConfig(1, createConfig());
    this.sendMove(1, 1, 1);
    this.assertRegistrationAborted(1);
  }
}

