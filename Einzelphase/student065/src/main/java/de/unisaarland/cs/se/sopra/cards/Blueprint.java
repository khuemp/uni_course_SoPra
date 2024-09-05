package de.unisaarland.cs.se.sopra.cards;

public class Blueprint extends Card {

    private final int locationId;

    public Blueprint(final int id, final int locationId) {
        super(id);
        this.locationId = locationId;
    }

    @Override
    public void accept(final CardVisitor visitor) {
        visitor.visit(this);
    }

    public int getLocationId() {
        return this.locationId;
    }

}
