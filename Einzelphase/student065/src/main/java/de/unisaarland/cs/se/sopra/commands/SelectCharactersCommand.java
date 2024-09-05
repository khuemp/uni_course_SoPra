package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.cards.Card;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.List;
import java.util.Optional;

public class SelectCharactersCommand extends InitializationCommand {

    private static final int START_CARDS = 5;
    private final int survivorId0;
    private final int survivorId1;

    public SelectCharactersCommand(final int commId, final int survivorId0,
                                   final int survivorId1) {
        super(commId);
        this.survivorId0 = survivorId0;
        this.survivorId1 = survivorId1;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        final Player player = model.getPlayerByCommId(super.getCommId());
        if (this.survivorId0 != this.survivorId1) {
            if (player != null
                    && player.hasSurvivor(survivorId0) && player.hasSurvivor(survivorId1)) {
                final List<Survivor> survivors = player.getSurvivors();
                player.resetSurvivors();
                final Optional<Survivor> survivor1 = survivors.stream()
                        .filter(s -> s.getId() == Math.min(survivorId0, survivorId1)).findFirst();
                survivor1.ifPresent(value -> addSurvivor(model, connection, player, value));
                survivor1.ifPresent(survivors::remove);
                final Optional<Survivor> survivor2 = survivors.stream()
                        .filter(s -> s.getId() == Math.max(survivorId0, survivorId1)).findFirst();
                survivor2.ifPresent(value -> addSurvivor(model, connection, player, value));
                survivor2.ifPresent(survivors::remove);
                model.putBackSurvivors(survivors);
                drawCards(model, player, connection);
                model.nextPlayer();
            } else {
                connection.sendCommandFailed(super.getCommId(), "Invalid player!");
            }
        } else {
            connection.sendCommandFailed(super.getCommId(), "Cannot select Character twice!");
        }
    }

    /**
     * @param model      The model which holds the cards to draw form.
     * @param player     The player who picks the cars up.
     * @param connection To inform all players.
     */
    private void drawCards(final Model model, final Player player,
                           final ConnectionWrapper connection) {
        for (int i = 0; i < START_CARDS; i++) {
            final Optional<Card> optCard = model.getColony().drawCard();
            if (optCard.isPresent()) {
                final Card card = optCard.get();
                player.addCard(card);
                connection.sendCardDrawn(player.getId(), card.getId());
            }
        }
    }

    /**
     * Adds a survivor to a player.
     *
     * @param model      To add the survivor to its location.
     * @param connection To inform all players.
     * @param player     The player which receives the survivor.
     * @param survivor   The survivor to add.
     */
    private void addSurvivor(final Model model, final ConnectionWrapper connection,
                             final Player player,
                             final Survivor survivor) {
        player.addSurvivor(survivor);
        survivor.setLocation(model.getColony());
        model.getColony().addSurvivor(survivor);
        connection.sendCharacterSpawned(player.getId(), survivor.getId());
    }
}
