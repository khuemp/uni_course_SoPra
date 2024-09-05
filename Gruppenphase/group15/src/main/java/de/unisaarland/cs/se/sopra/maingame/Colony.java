package de.unisaarland.cs.se.sopra.maingame;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import java.util.List;
import org.slf4j.LoggerFactory;

public class Colony extends Place {
  private static final boolean DEBUG_OUTPUT = true;
  private int moral;
  private int numChildren;
  private int trash;
  private int food;
  private int starvationPoints;

  // todo : an Niklas anpassen
  //todo : moral ist laut spec in config angegeben
  public Colony(final int locId, final int entrances,
                final List<Card> cards) {
    super(locId, entrances, cards);
  }

  // adds character to location, used when moving, or spawning a new character
  @Override
  public boolean addCharacter(final Character character) {
    this.characters.add(character);
    character.setLocId(this.locId);
    return true;
  }

  @Override
  public boolean addCharacterWithoutSet(final Character character) {
    this.characters.add(character);
    return true;
  }

  // returns if any children are left
  @Override
  public boolean hasChildren() {
    return this.numChildren > 0;
  }

  // returns if any characters are left in the colony
  @Override
  public boolean hasChars() {
    return !this.characters.isEmpty();
  }

  // kills a child
  public boolean killChild(final Game game) {
    if (this.numChildren <= 0) {
      this.numChildren = 0;
      return false;
    }
    this.obs.broadcastChildKilled();
    this.numChildren--;
    game.changeMoral(-1);
    //todo : das event hier richtig?
    return true;
  }

  // return moral value
  public int getMoral() {
    return this.moral;
  }

  // removes a specified number of waste
  //todo: possibly command failed -> need to return boolean
  public boolean takeOutTrash(final int numCards) {
    if (this.trash == 0) {
      return false;
    }
    this.trash -= numCards;
    if (this.trash < 0) {
      this.trash = 0;
    }
    return true;
  }

  // returns current trash value
  public int getTrash() {
    return this.trash;
  }

  // returns current number of children
  public int getNumChildren() {
    return this.numChildren;
  }

  // returns cuurent number of food
  public int getFood() {
    return this.food;
  }

  // returns current number of starvation points, used to change moral in colony phase
  public int getStarvationPoints() {
    return this.starvationPoints;
  }

  // adds specified number of food, used in fuel card
  public void addFood(final int num) {
    this.food += num;
  }

  // removes food, used when feeding all charcaters
  public boolean removeFood(final int num) {
    if (this.food - num < 0) {
      return false;
    }
    this.food -= num;
    return true;
  }

  // adds one card to the trash
  public void addToTrash() {
    this.obs.broadcastWasteChanged(1);
    this.trash++;
  }

  // changes moral value, by specified int
  public void moralChange(final int change) {
    //LoggerFactory.getLogger(Colony.class).trace("CurrrentMoral: {}", this.moral);
    this.moral += change;
    if (this.moral < 0) {
      this.moral = 0;
    }
  }

  // adds specified number of children
  public void addChildren(final int num) {
    this.numChildren += num;
  }

  // counts how many of specified obstruction are at all entrances of colony
  public int numObstruction(final Obstruction obstruction) {
    int res = 0;
    for (final Entrance e : this.entranceList) {
      res += e.numObstruction(obstruction);
    }
    return res;
  }

  // checks if somewhere in the colony that obstruction exists
  public boolean hasObstruction(final Obstruction obstruction) {
    return this.numObstruction(obstruction) > 0;
  }

  // adds one starvation point, used when feeding characters
  public void addStarvationPoint() {
    this.starvationPoints++;
  }

  private int distributeToEmptySlots(final Entrance currEn, final int count, final Game g) {
    int zombieCount = count;
    final boolean check = currEn.removeObstruction(Obstruction.BARRICADE);
    if (check) {
      this.obs.broadcastBarricadeDestroyed(this.locId, currEn.getId());
      this.loggerino("Hi i removed obstruction", 999);
      zombieCount--;
    } else {
      if (this.numChildren != 0) {
        this.killChild(g);
        this.loggerino("i killed a child", 999);
        if (g.getGameEnded()) {
          return -1;
        }
      } else {
        final Character toKill = this.getNextInfected();
        if (toKill == null) {
          this.loggerino("trying to kill more characters than are living", 999);
          return -1;
        }
        g.killCharacter(toKill.getId());
        if (g.getGameEnded()) {
          return -1;
        }
        //this.obs.broadcastSurvivorKilled(toKill.getId());
        this.loggerino("i killed a character lmao", 999);
      }
    }
    return zombieCount;
  }

  // distributes zombies at all entrances, used at beginning of colony phase
  public void distributeZombies(final Game g, final int spawningZombies) {
    LoggerFactory.getLogger(Colony.class).debug("Spawnin " + spawningZombies + " Zombies");
    int zombieCount = spawningZombies;
    int entrance = 0;
    while (zombieCount > 0 && !g.getGameEnded()) {
      // TOdo logger auskommentieren
      this.loggerino(" remaining ZombieCount ", zombieCount);
      entrance = entrance % this.entrances;
      this.loggerino("entrance No", entrance);
      final Entrance currEn = this.entranceList.get(entrance);
      final int emptySlots = currEn.numObstruction(Obstruction.EMPTY);
      this.loggerino("empty slots in Entrance", emptySlots);
      if (emptySlots == 0) {
        zombieCount = this.distributeToEmptySlots(currEn, zombieCount, g);
        if (zombieCount == -1) {
          return;
        }
      } else {
        if (currEn.addObstruction(Obstruction.ZOMBIE)) {
          this.obs.broadcastZombieSpawned(this.locId, entrance);
          this.loggerino("No of Zombies in entrance", currEn.numObstruction(Obstruction.ZOMBIE));
          zombieCount--;
        }
      }
      entrance++;
    }
  }

  public int numCards() {
    return this.cards.size();
  }

  public Card getCard() {
    if (this.cards.isEmpty()) {
      LoggerFactory.getLogger(Colony.class).debug("Cards is empty");
      return null;
    }
    LoggerFactory.getLogger(Colony.class).debug("Cards: {}", this.cards);
    return this.cards.remove(0);
  }

  void loggerino(final String msg, final int a) {
    if (DEBUG_OUTPUT) {
      LoggerFactory.getLogger(Colony.class).debug("{} {}", msg, a);
    }
  }
}
