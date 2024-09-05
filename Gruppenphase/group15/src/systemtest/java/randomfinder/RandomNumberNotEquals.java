package sopra.systemtest.framework.random;

public class RandomNumberNotEquals extends RandomNumberElement {

  private final int number;

  public RandomNumberNotEquals(final int sides, final int number) {
    super(sides);
    this.number = number;
  }

  @Override
  public boolean accept(final RandomNumberVisitor visitor) {
    return visitor.visitRandomNumberNotEqual(this);
  }

  public int getNumber() {
    return number;
  }
}
