package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;

public class Leave extends Command {
  public Leave(final int commId) {
    super(commId);
  }

  //todo : if player calls leave in registration phase, registration is aborted
  @Override
  public void execute(final Game game) {
    final Integer playerId = game.getPlayerIdByCommId(this.commId);
    if (!this.checkPlayerExists(playerId, game)) {
      return;
    }
    final Player player = game.getPlayerById(playerId);
    final Observer observer = game.getObs();

    if (!this.checkItsPlayersTurn(player, observer, game)) {
      return;
    }

    game.leave(this.commId);
  }
}
