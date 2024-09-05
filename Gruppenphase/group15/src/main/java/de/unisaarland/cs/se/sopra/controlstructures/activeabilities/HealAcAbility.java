package de.unisaarland.cs.se.sopra.controlstructures.activeabilities;

public class HealAcAbility implements ActiveAbility {
  private int lastUsed;

  public HealAcAbility() {
    this.lastUsed = 0;
  }

  @Override
  public void accept(final AbilityVisitor v) {
    v.visit(this);
  }

  public int getLastUsed() {
    return this.lastUsed;
  }

  public void setLastUsed(final int lastUsed) {
    this.lastUsed = lastUsed;
  }
}
