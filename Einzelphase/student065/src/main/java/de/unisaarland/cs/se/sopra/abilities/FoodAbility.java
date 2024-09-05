package de.unisaarland.cs.se.sopra.abilities;

public class FoodAbility extends LimitedAbility {

    private static final int MAX_USES = 1;

    private final int numFood;

    public FoodAbility(final int numFood) {
        super(MAX_USES);
        this.numFood = numFood;
    }

    public int getNumFood() {
        return numFood;
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }
}
