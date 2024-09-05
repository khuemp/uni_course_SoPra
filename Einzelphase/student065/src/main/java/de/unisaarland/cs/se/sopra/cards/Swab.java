package de.unisaarland.cs.se.sopra.cards;

public class Swab extends Card {

    private static final int SWAB_AMOUNT = 5;
    private static final int SWAB_USES = 1;

    public Swab(final int id) {
        super(id);
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

    public int getUses() {
        return SWAB_USES;
    }

    public int getAmount() {
        return SWAB_AMOUNT;
    }
}
