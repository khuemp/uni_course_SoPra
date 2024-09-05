package de.unisaarland.cs.se.sopra.controlstructures.activeabilities;

public class FeedAcAbility implements ActiveAbility {
  private final int numFood;
  private int lastUsed;

  public FeedAcAbility(final int numFood) {
    this.numFood = numFood;
  }

  @Override
  public void accept(final AbilityVisitor v) {
    v.visit(this);
  }

  public int getNumFood() {
    return this.numFood;
  }

  public int getLastUsed() {
    return this.lastUsed;
  }

  public void setLastUsed(final int lastUsed) {
    this.lastUsed = lastUsed;
  }
}
