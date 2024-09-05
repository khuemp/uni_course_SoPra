package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.equipment.Equipment;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Place;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.maingame.Wound;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.EnumMap;
import java.util.List;

public class Move extends CharCommand {
  int locId;
  private EnumMap<Wound, Wound> woundType = new EnumMap<>(Wound.class);


  public Move(final int commId, final int charId, final int locId) {
    super(charId, commId);
    this.locId = locId;
    this.woundType.put(Wound.BITE, Wound.BITE);
    this.woundType.put(Wound.WOUND, Wound.WOUND);
    this.woundType.put(Wound.FROSTBITE, Wound.FROSTBITE);
    this.woundType.put(Wound.NONE, Wound.NONE);
  }

  public void setWoundType(final EnumMap<Wound, Wound> woundType) {
    this.woundType = woundType;
  }


  public int getLocId() {
    return this.locId;
  }

  @Override
  //validates the command then tries to move the character
  // to the specified Place

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
    if (this.locId == character.getLocId()) {
      observer.notifyCommandFailed(this.commId, "you can't move to a location you are already at");
      return;
    }
    if (character.getLastMove() == this.currentRound) {
      observer.notifyCommandFailed(player.getCommId(), "this Character already moved this Round");
      return;
    }
    final List<Equipment> equipmentList = character.getEquipments();
    if (equipmentList != null) {
      equipmentList.forEach(eq -> eq.modify(this));
    }
    if (!this.modified && (character.getPassiveAbility() != null)) {
      character.getPassiveAbility().modify(this);
    }
    final Place place = game.getPlaceById(this.locId);
    if (!this.checkPlaceExists(place, game)) {
      return;
    }
    final int originId = character.getLocId();
    final boolean check = place.addCharacter(character);
    if (check) {
      final Place origin = game.getPlaceById(originId);
      origin.removeCharacter(character);
    }

    if (!check) {
      observer.notifyCommandFailed(player.getCommId(),
          "you can't move there");
      return;
    }
    observer.broadcastMoved(this.charId, this.locId);
    character.setLastMove(this.currentRound);
    place.rollInfection(game, this.woundType, this.charId);
  }

}
