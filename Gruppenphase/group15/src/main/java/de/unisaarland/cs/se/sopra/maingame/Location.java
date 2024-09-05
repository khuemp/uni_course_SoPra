package de.unisaarland.cs.se.sopra.maingame;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import java.util.ArrayList;
import java.util.List;

public class Location extends Place {
  private final String name;
  private final int survivorSpaces;
  private final int initStackSize;

  public Location(final int locId, final int entrances, final String name,
                  final int survivorSpaces, final List<Card> cards) {
    super(locId, entrances, cards);
    this.name = name;
    this.survivorSpaces = survivorSpaces;
    this.initStackSize = this.cards.size();
  }

  @Override
  public boolean hasChildren() {
    return false;
  }

  public int numObstruction(final Obstruction obstruction) {
    int res = 0;
    for (final Entrance e : this.entranceList) {
      res += e.numObstruction(obstruction);
    }
    return res;
  }

  public boolean areCardsLeft() {
    return !this.cards.isEmpty();
  }

  public int getInitStackSize() {
    return this.initStackSize;
  }

  public List<Card> getCards(final int numCards) {
    if (this.cards.size() < numCards) {
      return null;
    }
    final List<Card> newCards = new ArrayList<>(numCards);
    for (int i = 0; i < numCards; i++) {
      newCards.add(this.cards.remove(0));
    }
    return newCards;
  }


  @Override
  public boolean addCharacter(final Character character) {
    if (this.characters.size() < this.survivorSpaces) {
      this.characters.add(character);
      character.setLocId(this.locId);
      return true;
    }
    return false;
  }

  @Override
  public boolean addCharacterWithoutSet(final Character character) {
    if (this.characters.size() < this.survivorSpaces) {
      this.characters.add(character);
      return true;
    }
    return false;
  }

  @Override
  public boolean hasChars() {
    return !this.characters.isEmpty();
  }

  public boolean hasObstruction(final Obstruction obstruction) {
    return this.numObstruction(obstruction) > 0;
  }

  public int numCards() {
    return this.cards.size();
  }

  private int distributeEmptySlots(final Entrance currEn, final int count, final Game g) {
    int zombieCount = count;
    final int barricades = currEn.numObstruction(Obstruction.BARRICADE);
    if (barricades > 0) {
      final boolean check = currEn.removeObstruction(Obstruction.BARRICADE);
      if (check) {
        this.obs.broadcastBarricadeDestroyed(this.locId, currEn.getId());
        zombieCount--;
      }
    } else {
      final Character toKill = this.getNextInfected();
      g.killCharacter(toKill.getId());
      //this.obs.broadcastSurvivorKilled(toKill.getId());
      if (g.getGameEnded()) {
        return -1;
      }
    }
    return zombieCount;
  }

  //TODO : this may not work correctly now

  public void distributeZombies(final Game g, final int spawningZombies) {
    int zombieCount = spawningZombies;
    int entrance = 0;
    while (zombieCount > 0) {
      entrance = entrance % this.entrances;
      final Entrance currEn = this.entranceList.get(entrance);
      final int emptySlots = currEn.numObstruction(Obstruction.EMPTY);
      if (emptySlots == 0) {
        zombieCount = this.distributeEmptySlots(currEn, zombieCount, g);
        if (zombieCount == -1) {
          return;
        }
      }
      if (currEn.addObstruction(Obstruction.ZOMBIE)) {
        this.obs.broadcastZombieSpawned(this.locId, entrance);
        zombieCount--;
      }

      entrance++;
    }
  }

  public int getSurvivorSpaces() {
    return this.survivorSpaces;
  }

  public String getName() {
    return this.name;
  }
}

