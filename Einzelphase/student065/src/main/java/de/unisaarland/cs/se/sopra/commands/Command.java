package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Model;

public abstract class Command {

    private final int commId;

    public Command(final int commId) {
        this.commId = commId;
    }

    protected abstract boolean checkPreCondition(Model model, ConnectionWrapper connection);

    protected abstract void run(Model model, ConnectionWrapper connection);

    public void execute(final Model model, final ConnectionWrapper connection) {
        if (checkPreCondition(model, connection)) {
            run(model, connection);
        }
    }

    public boolean checkPlayer(final int commId) {
        return this.commId == commId;
    }

    public int getCommId() {
        return commId;
    }
}
