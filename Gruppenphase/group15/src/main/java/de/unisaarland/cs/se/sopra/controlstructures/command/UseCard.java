package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.controlstructures.card.NoTargetVisitor;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;

public class UseCard extends Command {
  private final int cardId;

  public UseCard(final int commId, final int cardId) {
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

    if (player.containsCard(this.cardId)) {
      final Card card = player.getCardById(this.cardId);
      final NoTargetVisitor cardVisitor = new NoTargetVisitor(playerId, game);
      card.accept(cardVisitor);
    } else {
      observer.notifyCommandFailed(this.commId, "you don't have this Card");
    }

  }
}
