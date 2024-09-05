package sopra.systemtest.framework.random;

public class RandomNumberRange extends RandomNumberElement {

  private final int start;
  private final int end;

  /**
   * RandomNumber has to be in range [start,end] inclusive
   *
   * @param start start of the range included
   * @param end   end of the range included
   * @param sides sides of the die
   */
  public RandomNumberRange(final int start, final int end, final int sides) {
    super(sides);
    this.start = start;
    this.end = end;
  }

  @Override
  public boolean accept(final RandomNumberVisitor visitor) {
    return visitor.visitRandomNumberRange(this);
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}
