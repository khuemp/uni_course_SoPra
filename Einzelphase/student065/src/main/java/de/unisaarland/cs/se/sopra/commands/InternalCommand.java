package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Model;

public abstract class InternalCommand extends Command {

    public InternalCommand(final int commId) {
        super(commId);
    }

    @Override
    protected boolean checkPreCondition(final Model model, final ConnectionWrapper connection) {
        return !model.isVotePhase();
    }
}
