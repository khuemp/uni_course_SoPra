package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.equipment.Equipment;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Colony;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.List;

/**
 *
 */
public class Trash extends CharCommand {
  // could be changed by a modifier
  int numCards = 3;

  public Trash(final int commId, final int charId) {
    super(charId, commId);
  }

  //validates the command then removes the specified amount of trash
  // and sends the corresponding events

  @Override
  public void execute(final Game game) {
    this.currentRound = game.getRound();
    final Integer playerId = game.getPlayerIdByCommId(this.commId);
    final Observer observer = game.getObs();
    final Player player = game.getPlayerById(playerId);

    if (!this.checkPlayerExists(playerId, game)) {
      observer.notifyCommandFailed(player.getCommId(),
          "This player does not exist");
      return;
    }
    if (!this.validate(game, player, observer)) {
      return;
    }
    final Character character = game.getCharById(this.charId);

    if (character == null) {
      observer.notifyCommandFailed(player.getCommId(),
          "You don't have this character");
      return;
    }
    final Colony colony = game.getColony();
    if (character.getLocId() != colony.getLocId()) {
      observer.notifyCommandFailed(player.getCommId(),
          "you need to be in the Colony to clean waste");
      return;
    }

    final int beforeTrash;
    if (player.checkDiceExist()) {
      beforeTrash = colony.getTrash();
      final List<Equipment> equipmentList = character.getEquipments();
      if (equipmentList != null) {
        equipmentList.forEach(eq -> eq.modify(this));
      }
      if (!this.modified && (character.getPassiveAbility() != null)) {
        character.getPassiveAbility().modify(this);
      }
      if (!colony.takeOutTrash(this.numCards)) {
        observer.notifyCommandFailed(this.commId, "there is no trash to remove");
        return;
      }

      player.removeDice();
      observer.broadcastWasteChanged(beforeTrash - colony.getTrash());
    } else {
      observer.notifyCommandFailed(player.getCommId(), "you don't have a Die left");
    }
  }

  public void setNumCards(final int numCards) {
    this.numCards = numCards;
  }
}


