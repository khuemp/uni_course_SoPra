package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.activeabilities.AbilityTarget;
import de.unisaarland.cs.se.sopra.controlstructures.activeabilities.ActiveAbility;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;

public class UseTargetAbility extends Command {
  private final int charId;
  private final int target;

  public UseTargetAbility(final int commId, final int charId, final int target) {
    super(commId);
    this.charId = charId;
    this.target = target;
  }

  @Override
  public void execute(final Game game) {
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
    final Character character = game.getCharById(this.charId);
    if (character == null) {
      game.getObs().notifyCommandFailed(this.commId, "You don't have this character");
      return;
    }
    if (!player.containsCharacter(this.charId)) {
      game.getObs().notifyCommandFailed(this.commId, "You don't have this character");
      return;
    }
    final ActiveAbility activeAbility = character.getActiveAbility();
    final AbilityTarget abilityTargetVisitor =
        new AbilityTarget(this.charId, this.target, game, playerId);

    activeAbility.accept(abilityTargetVisitor);
  }
}
