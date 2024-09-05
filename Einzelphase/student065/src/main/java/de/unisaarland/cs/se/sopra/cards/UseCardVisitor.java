package de.unisaarland.cs.se.sopra.cards;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadType;
import de.unisaarland.cs.se.sopra.model.Colony;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import java.util.Optional;
import sopra.comm.FoodChange;

public class UseCardVisitor extends CardVisitor {

    public UseCardVisitor(final Model model, final ConnectionWrapper connection,
                          final Player player, final int commId) {
        super(model, connection, player, commId);
    }

    @Override
    public void visit(final Food card) {
        final Colony colony = getModel().getColony();
        colony.addFood(card.getFood());
        getModel().addTrash();
        getPlayer().removeCard(card);
        getConnection().sendCardUsed(card.getId());
        getConnection().sendWasteChanged(1);
        getConnection().sendFoodChanged(card.getFood(), FoodChange.FOOD_ADDED);
        checkCrossroadTrash();
    }

    @Override
    public void visit(final Stuff card) {
        final Player player = getPlayer();
        final Optional<Integer> optionalDie = player.getHighestDie();
        if (optionalDie.isPresent()) {
            player.removeLowestDie();
            final int die = getModel().rollActionDie();
            player.addDie(die);
            getPlayer().removeCard(card);
            getModel().addTrash();
            getConnection().sendCardUsed(card.getId());
            getConnection().sendWasteChanged(1);
            getConnection().sendDieRolled(player.getId(), die);
            checkCrossroadTrash();
        } else {
            getConnection().sendCommandFailed(getModel().getCommId(player.getId()), "No die left!");
        }
    }

    private void checkCrossroadTrash() {
        final Crossroad playerCrossroad;
        if ((playerCrossroad = getPlayer().getCrossroad()) != null) {
            if (playerCrossroad.getType() == CrossroadType.WASTECHANGED
                    && ((playerCrossroad.getNumTrash() < 0
                    && getPlayer().getNumTrashTrigger() >= getModel().getTrashPile())
                    || (playerCrossroad.getNumTrash() > 0
                    && getPlayer().getNumTrashTrigger() <= getModel().getTrashPile())
                    || (playerCrossroad.getNumTrash() == 0))) {
                getPlayer().setCrossroadActivate(true);
            }
        }
    }
}
