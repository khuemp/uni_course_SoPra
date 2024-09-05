package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Colony;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;

public class UseAbilityVisitor extends AbilityVisitor {

    private final Player player;
    private final Survivor survivor;

    public UseAbilityVisitor(final Model model, final ConnectionWrapper connection,
                             final Player player,
                             final Survivor survivor) {
        super(model, connection);
        this.player = player;
        this.survivor = survivor;
    }

    @Override
    public void visit(final BaseAbility baseAbility) {
        getConnection().sendCommandFailed(getModel().getCommId(player.getId()),
                "Cannot Actively use this characters ability!");
    }

    @Override
    public void visit(final FoodAbility foodAbility) {
        if (foodAbility.hasUsesLeft()) {
            final Colony colony = getModel().getColony();
            colony.addFood(foodAbility.getNumFood());
            getConnection().sendAbilityUsed(survivor.getId());
            foodAbility.used();
        } else {
            getConnection().sendCommandFailed(getModel().getCommId(player.getId()),
                    "No uses left!");
        }
    }

    @Override
    public void visit(final MedicineAbility medicineAbility) {
        getConnection().sendCommandFailed(getModel().getCommId(player.getId()),
                "Ability needs target!");
    }

    @Override
    public void visit(final BarricadeAbility barricadeAbility) {
        getConnection().sendCommandFailed(getModel().getCommId(player.getId()),
                "Ability needs target!");
    }

    @Override
    public void visit(final KillAbility killAbility) {
        getConnection().sendCommandFailed(getModel().getCommId(player.getId()),
                "Ability needs target!");
    }

    @Override
    public void visit(final WoundAbility woundAbility) {
        getConnection().sendCommandFailed(getModel().getCommId(player.getId()),
                "Cannot Actively use this characters ability!");
    }

    @Override
    public void visit(final NoInfectionAbility noInfectionAbility) {
        getConnection().sendCommandFailed(getModel().getCommId(player.getId()),
                "Cannot Actively use this characters ability!");
    }

    @Override
    public void visit(final SearchAbility searchAbility) {
        getConnection().sendCommandFailed(getModel().getCommId(player.getId()),
                "Cannot Actively use this characters ability!");
    }

    @Override
    public void visit(final TrashAbility trashAbility) {
        getConnection().sendCommandFailed(getModel().getCommId(player.getId()),
                "Cannot Actively use this characters ability!");
    }

}
