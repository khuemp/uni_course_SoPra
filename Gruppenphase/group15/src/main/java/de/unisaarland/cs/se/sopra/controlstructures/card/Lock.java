package de.unisaarland.cs.se.sopra.controlstructures.card;

import de.unisaarland.cs.se.sopra.maingame.CrisisCardType;

public class Lock extends TargetCard {

  public Lock(final int cardId) {
    super(cardId);
  }

  @Override
  public void accept(final CardVisitor v) {
    v.visit(this);
  }

  @Override
  public CrisisCardType checkCrisisType() {
    return CrisisCardType.STUFF;
  }
}