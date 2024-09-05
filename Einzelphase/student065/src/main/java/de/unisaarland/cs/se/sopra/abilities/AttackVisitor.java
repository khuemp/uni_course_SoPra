package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Entrance;
import de.unisaarland.cs.se.sopra.model.Location;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.StatusEffect;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;

public class AttackVisitor extends PassiveAbilityVisitor {

    private final int entranceIdx;

    public AttackVisitor(final Model model, final ConnectionWrapper connection, final Player player,
                         final Survivor survivor, final int entrance) {
        super(model, connection, player, survivor);
        this.entranceIdx = entrance;
    }

    @Override
    public void executeStandardAction() {
        if (this.kill()) {
            final StatusEffect infection = super.getModel().rollInfectionDie();
            dealWound(infection);
        }
    }

    @Override
    public void visit(final ColtAbility coltAbility) {
        if (coltAbility.hasUsesLeft()) {
            if (this.kill()) {
                coltAbility.used();
            }
        } else {
            coltAbility.getParent().accept(this);
        }
    }

    @Override
    public void visit(final NoInfectionAbility noInfectionAbility) {
        kill();
    }

    /**
     * This method kills a zombie and removing a fitting die from the player.
     *
     * @return Whether the attack succeeded.
     */
    private boolean kill() {
        final int commId = getModel().getCommId(getPlayer().getId());
        final Optional<Integer> die = getPlayer().getHighestDie();
        if (die.isPresent()) {
            final int dieValue = die.get();
            if (dieValue < getSurvivor().getAttack()) {
                getConnection().sendCommandFailed(commId, "No action dice left.");
                return false;
            }
            final Location location = getSurvivor().getLocation();
            final Optional<Entrance> optionalEntrance = location.getEntrance(entranceIdx);
            if (optionalEntrance.isPresent()) {
                final Entrance entrance = optionalEntrance.get();
                if (entrance.getZombieCount() == 0) {
                    getConnection().sendCommandFailed(commId, "No zombie at location");
                    return false;
                }
                entrance.killZombie();
                getPlayer().removeDieLowerThan(getSurvivor().getAttack());
                getConnection().sendZombieKilled(getSurvivor().getId(), location.getId(),
                        entranceIdx);
                return true;
            } else {
                getConnection().sendCommandFailed(commId, "Entrance does not exist!");
                return false;
            }
        }
        getConnection().sendCommandFailed(commId, "No action dice left.");
        return false;
    }

}