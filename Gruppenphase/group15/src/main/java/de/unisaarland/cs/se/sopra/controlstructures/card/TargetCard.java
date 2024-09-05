package de.unisaarland.cs.se.sopra.controlstructures.card;

import de.unisaarland.cs.se.sopra.maingame.CrisisCardType;
import de.unisaarland.cs.se.sopra.maingame.Location;
import java.util.Map;

public abstract class TargetCard implements Card {
  protected int cardId;

  public TargetCard(final int cardId) {
    this.cardId = cardId;
  }

  @Override
  public abstract void accept(final CardVisitor v);

  @Override
  public int getId() {
    return this.cardId;
  }

  @Override
  public abstract CrisisCardType checkCrisisType();

  @Override
  public void checkLocIdValid(final Map<Integer, Location> locationMap, final int colonyId) {
    // empty
  }
}
