package de.unisaarland.cs.se.sopra.controlstructures.passivabilities;

import de.unisaarland.cs.se.sopra.controlstructures.command.Attack;
import de.unisaarland.cs.se.sopra.controlstructures.command.Move;
import de.unisaarland.cs.se.sopra.controlstructures.command.Search;
import de.unisaarland.cs.se.sopra.controlstructures.command.Trash;

public abstract class PassiveAbility {

  public void modify(final Trash t) {
    //
  }

  public void modify(final Move m) {
    //
  }

  public void modify(final Attack a) {
    //
  }

  public void modify(final Search s) {
    //
  }
}
