package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.State;
import de.unisaarland.cs.se.sopra.server.Observer;

public class Register extends Command {
  final String name;

  public Register(final int commId, final String name) {
    super(commId);
    this.name = name;
  }

  @Override
  public void execute(final Game game) {
    if (game.getPhase() != State.REGISTRATION) {
      final Observer obs = game.getObs();
      obs.notifyCommandFailed(this.commId, "you can only register during registrationsphase");
      return;
    }
    game.register(this.name, this.commId);
  }
}
