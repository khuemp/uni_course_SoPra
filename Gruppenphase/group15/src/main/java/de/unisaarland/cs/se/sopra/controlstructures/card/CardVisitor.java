package de.unisaarland.cs.se.sopra.controlstructures.card;

import de.unisaarland.cs.se.sopra.controlstructures.equipment.Equipment;

public interface CardVisitor {
  void visit(Food c);

  void visit(Stuff c);

  void visit(Medicine c);

  void visit(Scissors c);

  void visit(Lock c);

  void visit(Fuel c);

  void visit(Equipment e);
}
