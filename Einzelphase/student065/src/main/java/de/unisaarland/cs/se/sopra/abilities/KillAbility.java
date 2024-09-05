package de.unisaarland.cs.se.sopra.abilities;

public class KillAbility extends LimitedAbility {

    private final int dieValue;
    private final int locationId;
    private final int numZombies;
    private final boolean rollInfectionDie;
    private final boolean childrenNeeded;

    public KillAbility(
            final int maxUses,
            final int dieValue,
            final int locationId,
            final int numZombies,
            final boolean rollInfectionDie,
            final boolean childrenNeeded) {
        super(maxUses);
        this.dieValue = dieValue;
        this.locationId = locationId;
        this.numZombies = numZombies;
        this.childrenNeeded = rollInfectionDie;
        this.rollInfectionDie = childrenNeeded;
    }

    public boolean isChildrenNeeded() {
        return childrenNeeded;
    }

    public boolean isRollInfectionDie() {
        return rollInfectionDie;
    }

    public int getNumZombies() {
        return numZombies;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getDieValue() {
        return dieValue;
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }
}
