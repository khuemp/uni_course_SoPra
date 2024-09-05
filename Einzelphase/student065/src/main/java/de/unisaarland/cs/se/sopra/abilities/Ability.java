package de.unisaarland.cs.se.sopra.abilities;

public abstract class Ability {

    public abstract void accept(AbilityVisitor visitor);

    public void tick() {
        //empty
    }

}
