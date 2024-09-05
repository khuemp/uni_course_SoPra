package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.model.StatusEffect;

public class WoundAbility extends Ability {

    private final StatusEffect replacee;
    private final StatusEffect replacement;

    public WoundAbility(final StatusEffect replacee, final StatusEffect replacement) {
        this.replacee = replacee;
        this.replacement = replacement;
    }

    public StatusEffect getReplacement() {
        return replacement;
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

    public StatusEffect getReplacee() {
        return replacee;
    }

}
