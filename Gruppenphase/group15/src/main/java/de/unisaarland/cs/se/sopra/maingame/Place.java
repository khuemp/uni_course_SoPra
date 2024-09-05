package de.unisaarland.cs.se.sopra.maingame;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import org.slf4j.LoggerFactory;

public abstract class Place {
  protected final int locId;
  protected final int entrances;
  protected final List<Entrance> entranceList;
  //todo cards
  protected final List<Card> cards;
  protected Observer obs;
  Comparator<Character> comp = Comparator.comparingInt(Character::getSocialStatus);
  protected final PriorityQueue<Character> characters = new PriorityQueue<>(this.comp);

  public Place(final int locId, final int entrances, final List<Card> cards1) {
    this.locId = locId;
    this.entrances = entrances;
    this.entranceList = new ArrayList<>(this.entrances);
    for (int i = 0; i < entrances; i++) {
      final Entrance newE = new Entrance(i);
      this.entranceList.add(newE);
    }
    if (cards1 == null) {
      this.cards = new ArrayList<>();
    } else {
      this.cards = cards1;
    }
  }

  public int getLocId() {
    return this.locId;
  }

  public Entrance getEntranceById(final int entranceId) {
    if (entranceId < this.entranceList.size() && entranceId >= 0) {
      return this.entranceList.get(entranceId);
    }
    return null;

  }

  public void shuffleCards(final Random die) {
    Collections.shuffle(this.cards, die);
  }

  public boolean containsCharacter(final int charId) {
    for (final Character currChar : this.characters) {
      if (currChar.getId() == charId) {
        return true;
      }
    }
    return false;
  }

  public Character getNextInfected() {
    return this.characters.poll();
  }

  public int numCharacters() {
    return this.characters.size();
  }

  public boolean containsEntrance(final int entranceId) {
    for (final Entrance entrance : this.entranceList) {
      if (entrance.getId() == entranceId) {
        return true;
      }
    }
    return false;
  }

  public abstract boolean addCharacter(Character character);

  public abstract boolean addCharacterWithoutSet(Character character);

  public boolean removeCharacter(final Character character) {
    if (this.characters.contains(character)) {
      this.characters.remove(character);
      return true;
    }
    return false;
  }

  public abstract boolean hasChars();

  public abstract boolean hasChildren();


  public boolean spawnZombies(final int zombieAmount) {
    int zombiesLeft = zombieAmount;
    boolean freeSpace;

    while (zombiesLeft > 0) {
      freeSpace = false;
      for (int i = 0; i < this.entranceList.size() && zombiesLeft > 0; ++i) {
        if (this.getEntranceById(i).addObstruction(Obstruction.ZOMBIE)) {
          zombiesLeft -= 1;
          freeSpace = true;
        }
      }
      if (!freeSpace) {
        return false;
      }
    }
    return true;
  }


  public void setObs(final Observer obs) {
    this.obs = obs;
  }

  public List<Card> getCards() {
    return this.cards;
  }

  public void rollInfection(final Game g, final EnumMap<Wound, Wound> translateWound,
                            final int charId) {
    final int diceRoll = g.rollDice2(12);
    LoggerFactory.getLogger(Place.class).debug("INFECTION DIE: " + diceRoll);
    final Wound w = Game.translateToWound(diceRoll, translateWound);
    LoggerFactory.getLogger(Place.class).debug("Wounde: " + w);
    if (w == Wound.BITE) {
      this.obs.broadcastBitten(charId);
      g.killCharacterInfection(charId);
      if (g.getGameEnded()) {
        LoggerFactory.getLogger(Place.class).debug("broken out becaus game ended");
        return;
      }
      this.infectionLoop(this.locId, g);
    } else if (w == Wound.WOUND || w == Wound.FROSTBITE) {
      final Character c = g.getCharById(charId);
      if (w == Wound.WOUND) {
        c.wound(Wound.WOUND);
        this.obs.broadcastWounded(c.getId());
      }
      if (w == Wound.FROSTBITE) {
        c.wound(Wound.FROSTBITE);
        this.obs.broadcastFrostbitten(c.getId());
      }
      c.wound(w);
    }
  }

  public void infectionLoop(final int locId, final Game game) {
    LoggerFactory.getLogger(Place.class).debug("In Infection Loop");
    final Place place = game.getPlaceById(locId);
    while (place.hasChars() || place.hasChildren()) {
      final int nextDice = game.rollDice2(12);
      LoggerFactory.getLogger(Place.class).debug("INFECTION DIE: " + nextDice);
      final HashMap<Wound, Wound> standardTranslation = new HashMap<>(4);
      standardTranslation.put(Wound.WOUND, Wound.WOUND);
      standardTranslation.put(Wound.BITE, Wound.BITE);
      standardTranslation.put(Wound.FROSTBITE, Wound.FROSTBITE);
      standardTranslation.put(Wound.NONE, Wound.NONE);
      final Wound w1 = Game.translateToWound(nextDice, standardTranslation);
      if (w1 == Wound.NONE) {
        LoggerFactory.getLogger(Place.class).debug("broken out of loop, no wound");
        break;
      } else {
        if (place.hasChildren()) {
          LoggerFactory.getLogger(Place.class).debug("killing a child");
          this.infectionChildren(game.getColony(), game);
        } else {
          if (place.hasChars()) {
            LoggerFactory.getLogger(Place.class).debug("killing character");
            this.infectionChars(game);
          } else {
            break;
          }
        }
      }
    }
  }

  private void infectionChildren(final Colony colony, final Game game) {
    this.obs.broadcastBitten();
    colony.killChild(game);
  }

  private void infectionChars(final Game game) {
    final Character nextInfected = this.getNextInfected();
    this.obs.broadcastBitten(nextInfected.getId());
    game.killCharacterInfection(nextInfected.getId());
    if (game.getGameEnded()) {
      return;
    }
  }
}
