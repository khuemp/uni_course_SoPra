package de.unisaarland.cs.se.sopra.controlstructures.equipment;

import de.unisaarland.cs.se.sopra.controlstructures.command.Attack;
import de.unisaarland.cs.se.sopra.controlstructures.command.Barricade;
import de.unisaarland.cs.se.sopra.controlstructures.command.Move;
import de.unisaarland.cs.se.sopra.controlstructures.command.Search;
import de.unisaarland.cs.se.sopra.controlstructures.command.Trash;

public class Colt extends Equipment {

  public Colt(final int id) {
    super.cardId = id;
  }

  @Override
  public void modify(final Trash t) {
    //
  }

  @Override
  public void modify(final Attack a) {
    if (this.getLastUsed() != a.getCurrentRound()) {
      if (!a.getModified()) {
        this.setLastUsed(a.getCurrentRound());
        a.setInfectionDiceRolled(false);
        a.setModified(true);
      }
    }
  }

  @Override
  public void modify(final Search s) {
    //
  }

  @Override
  public void modify(final Move m) {
    //
  }

  @Override
  public void modify(final Barricade b) {
    //
  }
}
