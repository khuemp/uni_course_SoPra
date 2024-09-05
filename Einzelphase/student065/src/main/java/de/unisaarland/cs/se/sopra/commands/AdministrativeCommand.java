package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Model;

public abstract class AdministrativeCommand extends Command {

    public AdministrativeCommand(final int commId) {
        super(commId);
    }

    @Override
    public boolean checkPreCondition(final Model model, final ConnectionWrapper connection) {
        if (model.getGameState().canRegister()) {
            return true;
        } else {
            connection.sendCommandFailed(getCommId(), "Game has already started");
            return false;
        }

    }

}
