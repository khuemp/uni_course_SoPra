package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.server.Observer;

public class SelectCharacter extends Command {

  private int id0;
  private int id1;

  public SelectCharacter(final int commId, final int id0, final int id1) {
    super(commId);
    this.id0 = id0;
    this.id1 = id1;
  }

  @Override
  public void execute(final Game game) {
    if (game.getCharacterSelectCommId() != this.commId) {
      final Observer obs = game.getObs();
      obs.notifyCommandFailed(this.commId, "Not your turn to select character");
    }
    final Integer playerId = game.getPlayerIdByCommId(this.commId);
    if (!this.checkPlayerExists(playerId, game)) {
      return;
    }
    if (this.id0 < this.id1) {
      game.addCharacterToPlayer(playerId, this.id0);
      game.addCharacterToPlayer(playerId, this.id1);
    } else {
      game.addCharacterToPlayer(playerId, this.id1);
      game.addCharacterToPlayer(playerId, this.id0);
    }
    game.emptyCharSelection();
  }


  public void setId0(final int id0) {
    this.id0 = id0;
  }

  public void setId1(final int id1) {
    this.id1 = id1;
  }


}
