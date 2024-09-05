package de.unisaarland.cs.se.sopra.cards;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Crisis;
import de.unisaarland.cs.se.sopra.model.CrisisType;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;

public class ContributeCardVisitor extends CardVisitor {

    public ContributeCardVisitor(final Model model, final ConnectionWrapper connection,
                                 final Player player,
                                 final int commId) {
        super(model, connection, player, commId);
    }

    /**
     * This method attempts to contribute the given card to the
     * current crisis.
     *
     * @param card The card to contribute.
     * @param type Which crisis type the card can be contributed to.
     */
    private void handle(final Card card, final CrisisType type) {
        final Crisis crisis = getModel().getCrisis();
        if (crisis.getType() == type) {
            crisis.cardContributed();
            getPlayer().removeCard(card);
            getConnection().sendContributed(getPlayer().getId(), card.getId());
        } else {
            getConnection().sendCommandFailed(getCommId(), "Wrong card type!");
        }
    }

    @Override
    public void visit(final Blueprint card) {
        handle(card, CrisisType.STUFF);
    }

    @Override
    public void visit(final Colt card) {
        handle(card, CrisisType.STUFF);
    }

    @Override
    public void visit(final Food card) {
        handle(card, CrisisType.FOOD);
    }

    @Override
    public void visit(final Fuel card) {
        handle(card, CrisisType.FUEL);
    }

    @Override
    public void visit(final Hammer card) {
        handle(card, CrisisType.STUFF);
    }

    @Override
    public void visit(final Lock card) {
        handle(card, CrisisType.STUFF);
    }

    @Override
    public void visit(final Medicine card) {
        handle(card, CrisisType.MEDICINE);
    }

    @Override
    public void visit(final Scissors card) {
        handle(card, CrisisType.STUFF);
    }

    @Override
    public void visit(final Snowboots card) {
        handle(card, CrisisType.STUFF);
    }

    @Override
    public void visit(final Stuff card) {
        handle(card, CrisisType.STUFF);
    }

    @Override
    public void visit(final Swab card) {
        handle(card, CrisisType.STUFF);
    }

}
