package de.unisaarland.cs.se.sopra.crossroad;

public class FoodConsequence extends Consequence {

    private final int changeFood;

    public FoodConsequence(final int changeFood) {
        this.changeFood = changeFood;
    }

    @Override
    public void accept(final ConsequenceVisitor consequenceVisitor) {
        consequenceVisitor.visit(this);
    }

    public int getChangeFood() {
        return changeFood;
    }
}
