package de.unisaarland.cs.se.sopra.cards;

public class Fuel extends Card {

    public Fuel(final int id) {
        super(id);
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

}
