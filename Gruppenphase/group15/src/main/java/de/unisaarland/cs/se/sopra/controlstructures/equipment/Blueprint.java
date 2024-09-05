package de.unisaarland.cs.se.sopra.controlstructures.equipment;

import de.unisaarland.cs.se.sopra.controlstructures.command.Attack;
import de.unisaarland.cs.se.sopra.controlstructures.command.Barricade;
import de.unisaarland.cs.se.sopra.controlstructures.command.Move;
import de.unisaarland.cs.se.sopra.controlstructures.command.Search;
import de.unisaarland.cs.se.sopra.controlstructures.command.Trash;
import de.unisaarland.cs.se.sopra.maingame.Location;
import java.util.Map;

public class Blueprint extends Equipment {

  private final int locId;

  public Blueprint(final int locId, final int id) {
    this.locId = locId;
    super.cardId = id;
  }

  public int getLocId() {
    return this.locId;
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
    final int numCardsPulled = s.getNumCards() + 1;
    if (this.getLastUsed() != s.getCurrentRound()) {
      if (!s.getModified()) {
        if (s.getLocId() == this.locId) {
          s.setNumCards(numCardsPulled);
          this.setLastUsed(s.getCurrentRound());
          s.setModified(true);
        }
      }
    }
  }

  @Override
  public void modify(final Move m) {
    //
  }

  @Override
  public void modify(final Barricade b) {
    //
  }

  @Override
  public void checkLocIdValid(final Map<Integer, Location> locationMap, final int colonyId) {
    if (!locationMap.containsKey(this.locId) && this.locId != colonyId) {
      throw new IllegalArgumentException();
    }
  }
}
