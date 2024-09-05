package de.unisaarland.cs.se.sopra.cards;

public class Stuff extends Card {

    public Stuff(final int id) {
        super(id);
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

}
