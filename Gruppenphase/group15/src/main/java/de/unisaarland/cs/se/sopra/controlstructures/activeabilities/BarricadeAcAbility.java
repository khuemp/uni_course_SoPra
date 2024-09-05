package de.unisaarland.cs.se.sopra.controlstructures.activeabilities;

import java.util.HashMap;
import java.util.Map;

public class BarricadeAcAbility implements ActiveAbility {

  private final int numBarricades;
  private final Map<Integer, Integer> activationsPerRound;
  private final int maxActivations;

  public BarricadeAcAbility(final int numBarricades, final int maxActivations) {
    this.numBarricades = numBarricades;
    this.maxActivations = maxActivations;
    this.activationsPerRound = new HashMap<>();
  }

  @Override
  public void accept(final AbilityVisitor v) {
    v.visit(this);
  }

  public int getNumBarricades() {
    return this.numBarricades;
  }

  public int getMaxActivations() {
    return this.maxActivations;
  }

  public void addUseToRound(final int round) {
    final Integer value = this.activationsPerRound.get(round);
    if (value == null) {
      this.activationsPerRound.put(round, 1);
    } else {
      this.activationsPerRound.remove(round);
      this.activationsPerRound.put(round, value + 1);
    }
  }

  public int getUseRound(final int round) {
    final Integer value = this.activationsPerRound.get(round);
    if (value == null) {
      return 0;
    } else {
      return value;
    }
  }
}
