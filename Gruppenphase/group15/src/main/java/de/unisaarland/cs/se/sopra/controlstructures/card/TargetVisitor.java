package de.unisaarland.cs.se.sopra.controlstructures.card;

import de.unisaarland.cs.se.sopra.controlstructures.equipment.Equipment;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Entrance;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Obstruction;
import de.unisaarland.cs.se.sopra.maingame.Place;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;

public class TargetVisitor implements CardVisitor {

  final int playerId;
  final int characterId;
  final int target;
  final Game game;

  public TargetVisitor(final int playerId, final int characterId,
                       final int target, final Game game) {
    this.playerId = playerId;
    this.characterId = characterId;
    this.target = target;
    this.game = game;
  }

  @Override
  public void visit(final Food c) {
    final Observer obs = this.game.getObs();
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    obs.notifyCommandFailed(commId, "this seems to be a no target card");
  }

  //todo : command failed
  @Override
  public void visit(final Stuff c) {
    final Observer obs = this.game.getObs();
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    obs.notifyCommandFailed(commId, "this seems to be a no target card");
  }

  @Override
  public void visit(final Medicine c) {
    final int targetCharId = this.target;
    final Player player = this.game.getPlayerById(this.playerId);

    final Observer obs = this.game.getObs();
    if (!this.sameLocation(this.characterId, targetCharId,
        "Character to be healed needs to be in the same location")
        || !player.containsCard(c.cardId)) {
      obs.notifyCommandFailed(player.getCommId(), "could not be healed");
      return;
    }

    final Character targetCharacter = this.game.getCharById(targetCharId);
    final boolean check = targetCharacter.beHealed();

    if (!check) {
      obs.notifyCommandFailed(player.getCommId(), "could not be healed");
      return;
    }
    obs.broadcastCardUsed(c.cardId, this.characterId, this.target);
    this.game.addCardToTrash(c.cardId, this.playerId);
  }

  @Override
  public void visit(final Scissors c) {
    final Character character = this.game.getCharById(this.characterId);
    final int placeId = character.getLocId();
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    final Place place = this.game.getPlaceById(placeId);
    if (!this.checkPlaceExists(place, this.game, commId)) {
      return;
    }
    final int entranceId = this.target;
    final Entrance entrance = place.getEntranceById(entranceId);
    if (!this.checkEntraceExists(entrance, this.game, commId)) {
      return;
    }
    final Observer obs = this.game.getObs();

    if (entrance == null) {
      obs.notifyCommandFailed(commId, "Scissors need Zombie at entrance to activate");
      return;
    }
    if (entrance.numObstruction(Obstruction.ZOMBIE) == 0) {
      obs.notifyCommandFailed(commId, "Scissors need Zombie at entrance to activate");
      return;
    }

    if (entrance.removeObstruction(Obstruction.ZOMBIE)) {
      obs.broadcastCardUsed(c.cardId, this.characterId, this.target);
      obs.broadcastZombieKilled(this.characterId, character.getLocId(), entranceId);
      this.game.addCardToTrash(c.cardId, this.playerId);
    }
  }

  @Override
  public void visit(final Lock c) {
    final Character character = this.game.getCharById(this.characterId);
    final int placeId = character.getLocId();
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    final Place place = this.game.getPlaceById(placeId);
    if (!this.checkPlaceExists(place, this.game, commId)) {
      return;
    }
    final int entranceId = this.target;
    final Entrance entrance = place.getEntranceById(entranceId);
    if (!this.checkEntraceExists(entrance, this.game, commId)) {
      return;
    }

    if (entrance.numObstruction(Obstruction.EMPTY) == 0) {
      final Observer obs = this.game.getObs();
      obs.notifyCommandFailed(commId, "Lock needs empty space at entrance to activate");
      return;
    }

    if (entrance.addObstruction(Obstruction.BARRICADE)) {
      final Observer obs = this.game.getObs();
      obs.broadcastCardUsed(c.cardId, this.characterId, this.target);
      obs.broadcastBarricaded(this.characterId, character.getLocId(), entranceId);
      this.game.addCardToTrash(c.cardId, this.playerId);
    }
  }

  @Override
  public void visit(final Fuel c) {
    final int targetLocationId = this.target;
    final Place targetPlace = this.game.getPlaceById(targetLocationId);
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    if (!this.checkPlaceExists(targetPlace, this.game, commId)) {
      return;
    }
    final Observer obs = this.game.getObs();
    //TODO check if the survivor must be yours
    if (!player.containsCharacter(this.characterId)) {
      obs.notifyCommandFailed(player.getCommId(), "you don't have this character");
      return;
    }
    final Character character = this.game.getCharById(this.characterId);

    //TODO
    if (character.getLastMove() == this.game.getRound()) {
      obs.notifyCommandFailed(player.getCommId(), "this Character already moved this round");
      return;
    }
    if (!targetPlace.addCharacterWithoutSet(character)) {
      obs.notifyCommandFailed(commId, "character couldn't move there");
      return;
    }
    final int oldLocId = character.getLocId();
    final Place currentPlace = this.game.getPlaceById(oldLocId);
    currentPlace.removeCharacter(character);
    character.setLastMove(this.game.getRound());
    obs.broadcastCardUsed(c.cardId, this.characterId, this.target);
    this.game.addCardToTrash(c.cardId, this.playerId);
    character.setLocId(targetPlace.getLocId());
  }

  @Override
  public void visit(final Equipment e) {
    final int targetCharacterId = this.target;
    if (!this.sameLocation(this.characterId, targetCharacterId,
        "Character to be equipped needs to be in the same location")) {
      return;
    }

    final Observer obs = this.game.getObs();
    obs.broadcastCardUsed(e.getId(), this.characterId, this.target);
    this.game.addCardToTrash(e.getId(), this.playerId);
    final Character character = this.game.getCharById(this.characterId);
    character.addEquipment(e);
  }

  private boolean sameLocation(final int characterId1, final int characterId2,
                               final String failureMessage) {
    final Character character1 = this.game.getCharById(characterId1);
    final Character character2 = this.game.getCharById(characterId2);
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    if (!this.checkCharacterExists(character1, this.game, commId)
        || !this.checkCharacterExists(character2, this.game, commId)) {
      return false;
    }
    final int locationId1 = character1.getLocId();
    final int locationId2 = character2.getLocId();

    final Observer obs = this.game.getObs();

    final boolean result = (locationId1 == locationId2);
    if (!result) {
      obs.notifyCommandFailed(commId, failureMessage);
    }

    return result;
  }

  protected boolean checkPlaceExists(final Place place, final Game game, final int commId) {
    if (place == null) {
      final Observer obs = game.getObs();
      obs.notifyCommandFailed(commId, "not a place");
      return false;
    }
    return true;
  }

  protected boolean checkCharacterExists(final Character character, final Game game,
                                         final int commId) {
    if (character == null) {
      final Observer obs = game.getObs();
      obs.notifyCommandFailed(commId, "not a character");
      return false;
    }
    return true;
  }

  protected boolean checkEntraceExists(final Entrance entrance, final Game game, final int commId) {
    if (entrance == null) {
      final Observer obs = game.getObs();
      obs.notifyCommandFailed(commId, "not an entrance");
      return false;
    }
    return true;
  }
}
