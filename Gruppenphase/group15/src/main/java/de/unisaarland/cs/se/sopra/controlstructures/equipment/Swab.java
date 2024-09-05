package de.unisaarland.cs.se.sopra.controlstructures.equipment;

import de.unisaarland.cs.se.sopra.controlstructures.command.Attack;
import de.unisaarland.cs.se.sopra.controlstructures.command.Barricade;
import de.unisaarland.cs.se.sopra.controlstructures.command.Move;
import de.unisaarland.cs.se.sopra.controlstructures.command.Search;
import de.unisaarland.cs.se.sopra.controlstructures.command.Trash;

public class Swab extends Equipment {

  public Swab(final int id) {
    super.cardId = id;
  }

  @Override
  public void modify(final Trash t) {
    if (this.getLastUsed() != t.getCurrentRound()) {
      if (!t.getModified()) {
        t.setNumCards(5);
        this.setLastUsed(t.getCurrentRound());
        t.setModified(true);
      }
    }
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
    //
  }

}
