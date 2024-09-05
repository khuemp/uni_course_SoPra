package de.unisaarland.cs.se.sopra.cards;

public class Hammer extends Card {

    public Hammer(final int id) {
        super(id);
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

}
