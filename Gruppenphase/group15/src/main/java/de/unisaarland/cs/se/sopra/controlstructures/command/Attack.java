package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.equipment.Equipment;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Entrance;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Obstruction;
import de.unisaarland.cs.se.sopra.maingame.Place;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.maingame.Wound;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.EnumMap;
import java.util.List;

public class Attack extends CharCommand {
  final int entranceId;
  private final EnumMap<Wound, Wound> woundType = new EnumMap<>(Wound.class);
  boolean isInfectionDiceRolled = true;

  public Attack(final int commId, final int charId, final int entranceId) {
    super(charId, commId);
    this.entranceId = entranceId;
    this.woundType.put(Wound.BITE, Wound.BITE);
    this.woundType.put(Wound.WOUND, Wound.WOUND);
    this.woundType.put(Wound.FROSTBITE, Wound.FROSTBITE);
    this.woundType.put(Wound.NONE, Wound.NONE);
  }

  public void setInfectionDiceRolled(final boolean infectionDiceRolled) {
    this.isInfectionDiceRolled = infectionDiceRolled;
  }

  //checks if the command is valid and calls the private function handleAttack
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

    if (player.containsCharacter(this.charId)) {

      observer.notifyCommandFailed(player.getCommId(), "you don't posses this Character");
      return;
    }

     */
    if (!player.checkDiceValue(this.charId, false)) {
      observer.notifyCommandFailed(player.getCommId(),
          "you don't have a dice of high enough value left");
      return;
    }
    final Character character = game.getCharById(this.charId);
    if (character == null) {
      observer.notifyCommandFailed(player.getCommId(),
          "Character couldn't be obtained from Game");
      return;
    }
    this.handleAttack(game, player, character, observer);
  }

  //retrieves the entrance using the locId in the character
  // and calls removeObstruction
  private void handleAttack(final Game game, final Player player, final Character character,
                            final Observer observer) {
    final Place place = game.getPlaceById(character.getLocId());
    if (!this.checkPlaceExists(place, game)) {
      observer.notifyCommandFailed(player.getCommId(), "This place does not exist");
      return;
    }
    if (!place.containsEntrance(this.entranceId)) {
      observer.notifyCommandFailed(player.getCommId(),
          "This place don't have that entrance");
      return;
    }
    final Entrance entrance = place.getEntranceById(this.entranceId);
    this.removeObstruction(game, entrance, character, observer, player);
  }

  //validates that the entrance exists and has zombie to attack,
  // then removes the Zombie from the entrance and sends the necessary event
  private void removeObstruction(final Game game, final Entrance entrance,
                                 final Character character,
                                 final Observer observer, final Player player) {
    if (entrance == null) {
      observer.notifyCommandFailed(player.getCommId(), "tried to attack on nonexistent entrance");
      return;
    }
    if (entrance.numObstruction(Obstruction.ZOMBIE) < 1) {
      observer.notifyCommandFailed(player.getCommId(), "no zombie to attack");
      return;
    }
    final List<Equipment> equipmentList = character.getEquipments();
    if (equipmentList != null) {
      equipmentList.forEach(eq -> eq.modify(this));
    }
    if (!this.modified && (character.getPassiveAbility() != null)) {
      character.getPassiveAbility().modify(this);
    }
    final boolean check = entrance.removeObstruction(Obstruction.ZOMBIE);
    if (!check) {
      observer.notifyCommandFailed(this.commId, "Zombie couldn't be removed");
      return;
    }
    observer.broadcastZombieKilled(this.charId, character.getLocId(), this.entranceId);
    if (this.isInfectionDiceRolled) {
      final Place p = game.getPlaceById(character.getLocId());
      p.rollInfection(game, this.woundType, this.charId);
    }
    if (game.getGameEnded()) {
      return;
    }
    player.removeDiceValue(character.getAttackValue());
  }
}