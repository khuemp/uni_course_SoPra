package de.unisaarland.cs.se.sopra.abilities;

public abstract class PassiveAbility extends Ability {

    private final Ability parent;

    public PassiveAbility(final Ability parent) {
        this.parent = parent;
    }

    public Ability getParent() {
        return parent;
    }

    @Override
    public void tick() {
        parent.tick();
    }

}
