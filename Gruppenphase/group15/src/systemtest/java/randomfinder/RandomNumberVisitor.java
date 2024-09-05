package sopra.systemtest.framework.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.slf4j.LoggerFactory;

public class RandomNumberVisitor {

  final Random random;

  public RandomNumberVisitor(final Random random) {
    this.random = random;
  }

  public boolean visitRandomNumber(final RandomNumber randomNumber) {
    final var r = random.nextInt(randomNumber.getSides());

    LoggerFactory.getLogger(this.getClass()).info("R: %d".formatted(r));
    return r == randomNumber.getNumber();
  }

  public boolean visitEmptyNumber(final EmptyRandomNumber emptyRandomNumber) {
    final var r = random.nextInt(emptyRandomNumber.getSides());

    LoggerFactory.getLogger(this.getClass()).info("E: %d".formatted(r + 1));
    return true;
  }

  public boolean visitShuffle(final ShuffleElement shuffleElement) {
    final var list = new ArrayList<Integer>();

    for (int i = 0; i < shuffleElement.getSides(); i++) {
      list.add(i);
    }

    Collections.shuffle(list, random);

    LoggerFactory.getLogger(this.getClass()).info("S: {}", list.size());
    return true;
  }

  public boolean visitRandomNumberRange(final RandomNumberRange randomNumberRange) {
    final var r = random.nextInt(randomNumberRange.getSides());

    LoggerFactory.getLogger(this.getClass()).info("Range: %d".formatted(r));
    return r <= randomNumberRange.getEnd() && r >= randomNumberRange.getStart();
  }

  public boolean visitRandomNumberNotEqual(final RandomNumberNotEquals randomNumberNotEquals) {
    final var r = random.nextInt(randomNumberNotEquals.getSides());

    LoggerFactory.getLogger(this.getClass()).info("N: %d".formatted(r));
    return r != randomNumberNotEquals.getNumber();
  }
}
