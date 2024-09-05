package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.State;
import de.unisaarland.cs.se.sopra.server.Observer;

public class StartGame extends Command {

  final int commId;

  public StartGame(final int commId) {
    super(commId);
    this.commId = commId;
  }

  @Override
  public void execute(final Game game) {
    if (game.getPhase() != State.REGISTRATION) {
      final Observer obs = game.getObs();
      obs.notifyCommandFailed(this.commId, "game is already started");
      return;
    }
    if (game.getCurrentPlayers() == 0) {
      final Observer obs = game.getObs();
      obs.notifyCommandFailed(this.commId,
          "game needs at least one registered player to be abel to start");
      return;
    }
    //todo : hier vllt direkt beginPreparationPhase aufrufen?
    game.setPhase(State.PREPARATION);
  }
}
