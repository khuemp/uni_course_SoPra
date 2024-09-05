package sopra.systemtest.framework.random;

import java.util.Random;

public class SeedFinder {

  public static int findSeed(final Iterable<RandomNumberElement> conditions,
                             final int upperSeedBound) {
    int seed = 0;
    while (seed < upperSeedBound) {
      final var random = new Random(seed);

      boolean match = true;

      final var visitor = new RandomNumberVisitor(random);

      for (final var c : conditions) {
        if (!c.accept(visitor)) {
          seed++;
          match = false;
          break;
        }
      }

      if (match) {
        return seed;
      }
    }
    return -1;
  }
}
