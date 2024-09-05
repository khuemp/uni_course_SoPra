package de.unisaarland.cs.se.sopra.controlstructures.equipment;

import de.unisaarland.cs.se.sopra.controlstructures.command.Attack;
import de.unisaarland.cs.se.sopra.controlstructures.command.Barricade;
import de.unisaarland.cs.se.sopra.controlstructures.command.Move;
import de.unisaarland.cs.se.sopra.controlstructures.command.Search;
import de.unisaarland.cs.se.sopra.controlstructures.command.Trash;

public class Hammer extends Equipment {

  public Hammer(final int id) {
    super.cardId = id;
  }

  @Override
  public void modify(final Trash t) {
    //
  }

  @Override
  public void modify(final Attack a) {
    //
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
    if (this.getLastUsed() != b.getCurrentRound()) {
      if (!b.getModified()) {
        this.setLastUsed(b.getCurrentRound());
        b.setNeedsDice(false);
        b.setModified(true);
      }
    }
  }
}
