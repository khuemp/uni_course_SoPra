package sopra.systemtest.advanced;

import java.util.Set;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;
import sopra.systemtest.api.Utils;

public class Scissors extends SystemTest {

  public Scissors() {
    super(Scissors.class, false);
  }

  @Override
  public String createConfig() {
    return Utils.loadResource(Scissors.class, "scissors.json");
  }

  @Override
  protected long createSeed() {
    return 0;
  }

  @Override
  protected Set<Integer> createSockets() {
    return Set.of(1);
  }

  @Override
  public void run() throws TimeoutException {
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
    this.assertNextRound(1, 10);
    this.assertCrisis(1, 4008);
    for (int i = 0; i < 3; ++i) {
      this.assertEvent(1);
    }
    this.assertActNow(1);
    this.sendLeave(1);
    this.assertLeft(1, 0);
  }
}



