package de.unisaarland.cs.se.sopra.abilities;

public class ColtAbility extends LimitedPassiveAbility {

    private static final int MAX_USES = 1;

    public ColtAbility(final Ability ability) {
        super(ability, MAX_USES);
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

}
