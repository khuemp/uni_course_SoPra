package de.unisaarland.cs.se.sopra.controlstructures.command;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.controlstructures.card.TargetVisitor;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;

public class UseTargetCard extends Command {
  private final int cardId;
  private final int charId;
  private final int target;

  public UseTargetCard(final int commId, final int cardId, final int charId, final int target) {

    super(commId);
    this.cardId = cardId;
    this.charId = charId;
    this.target = target;
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

    if (player.containsCard(this.cardId) && player.containsCharacter(this.charId)) {
      final Card card = player.getCardById(this.cardId);
      final TargetVisitor targetCardVisitor =
          new TargetVisitor(playerId, this.charId, this.target, game);
      card.accept(targetCardVisitor);
    } else {
      observer.notifyCommandFailed(this.commId, "you don't have this card or charter");
    }
  }
}
