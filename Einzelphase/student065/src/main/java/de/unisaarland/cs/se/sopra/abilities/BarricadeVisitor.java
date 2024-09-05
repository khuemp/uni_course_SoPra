package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadType;
import de.unisaarland.cs.se.sopra.model.Entrance;
import de.unisaarland.cs.se.sopra.model.Location;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;

public class BarricadeVisitor extends PassiveAbilityVisitor {

    private static final int BARRICADE_COUNT = 1;
    private final int entranceIdx;

    public BarricadeVisitor(final Model model, final ConnectionWrapper connection,
                            final Player player,
                            final Survivor survivor, final int entrance) {
        super(model, connection, player, survivor);
        this.entranceIdx = entrance;
    }

    @Override
    public void executeStandardAction() {
        final Optional<Integer> die = getPlayer().getHighestDie();
        if (die.isPresent()) {
            if (barricade()) {
                getPlayer().removeLowestDie();
            }
        } else {
            getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                    "No die left!");
        }
    }

    @Override
    public void visit(final HammerAbility hammerAbility) {
        if (hammerAbility.hasUsesLeft()) {
            if (this.barricade()) {
                hammerAbility.used();
            }
        } else {
            hammerAbility.getParent().accept(this);
        }
    }

    /**
     * Places a barricade at the given entrance.
     *
     * @return Whether placing the barricade succeeded.
     */
    private boolean barricade() {
        final int commId = getModel().getCommId(getPlayer().getId());
        final Location location = getSurvivor().getLocation();
        final Optional<Entrance> optionalEntrance = location.getEntrance(entranceIdx);
        if (optionalEntrance.isPresent()) {
            final Entrance entrance = optionalEntrance.get();
            if (entrance.getCapacityLeft() == 0) {
                getConnection().sendCommandFailed(commId, "No space left!");
                return false;
            }
            entrance.placeBarricades(BARRICADE_COUNT);
            getConnection().sendBarricaded(getSurvivor().getId(), location.getId(),
                    entranceIdx);
            checkCrossroadBarricade(location);
            return true;
        } else {
            getConnection().sendCommandFailed(commId, "Entrance does not exist!");
            return false;
        }
    }

    private void checkCrossroadBarricade(final Location location) {
        final Crossroad playerCrossroad;
        if ((playerCrossroad = getPlayer().getCrossroad()) != null) {
            if (playerCrossroad.getType() == CrossroadType.BARRICADED
                    && ((!playerCrossroad.hasLoc())
                    || (playerCrossroad.hasLoc() && playerCrossroad.getLocId()
                    == location.getId()))) {
                getPlayer().setCrossroadActivate(true);
            }
        }
    }
}
