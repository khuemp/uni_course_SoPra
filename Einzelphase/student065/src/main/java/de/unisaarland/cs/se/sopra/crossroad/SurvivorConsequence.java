package de.unisaarland.cs.se.sopra.crossroad;

public class SurvivorConsequence extends Consequence {

    private final int spawnSurvivors;
    private final boolean children;

    public SurvivorConsequence(final int spawnSurvivors, final boolean children) {
        this.spawnSurvivors = spawnSurvivors;
        this.children = children;
    }

    @Override
    public void accept(final ConsequenceVisitor consequenceVisitor) {
        consequenceVisitor.visit(this);
    }

    public int getSpawnSurvivors() {
        return spawnSurvivors;
    }

    public boolean hasChildren() {
        return children;
    }
}
