package de.unisaarland.cs.se.sopra.cards;

public class Scissors extends Card {

    public Scissors(final int id) {
        super(id);
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

}
