package de.unisaarland.cs.se.sopra.cards;

public abstract class Card {

    private final int id;

    public Card(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract void accept(CardVisitor visitor);
}
