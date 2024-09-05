package de.unisaarland.cs.se.sopra.maingame;

public class Crisis {

  private final int moralChange;
  private final int identifier;
  private final int requiredCards;
  private final CrisisCardType requiredType;
  private int contributedCards;

  public Crisis(final int moralChange, final int identifier, final int requiredCards,
                final CrisisCardType cardRequired) {
    this.moralChange = moralChange;
    this.identifier = identifier;
    this.requiredCards = requiredCards;
    this.requiredType = cardRequired;
  }

  public void addCard() {
    this.contributedCards++;
  }

  public boolean checkType(final CrisisCardType contributedCard) {
    return this.requiredType == contributedCard;
  }

  public int getConsequence() {
    if (this.contributedCards < this.requiredCards) {
      return -this.moralChange;
    } else if (this.contributedCards >= this.requiredCards + 2) {
      return 1;
    } else {
      return 0;
    }
  }

  public int getId() {
    return this.identifier;
  }

}
