package de.unisaarland.cs.se.sopra.crossroad;

public abstract class Consequence {

    public abstract void accept(ConsequenceVisitor consequenceVisitor);
}
