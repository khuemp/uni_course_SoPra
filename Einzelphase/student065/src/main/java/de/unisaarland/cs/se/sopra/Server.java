package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.model.Model;

public class Server {

    private final Model model;
    private final ConnectionWrapper connectionWrapper;

    public Server(final Model model, final ConnectionWrapper connectionWrapper) {
        this.model = model;
        this.connectionWrapper = connectionWrapper;
    }

    /**
     * Main game loop.
     */
    public void run() {
        while (model.getGameState().gameRunning()) {
            model.getGameState().run(model, connectionWrapper);
        }
    }


}
