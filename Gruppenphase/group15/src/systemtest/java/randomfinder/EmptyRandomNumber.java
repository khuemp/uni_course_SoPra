package sopra.systemtest.framework.random;

public class EmptyRandomNumber extends RandomNumberElement {

  public EmptyRandomNumber(final int sides) {
    super(sides);
  }

  @Override
  public boolean accept(final RandomNumberVisitor visitor) {
    return visitor.visitEmptyNumber(this);
  }
}