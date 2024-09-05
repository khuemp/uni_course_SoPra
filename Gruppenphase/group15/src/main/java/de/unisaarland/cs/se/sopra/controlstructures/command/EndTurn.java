package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;

public class EndTurn extends Command {

  public EndTurn(final int commId) {
    super(commId);
  }

  @Override
  public void execute(final Game game) {
    final Integer playerId = game.getPlayerIdByCommId(this.commId);
    if (!this.checkPlayerExists(playerId, game)) {
      return;
    }
    final Player player = game.getPlayerById(playerId);

    if (!this.checkItsPlayersTurn(player, game.getObs(), game)) {
      return;
    }
    player.setTurnEnded(true);
  }
}
