package de.unisaarland.cs.se.sopra.cards;

public class Food extends Card {

    private final int food;

    public Food(final int id, final int food) {
        super(id);
        this.food = food;
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

    public int getFood() {
        return food;
    }
}
