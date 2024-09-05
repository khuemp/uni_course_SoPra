package sopra.systemtest;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class TryingInvalidInput extends SystemTest {
  public TryingInvalidInput() {
    super(TryingInvalidInput.class, false);
  }

  @Override
  protected String createConfig() {
    return Utils.loadResource(TryingInvalidInput.class, "basicConfig.json");
  }

  @Override
  protected long createSeed() {
    return 0;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1, 2);
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    this.sendRegister(1, "Jeff");
    this.assertConfig(1, createConfig());
    this.sendStartGame(1);
    this.assertGameStarted(1);
    this.assertPlayer(1, 0, "Jeff");
    this.assertCharacters(1, 2002, 2000, 2006, 2004);
    this.sendSelectCharacters(1, 2004, 2006);
    this.assertCharacterSpawned(1, 0, 2004);
    this.assertCharacterSpawned(1, 0, 2006);
    this.assertCardDrawn(1, 0, 1030);
    this.assertCardDrawn(1, 0, 1012);
    this.assertCardDrawn(1, 0, 1033);
    this.assertCardDrawn(1, 0, 1031);
    this.assertCardDrawn(1, 0, 1032);
    this.assertNextRound(1, 1);
    this.assertCrisis(1, 4007);
    this.assertDieRolled(1, 0, 5);
    this.assertDieRolled(1, 0, 2);
    this.assertDieRolled(1, 0, 4);
    this.assertActNow(1);
    this.sendUseCard(1, 1030, 2004, 3001);
    this.assertCardUsed(1, 1030, 2004, 3001);
    this.assertWasteChanged(1, 1);
    this.assertActNow(1);
    this.sendUseCard(1, 1031, 2004, 3000);
    this.assertCommandFailed(1);
  }
}
