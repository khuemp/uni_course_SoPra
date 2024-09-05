package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.abilities.Ability;
import de.unisaarland.cs.se.sopra.abilities.SearchVisitor;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;

public class SearchCommand extends InGameCommand {

    private final int survivorId;

    public SearchCommand(final int commId, final int survivorId) {
        super(commId);
        this.survivorId = survivorId;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        final int playerId = model.getPlayerID(super.getCommId());
        if (model.hasSurvivor(playerId, survivorId)) {
            final Optional<Survivor> optionalSurvivor = model.getSurvivor(survivorId);
            if (optionalSurvivor.isPresent()) {
                final Survivor survivor = optionalSurvivor.get();
                final SearchVisitor visitor = new SearchVisitor(model, connection,
                        model.getPlayer(playerId), survivor);
                final Ability ability = survivor.getAbility();
                ability.accept(visitor);
            }
        } else {
            connection.sendCommandFailed(super.getCommId(), "Wrong character!");
        }
    }
}
