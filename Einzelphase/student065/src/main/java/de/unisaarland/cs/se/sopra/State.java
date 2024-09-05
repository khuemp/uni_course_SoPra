package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.model.Model;

public abstract class State {

    public abstract void run(Model model, ConnectionWrapper connection);

    public boolean gameRunning() {
        return true;
    }

    public boolean canRegister() {
        return false;
    }

    public boolean inGame() {
        return false;
    }
}
