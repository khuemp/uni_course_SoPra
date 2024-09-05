package de.unisaarland.cs.se.sopra.abilities;

public abstract class LimitedPassiveAbility extends PassiveAbility {

    private final int uses;
    private int usesLeft;

    public LimitedPassiveAbility(final Ability parent, final int uses) {
        super(parent);
        this.usesLeft = this.uses = uses;
    }

    public boolean hasUsesLeft() {
        return usesLeft > 0;
    }

    public void used() {
        usesLeft--;
    }

    @Override
    public void tick() {
        this.usesLeft = uses;
        getParent().tick();
    }

}
