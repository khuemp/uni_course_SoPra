package sopra.systemtest.framework.random;

public abstract class RandomNumberElement {

  private final int sides;

  public RandomNumberElement(final int sides) {
    this.sides = sides;
  }

  public int getSides() {
    return sides;
  }

  public abstract boolean accept(final RandomNumberVisitor visitor);
}