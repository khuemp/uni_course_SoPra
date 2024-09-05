package de.unisaarland.cs.se.sopra.maingame;


import static de.unisaarland.cs.se.sopra.maingame.Obstruction.BARRICADE;
import static de.unisaarland.cs.se.sopra.maingame.Obstruction.ZOMBIE;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import sopra.comm.FoodChange;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;

public class Game {
  //todo stratCards in Colony
  private final Map<Integer, Player> players = new HashMap<>();
  private final Colony colony;
  private final Map<Integer, Location> locations;
  private final int maxRound;
  private final Goal goal;
  private final List<Crisis> crises;
  private final Map<Integer, Character> activeCharacters = new HashMap<>();
  private final List<Character> leftoverSurvivors;
  private final Random dice;
  private final int maxPlayers;
  private final Map<Integer, Integer> commIdMapping = new HashMap<>();
  private final JSONObject jsonObj;
  private final List<Character> charSelection = new ArrayList<>(4);
  private int round;
  private State phase;
  private Crisis currentCrisis;
  private Observer obs;
  private boolean hasGameEnded;
  private int characterSelectCommId;


  public Game(final Colony colony, final Map<Integer, Location> locations,
              final Goal goal, final List<Crisis> crises,
              final List<Character> leftoverSurvivors,
              final int maxPlayers, final long seed, final JSONObject jsonObj) {
    this.colony = colony;
    this.locations = locations;
    this.goal = goal;
    this.maxRound = goal.getMaxRounds();
    this.crises = crises;
    this.leftoverSurvivors = leftoverSurvivors;
    this.maxPlayers = maxPlayers;
    this.dice = new Random(seed);
    this.jsonObj = jsonObj;
    this.phase = State.REGISTRATION;
    this.round = goal.getMaxRounds() + 1;
  }

  // translates an infection dice to a corresponding wound
  public static Wound translateToWound(final int diceRoll,
                                       final Map<Wound, Wound> translateWound) {
    if (diceRoll == 11) {
      return translateWound.get(Wound.BITE);
    } else if (diceRoll == 9 || diceRoll == 10) {
      return translateWound.get(Wound.FROSTBITE);
    } else if (diceRoll >= 6 && diceRoll < 9) {
      return translateWound.get(Wound.WOUND);
    } else {
      return translateWound.get(Wound.NONE);
    }
  }

  public boolean getGameEnded() {
    return this.hasGameEnded;
  }

  public void setGameEnded() {
    this.hasGameEnded = true;
  }

  public Character getCharById(final int charId) {
    return this.activeCharacters.get(charId);
  }

  public Location getLocById(final int locId) {
    return this.locations.get(locId);
  }

  public Place getPlaceById(final int placeId) {
    if (this.colony.getLocId() == placeId) {
      return this.colony;
    } else {
      return this.getLocById(placeId);
    }
  }

  public Player getPlayerById(final int playerId) {
    return this.players.get(playerId);
  }

  // returns locId charcter is at
  public int locateCharacterById(final int charId) {
    final Character character = this.activeCharacters.get(charId);
    return character.getLocId();
  }

  // assigns next charcter from leftover survivors to player, and adds children to colony
  public void assignNextCharacter(final int playerId, final int numChildren) {
    this.nextCharacter(playerId);
    this.colony.addChildren(numChildren);
    for (int i = 1; i <= numChildren; i++) {
      this.obs.broadcastChildSpawned();
    }
  }

  // spawn a new charcter from leftover survivors list
  public void nextCharacter(final int playerId) {
    if (!this.leftoverSurvivors.isEmpty()) {
      final Character chara = this.leftoverSurvivors.remove(0);
      final Player p = this.getPlayerById(playerId);
      // this.obs.broadcastCharacterSpawned(p.getId(), chara.getId());
      p.addCharacter(chara);
      this.activeCharacters.put(chara.getId(), chara);
      this.colony.addCharacter(chara);
      //return true;
    }
    //return false;
  }

  public Character getLeftoverById(final int charId) {
    for (final Character c : this.charSelection) {
      if (c.getId() == charId) {
        return c;
      }
    }
    return null;
  }

