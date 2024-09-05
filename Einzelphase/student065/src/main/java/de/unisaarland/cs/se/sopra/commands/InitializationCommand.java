package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.EndGameState;
import de.unisaarland.cs.se.sopra.model.Model;

public abstract class InitializationCommand extends Command {

    public InitializationCommand(final int commId) {
        super(commId);
    }

    @Override
    protected boolean checkPreCondition(final Model model, final ConnectionWrapper connection) {
        if (model.getGameState().canRegister()) {
            model.setGameState(EndGameState.getInstance());
            connection.sendRegistrationAborted();
            return false;
        }
        if (model.getGameState().inGame()) {
            connection.sendCommandFailed(getCommId(), "You cannot do this right now.");
            return false;
        }
        return true;
    }

}
