package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.abilities.Ability;
import de.unisaarland.cs.se.sopra.abilities.AttackVisitor;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;

public class AttackCommand extends InGameCommand {

    private final int survivorId;
    private final int entrance;

    public AttackCommand(final int commId, final int survivorId, final int entrance) {
        super(commId);
        this.survivorId = survivorId;
        this.entrance = entrance;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        final int playerId = model.getPlayerID(super.getCommId());
        if (model.hasSurvivor(playerId, survivorId)) {
            final Optional<Survivor> optionalSurvivor = model.getSurvivor(survivorId);
            if (optionalSurvivor.isPresent()) {
                final Survivor survivor = optionalSurvivor.get();
                final AttackVisitor visitor = new AttackVisitor(model, connection,
                        model.getPlayer(playerId), survivor, entrance);
                final Ability ability = survivor.getAbility();
                ability.accept(visitor);
            }
        } else {
            connection.sendCommandFailed(super.getCommId(), "Wrong character!");
        }
    }

}
