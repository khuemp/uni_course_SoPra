package de.unisaarland.cs.se.sopra.controlstructures.activeabilities;

import java.util.HashMap;
import java.util.Map;

public class KillAcAbility implements ActiveAbility {

  private final int locId;
  private final int dieValue;
  private final int numZombies;
  private final boolean infectionDie;
  private final boolean needChildren;
  private final Map<Integer, Integer> activationsPerRound;
  private int maxActivations;

  public KillAcAbility(final int locId, final boolean infectionDie,
                       final int dieValue,
                       final int numZombies, final boolean needChildren, final int maxActivations) {
    this.locId = locId;
    this.infectionDie = infectionDie;
    this.dieValue = dieValue;
    this.numZombies = numZombies;
    this.needChildren = needChildren;
    this.maxActivations = maxActivations;
    this.activationsPerRound = new HashMap<>();
  }

  @Override
  public void accept(final AbilityVisitor v) {
    v.visit(this);
  }

  public int getLocId() {
    return this.locId;
  }

  public int getDieValue() {
    return this.dieValue;
  }

  public boolean isNeedInfectionDice() {
    return this.infectionDie;
  }

  public boolean isNeedChildren() {
    return this.needChildren;
  }

  public int getMaxActivations() {
    return this.maxActivations;
  }

  public void setMaxActivations(final int maxActivations) {
    this.maxActivations = maxActivations;
  }

  public int getNumZombies() {
    return this.numZombies;
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
