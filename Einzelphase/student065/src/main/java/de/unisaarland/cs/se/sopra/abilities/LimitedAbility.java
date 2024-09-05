package de.unisaarland.cs.se.sopra.abilities;

public abstract class LimitedAbility extends Ability {

    private final int maxUses;
    private int usesLeft;

    public LimitedAbility(final int maxUses) {
        this.maxUses = this.usesLeft = maxUses;
    }

    public boolean hasUsesLeft() {
        return usesLeft > 0;
    }

    public void used() {
        usesLeft--;
    }

    @Override
    public void tick() {
        this.usesLeft = maxUses;
    }
}
