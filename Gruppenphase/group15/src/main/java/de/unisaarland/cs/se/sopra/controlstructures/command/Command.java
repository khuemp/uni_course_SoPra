package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Place;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.maingame.State;
import de.unisaarland.cs.se.sopra.server.Observer;

public abstract class Command {
  protected final int commId;

  public Command(final int commId) {
    this.commId = commId;
  }

  public abstract void execute(Game game);

  protected boolean checkItsPlayersTurn(final Player player, final Observer obs,
                                        final Game game) {
    //TODO : check if this actually fixes stuff, future test should show this
    if (game.getPhase() == State.REGISTRATION) {
      obs.broadcastRegistrationAborted();
      return false;
    }
    if (game.getPhase() != State.PLAYER) {
      obs.notifyCommandFailed(player.getCommId(),
          "command can only be send during player phase");
      return false;
    }
    if (player.getTurnEnded()) {
      obs.notifyCommandFailed(player.getCommId(),
          "it is not your turn");
      return false;
    }
    return true;
  }

  protected boolean checkPlayerExists(final Integer playerId, final Game game) {
    if (playerId == null) {
      final Observer obs = game.getObs();
      obs.notifyCommandFailed(this.commId, "not a registered player");
      return false;
    }
    return true;
  }

  protected boolean checkPlaceExists(final Place place, final Game game) {
    if (place == null) {
      final Observer obs = game.getObs();
      obs.notifyCommandFailed(this.commId, "this place does not exist");
      return false;
    }
    return true;
  }
}
