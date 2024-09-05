package de.unisaarland.cs.se.sopra.abilities;

public class SnowbootsAbility extends PassiveAbility {

    public SnowbootsAbility(final Ability ability) {
        super(ability);
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

}
