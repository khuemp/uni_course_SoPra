package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.cards.Card;
import de.unisaarland.cs.se.sopra.cards.UseCardVisitor;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import java.util.Optional;

public class UseCardCommand extends InGameCommand {

    private final int cardId;

    public UseCardCommand(final int commId, final int cardId) {
        super(commId);
        this.cardId = cardId;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        final int playerId = model.getPlayerID(super.getCommId());
        final Player player = model.getPlayer(playerId);
        final Optional<Card> optionalCard = player.getCard(cardId);
        if (optionalCard.isPresent()) {
            final UseCardVisitor visitor = new UseCardVisitor(model, connection,
                    player, super.getCommId());
            optionalCard.get().accept(visitor);
        } else {
            connection.sendCommandFailed(super.getCommId(), "Player does not have card!");
        }
    }
}
