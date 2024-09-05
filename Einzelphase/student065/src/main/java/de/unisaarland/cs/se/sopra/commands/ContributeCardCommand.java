package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.cards.Card;
import de.unisaarland.cs.se.sopra.cards.ContributeCardVisitor;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import java.util.Optional;

public class ContributeCardCommand extends InGameCommand {

    private final int cardId;

    public ContributeCardCommand(final int commId, final int cardId) {
        super(commId);
        this.cardId = cardId;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        final int playerId = model.getPlayerID(super.getCommId());
        final Player player = model.getPlayer(playerId);
        final Optional<Card> card = player.getCard(cardId);
        if (card.isPresent()) {
            final ContributeCardVisitor visitor =
                    new ContributeCardVisitor(model, connection, player,
                            super.getCommId());
            card.get().accept(visitor);
        } else {
            connection.sendCommandFailed(super.getCommId(), "No card with this ID!");
        }
    }

}
