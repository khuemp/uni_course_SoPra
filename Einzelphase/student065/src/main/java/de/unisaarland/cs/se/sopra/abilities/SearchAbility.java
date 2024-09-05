package de.unisaarland.cs.se.sopra.abilities;

public class SearchAbility extends LimitedAbility {

    private final int numCards;
    private final int locationId;

    public SearchAbility(final int locationId, final int maxUses, final int numCards) {
        super(maxUses);
        this.numCards = numCards;
        this.locationId = locationId;
    }

    public int getNumCards() {
        return numCards;
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

    public int getLocation() {
        return this.locationId;
    }

}
