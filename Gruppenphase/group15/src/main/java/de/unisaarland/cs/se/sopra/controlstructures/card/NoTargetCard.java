package de.unisaarland.cs.se.sopra.controlstructures.card;

import de.unisaarland.cs.se.sopra.maingame.CrisisCardType;
import de.unisaarland.cs.se.sopra.maingame.Location;
import java.util.Map;

public abstract class NoTargetCard implements Card {
  protected int cardId;

  public NoTargetCard(final int cardId) {
    this.cardId = cardId;
  }

  @Override
  public abstract void accept(CardVisitor v);

  @Override
  public abstract CrisisCardType checkCrisisType();

  @Override
  public int getId() {
    return this.cardId;
  }

  @Override
  public void checkLocIdValid(final Map<Integer, Location> locationMap, final int colonyId) {
    // empty
  }

}
