package de.unisaarland.cs.se.sopra.crossroad;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class ConsequenceFactoryImpl implements ConsequenceFactory<Consequence> {

    @Override
    public List<Consequence> createConsequence(final String type, final JSONObject consequence) {
        return switch (type) {

            case "changeFood", "changeMoral", "spawnSurvivors", "spawnZombies" -> {
                final List<Consequence> consequences = new ArrayList<>();
                consequences.add(this.createConcreteConsequence(type, consequence));
                yield consequences;
            }

            case "choice" -> {
                final JSONObject consequenceProperties = consequence.getJSONObject(type);
                final JSONObject consequence1 = consequenceProperties.getJSONObject("consequence1");
                final String type1 = consequence1.keys().next();
                final Consequence con1 = this.createConcreteConsequence(type1, consequence1);
                final JSONObject consequence2 = consequenceProperties.getJSONObject("consequence2");
                final String type2 = consequence2.keys().next();
                final Consequence con2 = this.createConcreteConsequence(type2, consequence2);
                final List<Consequence> consequences = new ArrayList<>();
                consequences.add(con1);
                consequences.add(con2);
                yield consequences;
            }

            default -> throw new IllegalArgumentException(
                    "Unknown consequence: %s".formatted(type));
        };
    }

    private Consequence createConcreteConsequence(final String type, final JSONObject consequence) {
        return switch (type) {

            case "changeFood" -> {
                final JSONObject consequenceProperties = consequence.getJSONObject(type);
                final int numFood = consequenceProperties.getInt("amount");
                yield new FoodConsequence(numFood);
            }

            case "changeMoral" -> {
                final JSONObject consequenceProperties = consequence.getJSONObject(type);
                final int numMoral = consequenceProperties.getInt("amount");
                yield new MoralConsequence(numMoral);
            }

            case "spawnSurvivors" -> {
                final JSONObject consequenceProperties = consequence.getJSONObject(type);
                final int numSurvivors = consequenceProperties.getInt("amount");
                try {
                    final boolean hasChildren = consequenceProperties.getBoolean("children");
                    yield new SurvivorConsequence(numSurvivors, hasChildren);
                } catch (final JSONException e) {
                    yield new SurvivorConsequence(numSurvivors, false);
                }
            }

            case "spawnZombies" -> {
                final JSONObject consequenceProperties = consequence.getJSONObject(type);
                final int numZombies = consequenceProperties.getInt("amount");
                try {
                    final int locId = consequenceProperties.getInt("locationId");
                    yield new ZombieConsequence(numZombies, locId);
                } catch (final JSONException e) {
                    yield new ZombieNoLocConsequence(numZombies);
                }
            }

            default -> {
                throw new IllegalArgumentException(
                        "Unknown consequence: %s".formatted(type));
            }
        };
    }
}
