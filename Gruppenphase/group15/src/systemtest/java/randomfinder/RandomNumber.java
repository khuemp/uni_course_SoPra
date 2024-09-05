package sopra.systemtest.framework.random;

public class RandomNumber extends RandomNumberElement {

  private final int number;

  public RandomNumber(final int sides, final int number) {
    super(sides);
    this.number = number;
  }

  public int getNumber() {
    return number;
  }

  @Override
  public boolean accept(final RandomNumberVisitor visitor) {
    return visitor.visitRandomNumber(this);
  }
}