  public void addCharacterToPlayer(final int playerId, final int charId) {
    final Character character = this.getLeftoverById(charId);
    final Player player = this.getPlayerById(playerId);
    //LoggerFactory.getLogger(Game.class)
    //.debug("leftoversurvivor size : " + this.leftoverSurvivors.size());
    if (character == null) {
      this.obs.notifyCommandFailed(player.getCommId(), "Character didn't exist.");
      return;
    }
    player.addCharacter(character);
    this.colony.addCharacter(character);
    this.activeCharacters.put(character.getId(), character);
    this.charSelection.remove(character);
  }

  public void emptyCharSelection() {
    final Iterator<Character> iti = this.charSelection.iterator();
    while (iti.hasNext()) {
      LoggerFactory.getLogger(Game.class).debug("");
      final Character curr = iti.next();
      this.leftoverSurvivors.add(curr);
      iti.remove();
    }
    LoggerFactory.getLogger(Game.class)
        .debug("Selection list empty " + this.charSelection.isEmpty());
  }

  // 0 is the error here todo: is this really reasonable
  public int getPlayerIdToChar(final int charId) {
    for (final Player p : this.players.values()) {
      if (p.containsCharacter(charId)) {
        return p.getId();
      }
    }
    return 0;
  }

  public boolean existLeftoverSurvivors() {
    return !this.leftoverSurvivors.isEmpty();
  }

  // rolls a die for a player, adds it to that players list and sends event
  public void rollDiceForPlayer(final int playerId) {
    final int result = this.dice.nextInt(6) + 1;
    final Player player = this.getPlayerById(playerId);
    player.addDice(result);
    this.obs.broadcastDieRolled(player.getId(), result);
    //return result;
  }

  public int getRound() {
    return this.round;
  }

  // die is rolled by that player, does not add result to list
  public int rollDice(final int upperLimit, final int playerId) {
    final int value = this.dice.nextInt(upperLimit);
    final Player p = this.getPlayerById(playerId);
    this.obs.broadcastDieRolled(p.getId(), value);
    return value;
  }

  public int rollDice2(final int upperLimit) {
    return this.dice.nextInt(upperLimit);
  }

  public void addObserver(final Observer obs) {
    this.obs = obs;
  }

  //maybe change name
  public Set<Integer> getCommIdList() {
    return this.commIdMapping.keySet();
  }

  public List<Player> getPlayersList() {
    return new ArrayList<>(this.players.values());
  }

  /*
  public void crisisEnded() {
    final int moralChange = this.currentCrisis.getConsequence();
    this.changeMoral(moralChange);
    this.currentCrisis = null;
  }
  */

  public void gameEnded(final boolean win) {
    this.obs.broadcastGameEnd(win);
  }

  public void register(final String name, final int commId) {
    LoggerFactory.getLogger(Game.class).debug("Players " + this.players.size());
    if (this.players.size() >= this.maxPlayers) {
      this.obs.broadcastRegistrationAborted();
      return;
    }
    final int maxId;
    if (!this.players.keySet().isEmpty()) {
      maxId = Collections.max(this.players.keySet());
    } else {
      maxId = -1;
    }
    LoggerFactory.getLogger(Game.class).debug("id is" + maxId + "commId is " + commId);
    //todo doppelte registrierung eines spielers über commId abfangen?
    if (this.commIdMapping.containsKey(commId)) {
      this.obs.notifyCommandFailed(commId, "don't register twice dummy");
      return;
    }
    final Player newPlayer = new Player(maxId + 1, commId, this.obs, name);
    this.players.put(newPlayer.getId(), newPlayer);

    this.commIdMapping.put(commId, maxId + 1);

    //todo : Spiel direkt starten wenn maxPlayers erreicht ist
    if (this.getGameEnded()) {
      return;
    }
    this.obs.notifyConfig(commId, this.jsonObj);
  }

  public int getCurrentPlayers() {
    return this.players.size();
  }

  public int getMaxPlayers() {
    return this.maxPlayers;
  }

  public void changeMoral(final int value) {
    this.colony.moralChange(value);
    final int currMoral = this.colony.getMoral();
    if (currMoral <= 0) {
      this.gameEnded(false);
    }
  }

  private void shuffleEverything() {
    this.colony.shuffleCards(this.dice);
    final Collection<Location> locs = this.locations.values();
    final Comparator<Location> comp1 = (o1, o2) -> {
      final Integer id1 = o1.locId;
      final Integer id2 = o2.locId;
      return id1.compareTo(id2);
    };
    final List<Location> locsSorted = new ArrayList<>(locs);
    Collections.sort(locsSorted, comp1);
    for (final Location l : locsSorted) {
      l.shuffleCards(this.dice);
    }
    Collections.shuffle(this.crises, this.dice);
  }

