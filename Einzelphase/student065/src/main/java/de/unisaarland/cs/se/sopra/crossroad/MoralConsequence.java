package de.unisaarland.cs.se.sopra.crossroad;

public class MoralConsequence extends Consequence {

    private final int changeMoral;

    public MoralConsequence(final int changeMoral) {
        this.changeMoral = changeMoral;
    }

    @Override
    public void accept(final ConsequenceVisitor consequenceVisitor) {
        consequenceVisitor.visit(this);
    }

    public int getChangeMoral() {
        return changeMoral;
    }
}
