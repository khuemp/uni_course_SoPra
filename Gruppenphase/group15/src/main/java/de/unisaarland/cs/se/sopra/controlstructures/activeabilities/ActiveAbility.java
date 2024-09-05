package de.unisaarland.cs.se.sopra.controlstructures.activeabilities;

public interface ActiveAbility {
  void accept(AbilityVisitor v);
}
