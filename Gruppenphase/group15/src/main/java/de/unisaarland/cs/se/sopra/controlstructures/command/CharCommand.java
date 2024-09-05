package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;

public abstract class CharCommand extends Command {
  protected final int charId;
  protected boolean modified;
  protected int currentRound;

  public CharCommand(final int charId, final int playerId) {
    super(playerId);
    this.charId = charId;
  }

  public boolean getModified() {
    return this.modified;
  }

  public void setModified(final boolean modified) {
    this.modified = modified;
  }

  public int getCurrentRound() {
    return this.currentRound;
  }

  protected boolean validate(final Game game, final Player player, final Observer observer) {
    if (!super.checkItsPlayersTurn(player, observer, game)) {
      return false;
    }
    if (!player.containsCharacter(this.charId)) {
      observer.notifyCommandFailed(player.getCommId(), "you don't posses this Character");
      return false;
    }
    return true;
  }

}
