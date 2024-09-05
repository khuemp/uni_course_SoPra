package de.unisaarland.cs.se.sopra.abilities;

public class SwabAbility extends LimitedPassiveAbility {

    private final int amount;

    public SwabAbility(final Ability ability, final int uses, final int amount) {
        super(ability, uses);
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
