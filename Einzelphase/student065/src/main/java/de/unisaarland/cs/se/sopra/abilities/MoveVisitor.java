package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.StatusEffect;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;

public class MoveVisitor extends PassiveAbilityVisitor {

    private final Optional<StatusEffect> wound;

    public MoveVisitor(final Model model, final ConnectionWrapper connection, final Player player,
                       final Survivor survivor,
                       final Optional<StatusEffect> optionalWound) {
        super(model, connection, player, survivor);
        this.wound = optionalWound;
    }

    @Override
    public void executeStandardAction() {
        wound.ifPresent(this::dealWound);
    }

    @Override
    public void visit(final WoundAbility woundAbility) {
        if (wound.isPresent()) {
            if (wound.get() == woundAbility.getReplacee()) {
                dealWound(woundAbility.getReplacement());
            } else {
                dealWound(wound.get());
            }
        }
    }

    @Override
    public void visit(final SnowbootsAbility snowbootsAbility) {
        if (wound.isPresent()) {
            if (wound.get() == StatusEffect.FROSTBITE) {
                getSurvivor().addWound();
                getConnection().sendWounded(getSurvivor().getId());
            } else {
                snowbootsAbility.getParent().accept(this);
            }
        }
    }

}
