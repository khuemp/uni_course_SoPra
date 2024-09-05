package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.abilities.Ability;
import de.unisaarland.cs.se.sopra.abilities.MoveVisitor;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadType;
import de.unisaarland.cs.se.sopra.model.Location;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.StatusEffect;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;

public class MoveCommand extends InGameCommand {

    private final int survivorId;
    private final int locationId;

    public MoveCommand(final int commId, final int survivorId, final int locationId) {
        super(commId);
        this.survivorId = survivorId;
        this.locationId = locationId;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        final int playerId = model.getPlayerID(super.getCommId());
        if (model.hasSurvivor(playerId, survivorId)) {
            final Optional<Survivor> optionalSurvivor = model.getSurvivor(survivorId);
            if (optionalSurvivor.isPresent()) {
                final Survivor survivor = optionalSurvivor.get();
                if (survivor.hasMoved()) {
                    connection.sendCommandFailed(super.getCommId(), "Cannot move anymore!");
                } else if (survivor.getLocation().getId() == locationId) {
                    connection.sendCommandFailed(super.getCommId(), "Already there!");
                } else {
                    final Optional<StatusEffect> wound = move(model, connection, survivor);
                    final MoveVisitor visitor = new MoveVisitor(model, connection,
                            model.getPlayer(playerId), survivor, wound);
                    final Ability ability = survivor.getAbility();
                    ability.accept(visitor);
                }
            }
        } else {
            connection.sendCommandFailed(super.getCommId(), "Wrong command!");
        }
    }

    /**
     * Moves a survivor to the destination location if possible
     * and yields a status effect iff successful.
     *
     * @param model      Game model inside which the survivor gets moved.
     * @param connection To inform all players.
     * @param survivor   Survivor to move.
     * @return The status effect the survivor received while moving.
     */
    private Optional<StatusEffect> move(final Model model, final ConnectionWrapper connection,
                                        final Survivor survivor) {
        final Optional<Location> optDestination = model.getLocation(this.locationId);
        if (optDestination.isPresent()) {
            final Location destination = optDestination.get();
            if (destination.equals(survivor.getLocation())) {
                connection.sendCommandFailed(super.getCommId(), "You are already here");
            } else if (destination.hasPlacesLeft()) {
                model.moveSurvivor(survivor, destination);
                survivor.moved();
                connection.sendMoved(survivorId, locationId);
                final Crossroad playerCrossroad;
                if ((playerCrossroad = model.getPlayerByCommId(super.getCommId()).getCrossroad())
                        != null) {
                    if (playerCrossroad.getType() == CrossroadType.MOVED
                            && !(playerCrossroad.hasLoc()
                                    && (playerCrossroad.getLocId() != destination.getId()))) {
                        model.getPlayerByCommId(super.getCommId()).setCrossroadActivate(true);
                    }
                }
                return Optional.of(model.rollInfectionDie());
            } else {
                connection.sendCommandFailed(super.getCommId(), "No spaces left!");
            }
        } else {
            connection.sendCommandFailed(super.getCommId(), "Invalid Argument! locationId");
        }
        return Optional.empty();
    }

}
