package de.unisaarland.cs.se.sopra.abilities;

public class BarricadeAbility extends LimitedAbility {

    private final int numBarricades;

    public BarricadeAbility(final int maxUses, final int numBarricades) {
        super(maxUses);
        this.numBarricades = numBarricades;
    }

    public int getNumBarricades() {
        return numBarricades;
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }

}
