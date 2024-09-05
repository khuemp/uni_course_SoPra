package de.unisaarland.cs.se.sopra.controlstructures.activeabilities;

import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;
import sopra.comm.FoodChange;

public class AbilityNoTarget implements AbilityVisitor {

  private final Game game;
  private final int charId;
  private final int playerId;

  public AbilityNoTarget(final Game game, final int charId, final int playerId) {
    this.game = game;
    this.charId = charId;
    this.playerId = playerId;
  }

  @Override
  public void visit(final HealAcAbility h) {
    final Player player = this.game.getPlayerById(this.playerId);
    this.game.getObs().notifyCommandFailed(player.getCommId(), "Wrong visitor created");
  }

  @Override
  public void visit(final BarricadeAcAbility b) {
    final Player player = this.game.getPlayerById(this.playerId);
    this.game.getObs().notifyCommandFailed(player.getCommId(), "Wrong visitor created");
  }

  @Override
  public void visit(final KillAcAbility k) {
    final Player player = this.game.getPlayerById(this.playerId);
    this.game.getObs().notifyCommandFailed(player.getCommId(), "Wrong visitor created");
  }

  @Override
  public void visit(final FeedAcAbility f) {
    final Player player = this.game.getPlayerById(this.playerId);
    final Character character = this.game.getCharById(this.charId);
    if (character == null) {
      this.game.getObs().notifyCommandFailed(player.getCommId(), "This character is not active");
      return;
    }

    if (f.getLastUsed() != this.game.getRound()) {
      this.game.getObs().broadcastAbilityUsed(this.charId);

      this.game.getColony().addFood(f.getNumFood());
      this.game.getObs().broadcastFoodChanged(f.getNumFood(), FoodChange.FOOD_ADDED);
      f.setLastUsed(this.game.getRound());
    } else {
      this.game
          .getObs().notifyCommandFailed(player.getCommId(), "Already used feed ability this round");
    }
  }
}
