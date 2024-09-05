package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.EndGameState;
import de.unisaarland.cs.se.sopra.model.Model;

public abstract class InGameCommand extends Command {

    public InGameCommand(final int commId) {
        super(commId);
    }

    @Override
    public boolean checkPreCondition(final Model model, final ConnectionWrapper connection) {
        if (model.getGameState().canRegister()) {
            model.setGameState(EndGameState.getInstance());
            connection.sendRegistrationAborted();
            return false;
        }
        if (!model.getGameState().inGame()) {
            connection.sendCommandFailed(getCommId(), "Wrong Phase");
            return false;
        }
        if (!model.getCurrentPlayer().equals(model.getPlayerByCommId(super.getCommId()))) {
            connection.sendCommandFailed(getCommId(),
                    String.format("Wrong Player! (%d),(%s)",
                            getCommId(), model.getPlayerByCommId(super.getCommId())));
            return false;
        }
        if (model.isVotePhase()) {
            connection.sendCommandFailed(getCommId(), "Vote Phase");
            return false;
        }
        return true;
    }

}
