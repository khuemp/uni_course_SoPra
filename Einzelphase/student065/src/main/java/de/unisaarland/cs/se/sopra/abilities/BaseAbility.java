package de.unisaarland.cs.se.sopra.abilities;

public class BaseAbility extends Ability {

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

}
