package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.maingame.Crisis;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;

public class ContributeCard extends Command {
  private final int cardId;

  public ContributeCard(final int commId, final int cardId) {
    super(commId);
    this.cardId = cardId;
  }

  @Override
  public void execute(final Game game) {
    final Integer playerId = game.getPlayerIdByCommId(this.commId);
    if (!this.checkPlayerExists(playerId, game)) {
      return;
    }
    final Player player = game.getPlayerById(playerId);
    final Observer observer = game.getObs();


    if (!super.checkItsPlayersTurn(player, observer, game)) {
      return;
    }

    if (!player.containsCard(this.cardId)) {
      observer.notifyCommandFailed(this.commId, "you don't have this card");
      return;
    }

    final Crisis crisis = game.getCurrentCrisis();
    final Card card = player.getCardById(this.cardId);

    if (!crisis.checkType(card.checkCrisisType())) {
      observer.notifyCommandFailed(this.commId, "this card can't be added to the Crisis");
      return;
    }

    crisis.addCard();
    player.removeCard(this.cardId);
    observer.broadcastContributed(game.getPlayerIdByCommId(this.commId), this.cardId);


  }
}
