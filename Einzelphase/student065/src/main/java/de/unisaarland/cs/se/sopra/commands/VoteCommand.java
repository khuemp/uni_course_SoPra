package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.EndGameState;
import de.unisaarland.cs.se.sopra.model.Model;

public class VoteCommand extends Command {
    private final boolean vote;

    public VoteCommand(final int commId, final boolean vote) {
        super(commId);
        this.vote = vote;
    }

    @Override
    protected boolean checkPreCondition(final Model model, final ConnectionWrapper connection) {
        if (model.getGameState().canRegister()) {
            model.setGameState(EndGameState.getInstance());
            connection.sendRegistrationAborted();
            return false;
        }
        if (!model.isVotePhase() || !model.getGameState().inGame()) {
            connection.sendCommandFailed(getCommId(), "Wrong phase");
            return false;
        }
        if (!model.getCurrentVotePlayer().equals(model.getPlayerByCommId(super.getCommId()))) {
            connection.sendCommandFailed(getCommId(), "Not your turn to vote");
            return false;
        }
        return true;
    }

    @Override
    protected void run(final Model model, final ConnectionWrapper connection) {
        if (vote) {
            model.setNumVote1(model.getNumVote1() + 1);
        } else {
            model.setNumVote2(model.getNumVote2() + 1);
        }
        model.getPlayerByCommId(super.getCommId()).setHasVoted(true);
    }
}
