package de.unisaarland.cs.se.sopra.controlstructures.passivabilities;

import de.unisaarland.cs.se.sopra.controlstructures.command.Trash;

public class TrashAbility extends PassiveAbility {
  final int numCards;

  public TrashAbility(final int numCards) {
    this.numCards = numCards;
  }

  @Override
  public void modify(final Trash t) {
    t.setNumCards(this.numCards);
    t.setModified(true);
  }
}