  public void beginPreparationPhase() throws TimeoutException {
    //give 5 cards to each player
    //final Collection<Player> playersSorted = this.players.values();
    this.colony.setObs(this.obs);
    for (final Place p : this.locations.values()) {
      p.setObs(this.obs);
    }
    final Comparator<Player> comp = (o1, o2) -> {
      final Integer id1 = o1.getId();
      final Integer id2 = o2.getId();
      return id1.compareTo(id2);
    };
    this.obs.broadcastGameStarted();
    //stream.toList() creates an immutable list which can not be sorted
    //final List<Player> playersSortedList = playersSorted.stream().toList();
    final List<Player> playersSortedList = new ArrayList<>(this.players.values());
    playersSortedList.sort(comp);
    for (final Player p : playersSortedList) {
      this.obs.broadcastPlayer(p.getId(), p.getName());
    }
    //final List<Card> cards = this.colony.getCards();
    /*
    for (int i = 0; i < cards.size(); i++) {
      if (cards.get(i) == null) {
        LoggerFactory.getLogger(Game.class).error("Nullposition: " + i);
      }
    } */
    this.shuffleEverything();
    // check beforehand with number of players
    for (final Player p : playersSortedList) {
      Collections.shuffle(this.leftoverSurvivors, this.dice);
      //assign characters
      final Character char0 = this.leftoverSurvivors.remove(0);
      final Character char1 = this.leftoverSurvivors.remove(0);
      final Character char2 = this.leftoverSurvivors.remove(0);
      final Character char3 = this.leftoverSurvivors.remove(0);
      if (char0 != null && char1 != null && char2 != null && char3 != null) {
        this.charSelection.add(char0);
        this.charSelection.add(char1);
        this.charSelection.add(char2);
        this.charSelection.add(char3);
        this.obs.notifyCharacters(p.getCommId(), char0.getId(), char1.getId(), char2.getId(),
            char3.getId());
      } else {
        this.obs.notifyCommandFailed(p.getCommId(), "Could not draw enough Characters");
        return;
      }

      if (this.colony.numCards() < 5) {
        this.obs.notifyCommandFailed(p.getCommId(), "not enough cards left");
        return;
      }
      final List<Card> newCards = new ArrayList<>(5);
      /*
      if (this.colony.getCards().contains(null)) {
        LoggerFactory.getLogger(Game.class).debug("irgendeine Karte ist null");
      }
      */
      for (int i = 0; i < 5 && !this.colony.getCards().isEmpty(); i++) {
        LoggerFactory.getLogger(Game.class).debug("Kartenzahl: {}", this.colony.getCards().size());
        final Card newCard = this.colony.getCard();
        this.obs.broadcastCardDrawn(p.getId(), newCard.getId());
        newCards.add(newCard);
      }
      p.addCards(newCards);
    }
    Collections.shuffle(this.leftoverSurvivors, this.dice);
    //this should only be called once
    //this.obs.broadcastGameStarted();
  }

  public Observer getObs() {
    return this.obs;
  }

  public boolean drawCrisis() {
    if (!this.crises.isEmpty()) {
      this.currentCrisis = this.crises.remove(0);
      return true;
    }
    return false;
  }

  public void beginPlayerPhase() {
    this.round--;
    if (this.round == 0) {
      this.obs.broadcastGameEnd(false);
      return;
    }
    this.phase = State.PLAYER;
    this.obs.broadcastNextRound(this.round);
    this.drawCrisis();
    this.obs.broadcastCrisis(this.currentCrisis.getId());

    for (final Player player : this.players.values()) {
      for (int i = 0; i < player.getNumDice(); i++) {
        this.rollDiceForPlayer(player.getId());
      }
      player.setTurnEnded(true);
    }
    for (final Character chara : this.activeCharacters.values()) {
      if (chara.hasFrostbite()) {
        chara.wound(Wound.WOUND);
        if (chara.numWounds() == 3) {
          this.killCharacter(chara.getId());
        }
      }
    }
  }

  public State getPhase() {
    return this.phase;
  }

  public void setPhase(final State phase) {
    this.phase = phase;
  }

