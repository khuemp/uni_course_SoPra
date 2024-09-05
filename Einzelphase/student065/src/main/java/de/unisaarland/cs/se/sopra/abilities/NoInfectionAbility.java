package de.unisaarland.cs.se.sopra.abilities;

public class NoInfectionAbility extends Ability {

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

}
