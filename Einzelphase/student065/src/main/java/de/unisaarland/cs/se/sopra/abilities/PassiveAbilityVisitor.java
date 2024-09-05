package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;

public abstract class PassiveAbilityVisitor extends WoundingAbilityVisitor {

    public PassiveAbilityVisitor(final Model model, final ConnectionWrapper connection,
                                 final Player player,
                                 final Survivor survivor) {
        super(model, connection, player, survivor);
    }

    public abstract void executeStandardAction();

    @Override
    public void visit(final BaseAbility fuelAbility) {
        this.executeStandardAction();
    }

    @Override
    public void visit(final FoodAbility foodAbility) {
        this.executeStandardAction();
    }

    @Override
    public void visit(final MedicineAbility medicineAbility) {
        this.executeStandardAction();
    }

    @Override
    public void visit(final BarricadeAbility barricadeAbility) {
        this.executeStandardAction();
    }

    @Override
    public void visit(final KillAbility killAbility) {
        this.executeStandardAction();
    }

    @Override
    public void visit(final WoundAbility woundAbility) {
        this.executeStandardAction();
    }

    @Override
    public void visit(final NoInfectionAbility noInfectionAbility) {
        this.executeStandardAction();
    }

    @Override
    public void visit(final SearchAbility searchAbility) {
        this.executeStandardAction();
    }

    @Override
    public void visit(final TrashAbility trashAbility) {
        this.executeStandardAction();
    }
}
