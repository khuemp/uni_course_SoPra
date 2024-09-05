package de.unisaarland.cs.se.sopra.controlstructures.card;

import de.unisaarland.cs.se.sopra.maingame.CrisisCardType;

public class Food extends NoTargetCard {
  final int foodAmount;

  public Food(final int cardId, final int foodAmount) {
    super(cardId);
    this.foodAmount = foodAmount;
  }

  @Override
  public void accept(final CardVisitor v) {
    v.visit(this);
  }

  @Override
  public CrisisCardType checkCrisisType() {
    return CrisisCardType.FOOD;
  }


}
