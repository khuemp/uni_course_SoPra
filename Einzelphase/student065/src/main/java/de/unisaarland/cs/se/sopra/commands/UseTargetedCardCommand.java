package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.cards.Card;
import de.unisaarland.cs.se.sopra.cards.UseTargetedCardVisitor;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;

public class UseTargetedCardCommand extends InGameCommand {

    private final int cardId;
    private final int survivorId;
    private final int target;

    public UseTargetedCardCommand(
            final int commId, final int cardId, final int survivorId, final int target) {
        super(commId);
        this.cardId = cardId;
        this.survivorId = survivorId;
        this.target = target;
    }

    @Override
    protected void run(final Model model, final ConnectionWrapper connection) {
        final int playerId = model.getPlayerID(super.getCommId());
        final Player player = model.getPlayer(playerId);
        final Optional<Card> optionalCard = player.getCard(cardId);
        final Optional<Survivor> optionalSurvivor = model.getSurvivor(survivorId);
        if (optionalCard.isPresent()
                && optionalSurvivor.isPresent()
                && player.hasSurvivor(survivorId)) {
            final UseTargetedCardVisitor visitor =
                    new UseTargetedCardVisitor(
                            model,
                            connection,
                            player,
                            super.getCommId(),
                            target,
                            optionalSurvivor.get());
            optionalCard.get().accept(visitor);
        } else {
            connection.sendCommandFailed(super.getCommId(), "Player does not have card!");
        }
    }
}