  public void beginColonyPhase() {
    //todo : check if correct place
    if (this.goal.getLocationsWithZombies().isPresent()) {
      if (this.goal.getLocationsWithZombies().get() < this.numLocsWithZombies()) {
        this.gameEnded(false);
      }
      if (this.getGameEnded()) {
        return;
      }
    }
    this.obs.broadcastColonyPhaseStarted();
    this.feedSurvivors();
    final int starve = this.colony.getStarvationPoints();
    if (starve != 0) {
      this.obs.broadcastMoralChanged(-starve, MoralChange.STARVATION_TOKEN);
      this.changeMoral(-starve);
    }
    if (this.getGameEnded()) {
      return;
    }
    final int trash = this.colony.getTrash();
    if (-(trash / 10) != 0) {
      this.obs.broadcastMoralChanged(-(trash / 10), MoralChange.WASTE);
      this.changeMoral(-(trash / 10));
    }
    if (this.getGameEnded()) {
      return;
    }
    final int crisisConsequence = this.currentCrisis.getConsequence();
    //this.crisisEnded();
    if (crisisConsequence != 0) {
      this.obs.broadcastMoralChanged(crisisConsequence, MoralChange.CRISIS);
      this.changeMoral(crisisConsequence);
    }
    if (this.getGameEnded()) {
      return;
    }
    this.distributeZombies();
    if (this.getGameEnded()) {
      return;
    }
    if (this.colony.getMoral() == 0) {
      this.obs.broadcastGameEnd(false);
    }

    final boolean gameWon =
        //todo how does this work
        this.goal.checkGoalFulfilled(this.numLocsWithZombies(),
            this.getObstructionNum(BARRICADE), this.round);
    if (gameWon) {
      this.obs.broadcastGameEnd(true);
    }
    this.phase = State.COLONY;
  }

  /*
  private int getCommIdToCharId(final int charId) {
    final int playerId = this.getPlayerIdToChar(charId);
    final Player p = this.getPlayerById(playerId);
    return p.getCommId();
  }*/

  public void deletePlayer(final int playerId) {
    this.players.remove(playerId);
  }

  /*
  public void killChild() {
      colony.killChild();
  }*/

  /*
  public boolean arePlayersLeft() {
      return !players.isEmpty();
  }*/

  public void killCharacter(final int charId) {
    //this.obs.broadcastMoralChanged(-1, MoralChange.CHARACTER_DIED);
    final int playerId = this.getPlayerIdToChar(charId);
    final Player p = this.getPlayerById(playerId);
    if (p != null) {
      if (p.killCharacter(charId)) {
        this.obs.broadcastSurvivorKilled(charId);
        this.obs.broadcastMoralChanged(-1, MoralChange.CHARACTER_DIED);
      }
    }
    final Character character = this.getCharById(charId);
    final int locId = character.getLocId();
    if (this.colony.getLocId() == locId) {
      this.colony.removeCharacter(character);
    } else {
      final Location loc = this.getLocById(locId);
      loc.removeCharacter(character);
    }
    this.activeCharacters.remove(charId);
    this.changeMoral(-1);
    if (this.getGameEnded()) {
      return;
    }
    if (p != null) {
      this.respawn(p);
    }
  }

  public void killCharacterLeave(final int charId) {

    this.obs.broadcastSurvivorKilled(charId);
    this.obs.broadcastMoralChanged(-1, MoralChange.CHARACTER_DIED);
    final Character character = this.getCharById(charId);
    final int locId = character.getLocId();
    if (this.colony.getLocId() == locId) {
      this.colony.removeCharacter(character);
    } else {
      final Location loc = this.getLocById(locId);
      loc.removeCharacter(character);
    }
    this.activeCharacters.remove(charId);
    this.changeMoral(-1);
    if (this.getGameEnded()) {
      return;
    }

  }

  private void respawn(final Player p) {
    p.setTurnEnded(true);
    if (!p.charactersLeft()) {
      if (this.leftoverSurvivors.isEmpty()) {
        this.leave(p.getCommId());
        if (this.players.isEmpty()) {
          this.gameEnded(false);
        }
      } else {
        this.nextCharacter(p.getId());
      }
    }
  }

  public int getObstructionNum(final Obstruction obstruction) {
    int res = this.colony.numObstruction(obstruction);
    for (final Location loc : this.locations.values()) {
      res += loc.numObstruction(obstruction);
    }
    return res;
  }

