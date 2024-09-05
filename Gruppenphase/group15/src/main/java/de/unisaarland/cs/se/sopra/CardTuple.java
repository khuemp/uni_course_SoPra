package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import java.util.List;
import java.util.Map;

public class CardTuple {
  //ArrayDeque<Card> cardArrayDeque;
  List<Card> cardList;
  Map<Integer, Card> cardHashMap;

  public CardTuple(final Map<Integer, Card> cardHashMap, final List<Card> cardArrayDeque) {
    this.cardList = cardArrayDeque;
    this.cardHashMap = cardHashMap;
  }

  public List<Card> getCardList() {
    return this.cardList;
  }

  public Map<Integer, Card> getCardHashMap() {
    return this.cardHashMap;
  }
}
