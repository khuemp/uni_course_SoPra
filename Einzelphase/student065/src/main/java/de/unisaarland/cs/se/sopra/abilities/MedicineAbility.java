package de.unisaarland.cs.se.sopra.abilities;

public class MedicineAbility extends LimitedAbility {

    public static final int MAX_USES = 1;

    public MedicineAbility() {
        super(MAX_USES);
    }

    @Override
    public void accept(final AbilityVisitor abilityVisitor) {
        abilityVisitor.visit(this);
    }
}