  public void addCardToTrash(final int cardId, final int playerId) {
    final Player player = this.getPlayerById(playerId);
    player.removeCard(cardId);
    this.colony.addToTrash();
  }

  public void feedSurvivors() {
    final int neededFood =
        (int) Math.ceil((double)
            (this.colony.getNumChildren() + this.colony.numCharacters()) / 2.0);
    final int existingFood = this.colony.getFood();
    if (existingFood >= neededFood) {
      this.colony.removeFood(neededFood);
      this.obs.broadcastFoodChanged(neededFood, FoodChange.FOOD_CONSUMED);
    } else {
      this.colony.addStarvationPoint();
      this.obs.broadcastStarvationTokenAdded();
    }
  }

  public Colony getColony() {
    return this.colony;
  }

  public int numLocsWithZombies() {
    //should colony be counted here as well?
    int res = 0;
    for (final Location location : this.locations.values()) {
      if (location.hasObstruction(ZOMBIE)) {
        res++;
      }
    }
    return res;
  }

  public void distributeZombies() {
    final int spawningZombiesColony =
        (int) Math.ceil(((double)
            (this.colony.numCharacters() + this.colony.getNumChildren())) / 2.0);
    LoggerFactory.getLogger(Game.class).debug("Has " + this.colony.numCharacters() + " Chars");
    LoggerFactory.getLogger(Game.class).debug("Has " + this.colony.getNumChildren() + " Kids");
    LoggerFactory.getLogger(Game.class).debug("Spawnin " + spawningZombiesColony + " Zombies");
    this.colony.distributeZombies(this, spawningZombiesColony);
    final Collection<Location> locs = this.locations.values();
    final Comparator<Location> comp = (o1, o2) -> {
      final Integer id1 = o1.locId;
      final Integer id2 = o2.locId;
      return id1.compareTo(id2);
    };
    final List<Location> locsSorted = new ArrayList<>(locs);
    Collections.sort(locsSorted, comp);
    for (final Location loc : locsSorted) {
      final int zombies = loc.numCharacters();
      loc.distributeZombies(this, zombies);
    }
  }

  public Integer getPlayerIdByCommId(final int commId) {
    LoggerFactory.getLogger(Game.class).debug("trying to get with value " + commId);
    if (!this.commIdMapping.containsKey(commId)) {
      return null;
    }
    return this.commIdMapping.get(commId);
  }

  public void leave(final int commId) {
    if (this.phase == State.REGISTRATION || this.phase == State.PREPARATION) {
      this.obs.broadcastRegistrationAborted();
      return;
    }
    final int playerId = this.getPlayerIdByCommId(commId);
    final Player player = this.getPlayerById(playerId);
    this.obs.broadcastLeft(playerId);
    player.setTurnEnded(true);

    final List<Character> characters = new ArrayList<>(player.getCharacters().values());
    final Comparator<Character> comp = (o1, o2) -> {
      final Integer id1 = o1.getSocialStatus();
      final Integer id2 = o2.getSocialStatus();
      return id2.compareTo(id1);
    };
    characters.sort(comp);
    this.deletePlayer(playerId);
    for (final Character character : characters) {
      //this.obs.broadcastSurvivorKilled(character.getId());
      this.killCharacterLeave(character.getId());
    }
  }

  public Crisis getCurrentCrisis() {
    return this.currentCrisis;
  }

  public int getMaxRound() {
    return this.maxRound;
  }

  public Random getDice() {
    return this.dice;
  }

  public int getCharacterSelectCommId() {
    return this.characterSelectCommId;
  }

  public void setCharacterSelectCommId(final int commId) {
    this.characterSelectCommId = commId;
  }

  public void killCharacterInfection(final int charId) {
    this.obs.broadcastMoralChanged(-1, MoralChange.CHARACTER_DIED);
    this.changeMoral(-1);
    if (this.getGameEnded()) {
      LoggerFactory.getLogger(Game.class).debug("game was ended");
      return;
    }
    LoggerFactory.getLogger(Game.class).debug("game was not ended");
    final int playerId = this.getPlayerIdToChar(charId);
    final Player p = this.getPlayerById(playerId);
    if (p != null) {
      p.killCharacter(charId);
    }
    final Character character = this.getCharById(charId);
    final int locId = character.getLocId();
    final Place place = this.getPlaceById(locId);
    place.removeCharacter(character);
    this.activeCharacters.remove(charId);
    if (p != null) {
      this.respawn(p);
    }
  }

}
