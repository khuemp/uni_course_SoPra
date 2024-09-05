package de.unisaarland.cs.se.sopra.abilities;

public class BlueprintAbility extends LimitedPassiveAbility {

    private static final int MAX_USES = 1;
    private static final int NUM_CARDS = 1;

    private final int locationId;

    public BlueprintAbility(final Ability ability, final int locationId) {
        super(ability, MAX_USES);
        this.locationId = locationId;
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

    public int getNumCards() {
        return NUM_CARDS;
    }

    public int getLocation() {
        return this.locationId;
    }

}
