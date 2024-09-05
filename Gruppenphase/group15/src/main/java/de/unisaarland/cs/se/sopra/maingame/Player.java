package de.unisaarland.cs.se.sopra.maingame;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Player {

  private final int playerId;
  private final int commId;
  private final String name;
  private final Map<Integer, Card> cardDeck = new HashMap<>();
  private final Map<Integer, Character> characters = new HashMap<>();
  private final List<Integer> rollResults = new ArrayList<>();
  private final Observer obs;
  private boolean turnEnded;

  public Player(final int id, final int commId, final Observer obs, final String name) {
    this.playerId = id;
    this.commId = commId;
    this.obs = obs;
    this.name = name;
  }

  public boolean getTurnEnded() {
    return this.turnEnded;
  }

  public void setTurnEnded(final boolean b) {
    this.turnEnded = b;
  }

  public String getName() {
    return this.name;
  }

  public boolean removeDiceValue(final int value) {
    Collections.sort(this.rollResults);
    /*
    int i = 0;
    while (i < rollResults.size()) {
      if (this.rollResults.get(i) >= value) {
        this.rollResults.remove(this.rollResults.get(i));
        return true;
      }
      i++;
    } */

    final Iterator<Integer> iti = this.rollResults.iterator();
    while (iti.hasNext()) {
      final int curr = iti.next();
      if (curr >= value) {
        iti.remove();
        return true;
      }
    }

    /*
    for (int i = 0; i < this.rollResults.size(); i++) {

    }*/

    return false;
  }

  //remove lowest dice value
  public boolean removeDice() {
    if (this.rollResults.isEmpty()) {
      return false;
    }
    Collections.sort(this.rollResults);
    this.rollResults.remove(0);
    return true;
  }

  public int getCommId() {
    return this.commId;
  }

  public boolean checkDiceExist() {
    return !this.rollResults.isEmpty();
  }

  public boolean checkDiceValue(final int charId, final boolean search) {
    Collections.sort(this.rollResults);
    final int valueToCompare;
    if (search) {
      valueToCompare = this.characters.get(charId).getSearchValue();
    } else {
      valueToCompare = this.characters.get(charId).getAttackValue();
    }

    /*
    for (final int roll : this.rollResults) {
      if (roll >= valueToCompare) {
        return true;
      }
    }*/

    /*
    for (int i = 0; i < this.rollResults.size(); i++) {
      if (this.rollResults.get(i) >= valueToCompare) {
        return true;
      }
    }*/
    final Iterator<Integer> iti = this.rollResults.iterator();
    while (iti.hasNext()) {
      final int curr = iti.next();
      if (curr >= valueToCompare) {
        return true;
      }
    }
    return false;
  }

  public Character getCharById(final int charId) {
    if (!this.characters.containsKey(charId)) {
      return null;
    }
    return this.characters.get(charId);
  }

  public void addCharacter(final Character newChar) {
    final int charId = newChar.getId();
    this.characters.put(charId, newChar);
    this.obs.broadcastCharacterSpawned(this.playerId, charId);
  }

  public boolean killCharacter(final int charId) {
    if (this.characters.containsKey(charId)) {
      this.characters.remove(charId);
      return true;
    }
    return false;
  }

  public boolean containsCharacter(final int charId) {
    return this.characters.containsKey(charId);
  }

  public boolean containsCard(final int cardId) {
    return this.cardDeck.containsKey(cardId);
  }

  public void addCards(final List<Card> newCards) {
    for (final Card card : newCards) {
      final int cardId = card.getId();
      this.cardDeck.put(cardId, card);
    }
  }

  public boolean removeCards(final int cardId) {
    if (this.cardDeck.containsKey(cardId)) {
      this.cardDeck.remove(cardId);
      return true;
    }
    return false;
  }

  public Card getCardById(final int cardId) {
    return this.cardDeck.get(cardId);
  }

  public boolean charactersLeft() {
    return !this.characters.isEmpty();
  }

  public int getId() {
    return this.playerId;
  }

  /*
  public void setRollResults(List<Integer> dices){
      for (int dice : dices){
        rollResults.add(dice);
      }
  }*/

  public void addDice(final int value) {
    this.rollResults.add(value);
  }

  public void removeCard(final int cardId) {
    this.cardDeck.remove(cardId);
  }

  public int getNumDice() {
    return this.characters.size() + 1;
  }

  public boolean checkDiceValueKill(final int dieValue) {
    Collections.sort(this.rollResults);
    /*
    for (int i = 0; i < this.rollResults.size(); i++) {
      if (this.rollResults.get(i) >= dieValue) {
        this.rollResults.remove(this.rollResults.get(i));
        return true;
      }
    } */
    final Iterator<Integer> iti = this.rollResults.iterator();
    while (iti.hasNext()) {
      final int curr = iti.next();
      if (curr >= dieValue) {
        iti.remove();
        return true;
      }
    }
    return false;
  }

  public List<Integer> getRollResults() {
    return this.rollResults;
  }

  public Map<Integer, Character> getCharacters() {
    return this.characters;
  }
}