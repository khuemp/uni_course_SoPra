package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadType;
import de.unisaarland.cs.se.sopra.model.Location;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;

public class CleanWasteVisitor extends PassiveAbilityVisitor {

    private static final int STANDARD_TRASH = 3;

    public CleanWasteVisitor(final Model model, final ConnectionWrapper connection,
                             final Player player, final Survivor survivor) {
        super(model, connection, player, survivor);
    }

    @Override
    public void executeStandardAction() {
        final Optional<Integer> die = getPlayer().getHighestDie();
        if (die.isPresent()) {
            if (cleanWaste(STANDARD_TRASH)) {
                getPlayer().removeLowestDie();
            }
        } else {
            getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                    "No die left!");
        }
    }

    @Override
    public void visit(final TrashAbility trashAbility) {
        cleanWaste(trashAbility.getAmount());
    }

    @Override
    public void visit(final SwabAbility swabAbility) {
        if (swabAbility.hasUsesLeft()) {
            if (cleanWaste(swabAbility.getAmount())) {
                swabAbility.used();
            }
        } else {
            swabAbility.getParent().accept(this);
        }
    }

    /**
     * @param amount Amount of waste to clean.
     * @return Whether cleaning the waste succeeded.
     */
    private boolean cleanWaste(final int amount) {
        final Optional<Integer> die = getPlayer().getHighestDie();
        // Here we need to check whether we have a die left, too! This was broken :(
        if (die.isEmpty()) {
            getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                    "No die left!");
            return false;
        }

        final Location location = getSurvivor().getLocation();
        if (location.equals(getModel().getColony()) && getModel().getTrashPile() != 0) {
            final int removedTrash = getModel().removeTrash(amount);
            getConnection().sendWasteChanged(-removedTrash);
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
            return true;
        }
        final int commId = getModel().getCommId(getPlayer().getId());
        getConnection().sendCommandFailed(commId, "Cannot remove cards from trash pile");
        return false;
    }

}
