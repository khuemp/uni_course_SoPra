package de.unisaarland.cs.se.sopra.abilities;

public class TrashAbility extends Ability {

    private final int amount;

    public TrashAbility(final int amount) {
        this.amount = amount;
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

    public int getAmount() {
        return this.amount;
    }

}
