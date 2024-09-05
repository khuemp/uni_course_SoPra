package de.unisaarland.cs.se.sopra.cards;

public class Medicine extends Card {

    public Medicine(final int id) {
        super(id);
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

}
