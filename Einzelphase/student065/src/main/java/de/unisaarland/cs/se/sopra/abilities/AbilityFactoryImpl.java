package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.config.AbilityFactory;
import de.unisaarland.cs.se.sopra.config.ParamMap;
import de.unisaarland.cs.se.sopra.model.StatusEffect;
import java.util.Locale;

public class AbilityFactoryImpl implements AbilityFactory<Ability> {

    @Override
    public Ability createAbility(final String name, final ParamMap params) {
        return switch (name) {
            case "wound" -> {
                final String before = params.getString("before");
                final String after = params.getString("after");
                yield createWound(before, after);
            }
            case "heal" -> {
                yield createHeal();
            }
            case "feed" -> {
                final int numFood = params.getInt("numFood");
                yield createFeed(numFood);
            }
            case "barricade" -> {
                final int numBarricades = params.getInt("numBarricades");
                final int maxActivations = params.getInt("maxActivations");
                yield createBarricade(numBarricades, maxActivations);
            }
            case "kill" -> {
                final int numZombies = params.getInt("numZombies");
                final int dieValue = params.getInt("dieValue");
                final int locationId = params.getInt("locationId");
                final int maxActivations = params.getInt("maxActivations");
                final boolean children = params.getBoolean("children", false);
                final boolean infectionDie = params.getBoolean("infectionDie", false);
                yield createKill(
                        dieValue, numZombies, maxActivations, locationId, children, infectionDie);
            }
            case "no_infection" -> {
                yield createNoInfection();
            }
            case "search" -> {
                final int numCards = params.getInt("numCards");
                final int location = params.getInt("location");
                final int maxActivations = params.getInt("maxActivations");
                yield createSearch(numCards, location, maxActivations);
            }
            case "trash" -> {
                final int numCards = params.getInt("numCards");
                yield createTrash(numCards);
            }
            default -> {
                throw new IllegalArgumentException("Unknown ability. (%s)".formatted(name));
            }
        };
    }

    private Ability createNoInfection() {
        return new NoInfectionAbility();
    }

    private Ability createWound(final String before, final String after) {
        final StatusEffect statusBefore = StatusEffect.valueOf(before.toUpperCase(Locale.ENGLISH));
        final StatusEffect statusAfter = StatusEffect.valueOf(after.toUpperCase(Locale.ENGLISH));
        return new WoundAbility(statusBefore, statusAfter);
    }

    private Ability createHeal() {
        return new MedicineAbility();
    }

    private Ability createFeed(final int numFood) {
        return new FoodAbility(numFood);
    }

    private Ability createBarricade(final int numBarricades, final int maxActiviations) {
        return new BarricadeAbility(maxActiviations, numBarricades);
    }

    private Ability createKill(
            final int dieValue,
            final int numZombies,
            final int maxActivations,
            final int locationId,
            final boolean children,
            final boolean infectionDie) {
        return new KillAbility(
                maxActivations, dieValue, locationId, numZombies, children, infectionDie);
    }

    private Ability createSearch(
            final int numCards, final int locationId, final int maxActivations) {
        return new SearchAbility(locationId, maxActivations, numCards);
    }

    private Ability createTrash(final int numCards) {
        return new TrashAbility(numCards);
    }
}
