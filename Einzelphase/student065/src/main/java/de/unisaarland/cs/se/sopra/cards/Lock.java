package de.unisaarland.cs.se.sopra.cards;

public class Lock extends Card {

    public Lock(final int id) {
        super(id);
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

}
