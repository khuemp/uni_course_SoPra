package de.unisaarland.cs.se.sopra.abilities;

public class HammerAbility extends LimitedPassiveAbility {

    private static final int MAX_USES = 1;

    public HammerAbility(final Ability ability) {
        super(ability, MAX_USES);
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

}
