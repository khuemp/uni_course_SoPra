package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.model.Model;

public final class EndGameState extends State {

    private static EndGameState instance;

    private EndGameState() {
        // Do nothing
    }

    public static EndGameState getInstance() {
        if (instance == null) {
            instance = new EndGameState();
        }
        return instance;
    }

    @Override
    public boolean gameRunning() {
        return false;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        // Do nothing
    }
}
