package de.unisaarland.cs.se.sopra.model;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.EndGameState;

public class BarricadesGoal implements Goal {

    private final int requiredBarricades;

    public BarricadesGoal(final int barricades) {
        this.requiredBarricades = barricades;
    }

    @Override
    public void verify(final Model model, final ConnectionWrapper connection) {
        int barricades = 0;
        for (final Entrance entrance : model.getColony().getEntrances()) {
            barricades += entrance.getBarricadeCount();
        }
        for (final Location location : model.getLocations()) {
            for (final Entrance entrance : location.getEntrances()) {
                barricades += entrance.getBarricadeCount();
            }
        }
        if (barricades >= requiredBarricades) {
            connection.sendGameEnd(true);
            model.setGameState(EndGameState.getInstance());
        }
    }
}
