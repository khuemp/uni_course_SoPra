package de.unisaarland.cs.se.sopra.cards;

public class Snowboots extends Card {

    public Snowboots(final int id) {
        super(id);
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

}
