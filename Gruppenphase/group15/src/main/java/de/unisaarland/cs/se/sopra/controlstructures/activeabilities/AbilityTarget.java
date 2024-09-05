package de.unisaarland.cs.se.sopra.controlstructures.activeabilities;

import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Entrance;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Obstruction;
import de.unisaarland.cs.se.sopra.maingame.Place;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.maingame.Wound;
import java.util.EnumMap;

public class AbilityTarget implements AbilityVisitor {

  private final Game game;
  private final int charId;
  private final int target;
  private final int playerId;

  public AbilityTarget(final int charId, final int target, final Game game, final int playerId) {
    this.charId = charId;
    this.target = target;
    this.game = game;
    this.playerId = playerId;
  }

  @Override
  public void visit(final HealAcAbility h) {
    final Player player = this.game.getPlayerById(this.playerId);

    if (h.getLastUsed() == this.game.getRound()) {
      this.game
          .getObs().notifyCommandFailed(player.getCommId(),
              "Already used heal ability this round");
      return;
    }
    final Character character = this.game.getCharById(this.charId);
    final Character targetChar = this.game.getCharById(this.target);
    if (character == null || targetChar == null) {
      this.game.getObs().notifyCommandFailed(player.getCommId(), "This character is not active");
      return;
    }
    if (character.getLocId() != targetChar.getLocId()) {
      this.game.getObs().notifyCommandFailed(player.getCommId(),
          "Target character is not in the same location");
      return;
    }
    if (targetChar.numWounds() == 0) {
      this.game.getObs().notifyCommandFailed(player.getCommId(), "No wound to heal");
      return;
    }
    targetChar.beHealed();
    h.setLastUsed(this.game.getRound());

    this.game.getObs().broadcastAbilityUsed(this.charId, this.target);
  }

  @Override
  public void visit(final BarricadeAcAbility b) {
    final Player player = this.game.getPlayerById(this.playerId);

    if (b.getMaxActivations() <= b.getUseRound(this.game.getRound())) {
      this.game.getObs().notifyCommandFailed(player.getCommId(), "Run out of activations");
      return;
    }

    final Character character = this.game.getCharById(this.charId);
    if (character == null) {
      this.game.getObs().notifyCommandFailed(player.getCommId(), "This character is not active");
      return;
    }
    final Place charPlace = this.game.getPlaceById(character.getLocId());

    if (charPlace == null) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "This place does not exist");
      return;
    }
    final int entranceId = this.target;

    if (!charPlace.containsEntrance(entranceId)) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "This entrance does not exist here");
      return;
    }
    final Entrance entrance = charPlace.getEntranceById(entranceId);

    if (entrance == null) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "This entrance does not exist");
      return;
    }

    if (entrance.numObstruction(Obstruction.EMPTY) != 0) {
      b.addUseToRound(this.game.getRound());
      this.game.getObs().broadcastAbilityUsed(this.charId, this.target);
    } else {
      this.game.getObs().notifyCommandFailed(player.getCommId(),
          "No empty slot in this entrance to barricade");
      return;
    }
    final int numBarricades = b.getNumBarricades();

    for (int i = 0; i < numBarricades; i++) {

      entrance.addObstruction(Obstruction.BARRICADE);

    }
  }

  @Override
  public void visit(final KillAcAbility k) {
    final Player player = this.game.getPlayerById(this.playerId);

    if (k.getMaxActivations() <= k.getUseRound(this.game.getRound())) {
      this.game.getObs().notifyCommandFailed(player.getCommId(), "Run out of kill activations");
      return;
    }
    final Character character = this.game.getCharById(this.charId);
    if (character == null) {
      this.game.getObs().notifyCommandFailed(player.getCommId(), "This character is not active");
      return;
    }
    final Place targetPlace = this.game.getPlaceById(k.getLocId());

    if (targetPlace == null) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "This place does not exist");
      return;
    }
    final Place charPlace = this.game.getPlaceById(character.getLocId());

    if (charPlace == null) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "This place does not exist");
      return;
    }

    if (targetPlace.getLocId() != charPlace.getLocId()) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "Can not use kill ability in this location");
      return;
    }
    final int entranceId = this.target;

    if (!charPlace.containsEntrance(entranceId)) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "This entrance do not exist here");
      return;
    }
    if (k.isNeedChildren() && !this.game.getColony().hasChildren()) {
      this.game.getObs().notifyCommandFailed(player.getCommId(), "Need children in colony");
      return;
    }
    if (!player.checkDiceValueKill(k.getDieValue())) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "Do not have corresponding die value");
      return;
    }

    final Entrance targetEntrance = charPlace.getEntranceById(entranceId);

    if (targetEntrance == null) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "This entrance does not exist");
      return;
    }

    if (targetEntrance.numObstruction(Obstruction.ZOMBIE) == 0) {
      this.game.getObs()
          .notifyCommandFailed(player.getCommId(), "No more zombie in this entrance to kill");
      return;
    } else {
      k.addUseToRound(this.game.getRound());
      this.game.getObs().broadcastAbilityUsed(this.charId, this.target);
    }
    this.runKillAbility(k, targetEntrance, character, targetPlace, entranceId);
  }

  @Override
  public void visit(final FeedAcAbility f) {
    final Player player = this.game.getPlayerById(this.playerId);
    this.game.getObs().notifyCommandFailed(player.getCommId(), "Wrong visitor created");
  }

  private void runKillAbility(final KillAcAbility k, final Entrance targetEntrance,
                              final Character character,
                              final Place targetPlace, final int entranceId) {
    final int numZombies = k.getNumZombies();
    for (int i = 0; i < numZombies; i++) {
      if (!targetEntrance.removeObstruction(Obstruction.ZOMBIE)) {
        break;
      } else {
        this.game.getObs().broadcastZombieKilled(this.charId, character.getLocId(), entranceId);
      }
    }

    final EnumMap<Wound, Wound> woundType = new EnumMap<>(Wound.class);
    woundType.put(Wound.BITE, Wound.BITE);
    woundType.put(Wound.WOUND, Wound.WOUND);
    woundType.put(Wound.FROSTBITE, Wound.FROSTBITE);
    woundType.put(Wound.NONE, Wound.NONE);

    if (k.isNeedInfectionDice()) {
      targetPlace.rollInfection(this.game, woundType, this.charId);
    }
  }

}
