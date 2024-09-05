package de.unisaarland.cs.se.sopra.model;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.EndGameState;

public class LocationsWithZombiesGoal implements Goal {

    private final int maxLocationWithZombies;

    public LocationsWithZombiesGoal(final int locationWithZombies) {
        this.maxLocationWithZombies = locationWithZombies;
    }

    @Override
    public void verify(final Model model, final ConnectionWrapper connection) {
        int locationWithZombies = 0;
        int zombiesAtColony = 0;
        for (final Entrance entrance : model.getColony().getEntrances()) {
            zombiesAtColony += entrance.getZombieCount();
        }
        if (zombiesAtColony > 0) {
            locationWithZombies++;
        }
        for (final Location location : model.getLocations()) {
            int zombiesAtLocation = 0;
            for (final Entrance entrance : location.getEntrances()) {
                zombiesAtLocation += entrance.getZombieCount();
            }
            if (zombiesAtLocation > 0) {
                locationWithZombies++;
            }
        }
        if (locationWithZombies <= maxLocationWithZombies) {
            connection.sendGameEnd(true);
            model.setGameState(EndGameState.getInstance());
        }
    }
}
