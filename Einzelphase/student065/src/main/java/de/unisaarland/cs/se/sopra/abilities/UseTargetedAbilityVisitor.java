package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadType;
import de.unisaarland.cs.se.sopra.model.Entrance;
import de.unisaarland.cs.se.sopra.model.Location;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.StatusEffect;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;

public class UseTargetedAbilityVisitor extends WoundingAbilityVisitor {

    private final int target;

    public UseTargetedAbilityVisitor(
            final Model model,
            final ConnectionWrapper connection,
            final Player player,
            final Survivor survivor,
            final int target) {
        super(model, connection, player, survivor);
        this.target = target;
    }

    @Override
    public void visit(final BaseAbility baseAbility) {
        getConnection()
                .sendCommandFailed(
                        getModel().getCommId(getPlayer().getId()),
                        "Cannot Actively use this characters ability!");
    }

    @Override
    public void visit(final FoodAbility foodAbility) {
        getConnection()
                .sendCommandFailed(
                        getModel().getCommId(getPlayer().getId()),
                        "This ability does not need target!");
    }

    @Override
    public void visit(final MedicineAbility medicineAbility) {
        if (medicineAbility.hasUsesLeft()) {
            final Optional<Survivor> optionalPatient = getModel().getSurvivor(target);
            if (optionalPatient.isPresent()) {
                attemptToHeal(medicineAbility, optionalPatient.get());
            } else {
                getConnection()
                        .sendCommandFailed(
                                getModel().getCommId(getPlayer().getId()),
                                "CharacterId does not exist.");
            }
        } else {
            getConnection()
                    .sendCommandFailed(getModel().getCommId(getPlayer().getId()), "No uses left!");
        }
    }


    @Override
    public void visit(final BarricadeAbility barricadeAbility) {
        if (barricadeAbility.hasUsesLeft()) {
            final Location location = getSurvivor().getLocation();
            final Optional<Entrance> optionalEntrance = location.getEntrance(this.target);
            if (optionalEntrance.isPresent()) {
                final Entrance entrance = optionalEntrance.get();
                final int capacity = entrance.getCapacityLeft();
                if (capacity > 0) {
                    final int amount = Math.min(capacity, barricadeAbility.getNumBarricades());
                    entrance.placeBarricades(amount);
                    getConnection().sendAbilityUsed(getSurvivor().getId(), target);
                    barricadeAbility.used();
                    checkCrossroadBarricade(location);
                } else {
                    getConnection()
                            .sendCommandFailed(
                                    getModel().getCommId(getPlayer().getId()), "No place left!");
                }
            } else {
                getConnection()
                        .sendCommandFailed(
                                getModel().getCommId(getPlayer().getId()),
                                "Entrance does not exist!");
            }
        } else {
            getConnection()
                    .sendCommandFailed(getModel().getCommId(getPlayer().getId()), "No uses left!");
        }
    }

    @Override
    public void visit(final KillAbility killAbility) {
        if (killAbility.hasUsesLeft()) {
            final Location location = getSurvivor().getLocation();
            if (location.getId() == killAbility.getLocationId()) {
                if (killAbility.isChildrenNeeded()
                        && getModel().getColony().getNumChildren() == 0) {
                    getConnection()
                            .sendCommandFailed(
                                    getModel().getCommId(getPlayer().getId()),
                                    "No children in colony!");
                    return;
                }
                final Optional<Entrance> optionalEntrance = location.getEntrance(this.target);
                if (optionalEntrance.isPresent()) {
                    killAtEntrance(killAbility, location, optionalEntrance.get());
                } else {
                    getConnection()
                            .sendCommandFailed(
                                    getModel().getCommId(getPlayer().getId()),
                                    "Entrance does not exist!");
                }
            } else {
                getConnection()
                        .sendCommandFailed(
                                getModel().getCommId(getPlayer().getId()), "Wrong location!");
            }
        } else {
            getConnection()
                    .sendCommandFailed(getModel().getCommId(getPlayer().getId()), "No uses left!");
        }
    }


    @Override
    public void visit(final WoundAbility woundAbility) {
        getConnection()
                .sendCommandFailed(
                        getModel().getCommId(getPlayer().getId()),
                        "Cannot Actively use this characters ability!");
    }

    @Override
    public void visit(final NoInfectionAbility noInfectionAbility) {
        getConnection()
                .sendCommandFailed(
                        getModel().getCommId(getPlayer().getId()),
                        "Cannot Actively use this characters ability!");
    }

    @Override
    public void visit(final SearchAbility searchAbility) {
        getConnection()
                .sendCommandFailed(
                        getModel().getCommId(getPlayer().getId()),
                        "Cannot Actively use this characters ability!");
    }

    @Override
    public void visit(final TrashAbility trashAbility) {
        getConnection()
                .sendCommandFailed(
                        getModel().getCommId(getPlayer().getId()),
                        "Cannot Actively use this characters ability!");
    }

    /**
     * This method heals a wounded survivor.
     *
     * @param medicineAbility Ability which is used to heal.
     * @param patient         Survivor to heal.
     */
    private void attemptToHeal(final MedicineAbility medicineAbility, final Survivor patient) {
        if (getSurvivor().getLocation().equals(patient.getLocation())
                && patient.isWounded()) {
            if (patient.getTimesFrostbitten() > 0) {
                patient.healFrostbite();
            } else {
                patient.heal();
            }
            medicineAbility.used();
            getConnection().sendAbilityUsed(getSurvivor().getId(), target);
        } else {
            getConnection()
                    .sendCommandFailed(
                            getModel().getCommId(getPlayer().getId()),
                            "Not wounded or not same location");
        }
    }

    /**
     * Kills zombies and removes a fitting die if needed.
     *
     * @param killAbility Ability which is used to kill.
     * @param location    Where to kill.
     * @param entrance    Specific entrance to attack.
     */
    private void killAtEntrance(final KillAbility killAbility, final Location location,
                                final Entrance entrance) {
        final int zombieCount = entrance.getZombieCount();
        if (zombieCount > 0) {
            final int dieValue = killAbility.getDieValue();
            final Optional<Integer> optionalHighestDie = getPlayer().getHighestDie();
            if (optionalHighestDie.isPresent()
                    && optionalHighestDie.get() >= dieValue) {
                getPlayer().removeDieLowerThan(dieValue);
            } else {
                getConnection()
                        .sendCommandFailed(
                                getModel().getCommId(getPlayer().getId()),
                                "No die left!");
                return;
            }
            final int amount = Math.min(zombieCount, killAbility.getNumZombies());
            entrance.removeZombies(amount);
            getConnection().sendAbilityUsed(getSurvivor().getId(), target);
            for (int i = 0; i < amount; i++) {
                getConnection()
                        .sendZombieKilled(
                                getSurvivor().getId(), location.getId(), target);
            }
            killAbility.used();
            if (killAbility.isRollInfectionDie()) {
                final StatusEffect wound = getModel().rollInfectionDie();
                dealWound(wound);
            }
        } else {
            getConnection()
                    .sendCommandFailed(
                            getModel().getCommId(getPlayer().getId()),
                            "Entrance does not have Zombies!");
        }
    }

    private void checkCrossroadBarricade(final Location location) {
        final Crossroad playerCrossroad;
        if ((playerCrossroad = getPlayer().getCrossroad()) != null) {
            if (playerCrossroad.getType() == CrossroadType.BARRICADED
                    && ((!playerCrossroad.hasLoc())
                    || (playerCrossroad.hasLoc() && playerCrossroad.getLocId()
                    == location.getId()))) {
                getPlayer().setCrossroadActivate(true);
            }
        }
    }
}
