package de.unisaarland.cs.se.sopra.controlstructures.card;

import de.unisaarland.cs.se.sopra.controlstructures.equipment.Equipment;
import de.unisaarland.cs.se.sopra.maingame.Colony;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;
import sopra.comm.FoodChange;

public class NoTargetVisitor implements CardVisitor {

  final int playerId;
  final Game game;

  public NoTargetVisitor(final int playerId, final Game game) {
    this.playerId = playerId;
    this.game = game;
  }

  @Override
  public void visit(final Food c) {
    final Player player = this.game.getPlayerById(this.playerId);
    if (!player.containsCard(c.getId())) {
      final Observer obs = this.game.getObs();
      obs.notifyCommandFailed(player.getCommId(), "player not there or didn't have card");
      return;
    }
    final Observer obs = this.game.getObs();
    obs.broadcastCardUsed(c.getId());
    this.game.addCardToTrash(c.getId(), this.playerId);
    final Colony colony = this.game.getColony();
    colony.addFood(c.foodAmount);
    obs.broadcastFoodChanged(c.foodAmount, FoodChange.FOOD_ADDED);
  }

  @Override
  public void visit(final Stuff c) {
    final Player player = this.game.getPlayerById(this.playerId);

    if (player == null || !player.checkDiceExist() || !player.containsCard(c.getId())) {
      final Observer obs = this.game.getObs();
      obs.notifyCommandFailed(player.getCommId(), "no die left or didn't have card");
      return;
    }

    final Observer obs = this.game.getObs();
    obs.broadcastCardUsed(c.getId());
    this.game.addCardToTrash(c.cardId, this.playerId);
    player.removeDice();
    this.game.rollDiceForPlayer(this.playerId);
  }

  @Override
  public void visit(final Medicine c) {
    final Observer obs = this.game.getObs();
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    obs.notifyCommandFailed(commId, "this seems to be a target card");
  }

  @Override
  public void visit(final Scissors c) {
    final Observer obs = this.game.getObs();
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    obs.notifyCommandFailed(commId, "this seems to be a no target card");
  }

  @Override
  public void visit(final Lock c) {
    final Observer obs = this.game.getObs();
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    obs.notifyCommandFailed(commId, "this seems to be a no target card");
  }

  @Override
  public void visit(final Fuel c) {
    final Observer obs = this.game.getObs();
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    obs.notifyCommandFailed(commId, "this seems to be a no target card");
  }

  @Override
  public void visit(final Equipment e) {
    final Observer obs = this.game.getObs();
    final Player player = this.game.getPlayerById(this.playerId);
    final int commId = player.getCommId();
    obs.notifyCommandFailed(commId, "this seems to be a no target card");
  }
}
