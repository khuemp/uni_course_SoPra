package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.equipment.Equipment;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Entrance;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Obstruction;
import de.unisaarland.cs.se.sopra.maingame.Place;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.List;

public class Barricade extends CharCommand {
  final int entranceId;
  boolean needsDice = true;

  public Barricade(final int commId, final int charId, final int entranceId) {
    super(charId, commId);
    this.entranceId = entranceId;
  }

  public void setNeedsDice(final boolean needsDice) {
    this.needsDice = needsDice;
  }

  //validates the command , gets the entrance using locId
  // and calls the private function barricadeEntrance
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
    /*
    if (!super.checkItsPlayersTurn(player, observer, game)) {
      return;
    }

    if (!player.containsCharacter(this.charId)) {
      observer.notifyCommandFailed(player.getCommId(), "you don't posses this Character");
      return;
    }

     */
    final Character character = game.getCharById(this.charId);
    if (character == null) {
      observer.notifyCommandFailed(player.getCommId(),
          "You don't have this character");
      return;
    }
    final Place place = game.getPlaceById(character.getLocId());
    if (!this.checkPlaceExists(place, game)) {
      return;
    }
    if (!place.containsEntrance(this.entranceId)) {
      observer.notifyCommandFailed(player.getCommId(),
          "This place don't have that entrance");
      return;
    }
    final Entrance entrance = place.getEntranceById(this.entranceId);
    if (this.needsDice && this.barricadeEntrance(character, entrance, observer, player)) {
      player.removeDice();
    }
  }

  //validates the entrance and try's to add the barricade to it
  private boolean barricadeEntrance(final Character character, final Entrance entrance,
                                    final Observer observer, final Player player) {
    if (entrance == null) {
      observer.notifyCommandFailed(player.getCommId(),
          "tried to barricade a nonexistent entrance");
      return false;
    }
    if (entrance.numObstruction(Obstruction.EMPTY) < 1) {
      observer.notifyCommandFailed(player.getCommId(), "Entrance is already full");
      return false;
    }
    if (!(!this.needsDice || player.checkDiceExist())) {
      observer.notifyCommandFailed(player.getCommId(), "no dice left");
      return false;
    }
    final List<Equipment> equipmentList = character.getEquipments();
    if (equipmentList != null) {
      equipmentList.forEach(eq -> eq.modify(this));
    }
    entrance.addObstruction(Obstruction.BARRICADE);
    observer.broadcastBarricaded(this.charId, character.getLocId(), this.entranceId);
    return true;
  }
}
