package de.unisaarland.cs.se.sopra.model;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.EndGameState;

public class SurviveGoal implements Goal {

    @Override
    public void verify(final Model model, final ConnectionWrapper connection) {
        if (model.getRounds() <= 1) {
            connection.sendGameEnd(true);
            model.setGameState(EndGameState.getInstance());
        }
    }
}
