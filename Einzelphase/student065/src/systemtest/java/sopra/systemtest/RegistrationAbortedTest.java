package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class RegistrationAbortedTest extends SystemTest {
    public RegistrationAbortedTest() {
        super(RegistrationAbortedTest.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(RegistrationAbortedTest.class, "configuration.json");
    }

    @Override
    protected long createSeed() {
        return 12;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        this.sendRegister(1, "Anna");
        this.assertConfig(1, createConfig());
        this.sendLeave(1);
        this.assertRegistrationAborted(1);
    }
}
