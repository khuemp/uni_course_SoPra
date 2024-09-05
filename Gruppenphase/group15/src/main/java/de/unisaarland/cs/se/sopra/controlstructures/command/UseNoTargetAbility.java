package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.activeabilities.AbilityNoTarget;
import de.unisaarland.cs.se.sopra.controlstructures.activeabilities.ActiveAbility;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;

public class UseNoTargetAbility extends Command {
  private final int charId;

  public UseNoTargetAbility(final int commId, final int charId) {
    super(commId);
    this.charId = charId;
  }

  @Override
  public void execute(final Game game) {
    final Character character = game.getCharById(this.charId);
    if (character == null) {
      game.getObs().notifyCommandFailed(this.commId, "This character does not exist");
      return;
    }
    final Integer playerId = game.getPlayerIdByCommId(this.commId);
    if (!this.checkPlayerExists(playerId, game)) {
      game.getObs().notifyCommandFailed(this.commId, "This player does not exist");
      return;
    }
    if (!this.checkPlayerExists(playerId, game)) {
      return;
    }
    final Player player = game.getPlayerById(playerId);
    if (!this.checkItsPlayersTurn(player, game.getObs(), game)) {
      return;
    }
    if (!player.containsCharacter(this.charId)) {
      game.getObs().notifyCommandFailed(this.commId, "You don't have this character");
      return;
    }
    final ActiveAbility activeAbility = character.getActiveAbility();
    final AbilityNoTarget abilityNoTargetVisitor = new AbilityNoTarget(game, this.charId, playerId);

    activeAbility.accept(abilityNoTargetVisitor);
  }
}