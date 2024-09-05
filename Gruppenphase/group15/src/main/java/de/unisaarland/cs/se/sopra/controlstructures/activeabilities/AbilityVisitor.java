package de.unisaarland.cs.se.sopra.controlstructures.activeabilities;

public interface AbilityVisitor {
  void visit(HealAcAbility h);

  void visit(BarricadeAcAbility b);

  void visit(KillAcAbility k);

  void visit(FeedAcAbility f);
}
