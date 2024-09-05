package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.PreGameState;
import de.unisaarland.cs.se.sopra.model.Model;

public class StartGameCommand extends AdministrativeCommand {

    public StartGameCommand(final int commId) {
        super(commId);
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        if (model.getPlayerByCommId(super.getCommId()) != null) {
            model.setGameState(PreGameState.getInstance());
        } else {
            connection.sendCommandFailed(super.getCommId(), "You are not registered!");
        }
    }

}
