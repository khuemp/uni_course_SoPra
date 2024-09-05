package de.unisaarland.cs.se.sopra.cards;

public class Colt extends Card {

    public Colt(final int id) {
        super(id);
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

}
