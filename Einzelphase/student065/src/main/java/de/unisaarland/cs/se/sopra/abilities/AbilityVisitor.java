package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Model;

public abstract class AbilityVisitor {

    private final Model model;
    private final ConnectionWrapper connection;

    public AbilityVisitor(final Model model, final ConnectionWrapper connection) {
        this.model = model;
        this.connection = connection;
    }

    public Model getModel() {
        return model;
    }

    public ConnectionWrapper getConnection() {
        return connection;
    }

    public abstract void visit(BaseAbility fuelAbility);

    public abstract void visit(FoodAbility foodAbility);

    public abstract void visit(MedicineAbility medicineAbility);

    public abstract void visit(BarricadeAbility barricadeAbility);

    public abstract void visit(KillAbility killAbility);

    public abstract void visit(WoundAbility woundAbility);

    public abstract void visit(NoInfectionAbility noInfectionAbility);

    public abstract void visit(SearchAbility searchAbility);

    public abstract void visit(TrashAbility trashAbility);

    public void visit(final BlueprintAbility blueprintAbility) {
        blueprintAbility.getParent().accept(this);
    }

    public void visit(final SwabAbility swabAbility) {
        swabAbility.getParent().accept(this);
    }

    public void visit(final SnowbootsAbility snowbootsAbility) {
        snowbootsAbility.getParent().accept(this);
    }

    public void visit(final HammerAbility hammerAbility) {
        hammerAbility.getParent().accept(this);
    }

    public void visit(final ColtAbility coltAbility) {
        coltAbility.getParent().accept(this);
    }

}
