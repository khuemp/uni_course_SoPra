package sopra.systemtest.framework.random;

public class ShuffleElement extends RandomNumberElement {

  public ShuffleElement(final int sides) {
    super(sides);
  }

  @Override
  public boolean accept(final RandomNumberVisitor visitor) {
    return visitor.visitShuffle(this);
  }
}
