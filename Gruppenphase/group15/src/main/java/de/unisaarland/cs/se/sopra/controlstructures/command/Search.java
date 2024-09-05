package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.controlstructures.equipment.Equipment;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Colony;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Location;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.List;
import org.slf4j.LoggerFactory;

public class Search extends CharCommand {
  int numCards = 1;
  int locId;

  public Search(final int commId, final int charId) {
    super(charId, commId);
  }

  private static boolean isColony(final Colony colony, final int locId) {
    return locId == colony.getLocId();
  }

  public int getLocId() {
    return this.locId;
  }

  private void setLocId(final int locId) {
    this.locId = locId;
  }

  public int getNumCards() {
    return this.numCards;
  }

  public void setNumCards(final int numCards) {
    this.numCards = numCards;
  }

  //validates the command and calls
  // the private searchLocation Method
  @Override
  public void execute(final Game game) {
    this.currentRound = game.getRound();
    final Integer playerId = game.getPlayerIdByCommId(this.commId);
    if (!this.checkPlayerExists(playerId, game)) {
      return;
    }
    final Player player = game.getPlayerById(playerId);
    final Observer observer = game.getObs();

    if (!this.validate(game, player, observer)) {
      return;
    }

    final Character character = game.getCharById(this.charId);
    if (character == null) {
      observer.notifyCommandFailed(player.getCommId(),
          "You don't have this character");
      return;
    }
    this.setLocId(character.getLocId());

    if (!player.checkDiceValue(this.charId, true)) {
      observer.notifyCommandFailed(player.getCommId(),
          "you don't have a dice of high enough value left");
      return;
    }
    final Colony colony = game.getColony();
    if ((Search.isColony(colony, character.getLocId()))) {
      observer.notifyCommandFailed(player.getCommId(), "you can't search in the Colony");
      return;
    }
    final Location loc = game.getLocById(character.getLocId());
    if (!this.checkPlaceExists(loc, game)) {
      return;
    }
    if (!loc.areCardsLeft()) {
      observer.notifyCommandFailed(player.getCommId(), "nothing to find here");
      return;
    }
    this.searchLocation(character, player, loc, game, observer);
    player.removeDiceValue(character.getSearchValue());
  }

  //determines if the Location is searched or a random meeting happens,
  // then either calls the randomMeeting method or searches the Location
  private void searchLocation(final Character character, final Player player, final Location loc,
                              final Game game, final Observer observer) {
    observer.broadcastSearched(character.getId(), this.locId);
    final int rolledDice = game.rollDice2(loc.getInitStackSize());
    LoggerFactory.getLogger(Search.class).debug("WÜRFEL HIER HALLO!!!:" + loc.getInitStackSize());
    //LoggerFactory.getLogger(Search.class).debug("WÜRFEL HIER HALLO!!!:" + rolledDice);
    //observer.broadcastDieRolled(player.getCommId(), rolledDice);
    if (game.existLeftoverSurvivors()) {
      if (0 == rolledDice) {
        this.randomMeeting(game);
      }
    }
    final List<Equipment> equipmentList = character.getEquipments();
    if (equipmentList != null) {
      equipmentList.forEach(eq -> eq.modify(this));
    }
    if (!this.modified && (character.getPassiveAbility() != null)) {
      character.getPassiveAbility().modify(this);
    }
    final List<Card> cards;
    cards = loc.getCards(this.numCards);
    for (final Card card : cards) {
      observer.broadcastCardDrawn(player.getId(), card.getId());
    }
    player.addCards(cards);
  }

  //handles a randomMeeting
  private void randomMeeting(final Game game) {
    final int numChild = game.rollDice2(4);
    //observer.broadcastDieRolled(player.getCommId(), numChild);
    final int playerId = game.getPlayerIdByCommId(this.commId);
    game.assignNextCharacter(playerId, numChild);
  }
}
