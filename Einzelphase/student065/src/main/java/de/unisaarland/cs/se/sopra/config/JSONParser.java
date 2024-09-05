package de.unisaarland.cs.se.sopra.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONParser {

    private JSONParser() {
    }

    /**
     * Parses configuration file to create a game model. Generically
     * typed such that you can reuse this parser to create
     * objects of your own class structure.
     *
     * @param mapPath      The file path of the configuration.
     * @param modelBuilder Creates and stores class objects from the json.
     * @param <M>          Generic type of model to build.
     * @return Game model.
     * @throws IOException If problems occur while reading the configuration file.
     */
    public static <M> M parse(final Path mapPath, final ModelBuilder<M> modelBuilder)
            throws IOException {
        final JSONObject json =
                new JSONObject(
                        Objects.requireNonNull(Files.readString(mapPath, StandardCharsets.UTF_8)));

        final int maxPlayers = json.getInt("maxPlayers");
        modelBuilder.setMaxPlayers(maxPlayers);
        modelBuilder.setConfigPath(mapPath);

        parseGoal(json, modelBuilder);
        parseCards(json, modelBuilder);
        parseLocations(json, modelBuilder);
        parseCharacters(json, modelBuilder);
        parseCrises(json, modelBuilder);
        parseCrossroad(json, modelBuilder);
        return modelBuilder.build();
    }

    /**
     * @param json         Json object of the colony.
     * @param modelBuilder The builder.
     * @param <M>          Generic type of model.
     */
    private static <M> void parseColony(final JSONObject json, final ModelBuilder<M> modelBuilder) {
        final int entrances = json.getInt("entrances");
        final int identifier = json.getInt("identifier");
        final JSONArray startCards = json.getJSONArray("startCards");
        final List<Integer> cardIds = new ArrayList<>(startCards.length());
        for (int j = 0; j < startCards.length(); j++) {
            final int cardId = startCards.getInt(j);
            cardIds.add(cardId);
        }
        modelBuilder.addColony(identifier, entrances, cardIds);
    }

    /**
     * @param json         Json object of the location.
     * @param modelBuilder The builder.
     * @param <M>          Generic type of model.
     */
    private static <M> void parseLocation(
            final JSONObject json, final ModelBuilder<M> modelBuilder) {
        final String name = json.getString("name");
        final int identifier = json.getInt("identifier");
        final int entrances = json.getInt("entrances");
        final int survivorSpaces = json.getInt("survivorSpaces");
        final JSONArray cardsJSON = json.getJSONArray("cards");
        final List<Integer> cardIds = new ArrayList<>(cardsJSON.length());
        for (int j = 0; j < cardsJSON.length(); j++) {
            final int cardId = cardsJSON.getInt(j);
            cardIds.add(cardId);
        }
        modelBuilder.addLocation(identifier, name, entrances, cardIds, survivorSpaces);
    }

    /**
     * @param json         Json object of all locations.
     * @param modelBuilder The builder.
     * @param <M>          Generic type of model.
     */
    private static <M> void parseLocations(
            final JSONObject json, final ModelBuilder<M> modelBuilder) {
        final JSONArray locations = json.getJSONArray("locations");
        for (int i = 0; i < locations.length(); i++) {
            final JSONObject locationJSON = locations.getJSONObject(i);
            if (locationJSON.has("colony")) {
                final JSONObject colonyJSON = locationJSON.getJSONObject("colony");
                parseColony(colonyJSON, modelBuilder);
            } else {
                parseLocation(locationJSON, modelBuilder);
            }
        }
    }

    /**
     * @param json         Json object of all crises cards.
     * @param modelBuilder The builder.
     * @param <M>          Generic type of model.
     */
    private static <M> void parseCrises(final JSONObject json, final ModelBuilder<M> modelBuilder) {
        final JSONArray crises = json.getJSONArray("crises");
        for (int i = 0; i < crises.length(); i++) {
            final JSONObject crisisJSON = crises.getJSONObject(i);
            final String crisisType = crisisJSON.keys().next();
            final JSONObject properties = crisisJSON.getJSONObject(crisisType);
            final int identifier = properties.getInt("identifier");
            final int moralChange = properties.getInt("moralChange");
            final int requiredCards = properties.getInt("requiredCards");
            modelBuilder.addCrisis(identifier, crisisType, moralChange, requiredCards);
        }
    }

    /**
     * @param json         Json object of the goal of the game.
     * @param modelBuilder The builder.
     * @param <M>          Generic type of model.
     */
    private static <M> void parseGoal(final JSONObject json, final ModelBuilder<M> modelBuilder) {
        final JSONObject goalJSON = json.getJSONObject("goal");
        final int moral = goalJSON.getInt("moral");
        final int rounds = goalJSON.getInt("rounds");
        final int zombiesColony = goalJSON.getInt("zombiesColony");
        final int zombiesLocations = goalJSON.getInt("zombiesLocations");
        final int childrenInColony = goalJSON.getInt("childrenInColony");
        final Optional<Integer> optLocationWithZombies =
                getOptionalInt(goalJSON, "locationWithZombies");
        final Optional<Integer> optBarricades = getOptionalInt(goalJSON, "barricades");
        final Optional<Boolean> optSurvive = getOptionalBool(goalJSON, "survive");
        modelBuilder.setChildrenInColony(childrenInColony);
        modelBuilder.setZombiesColony(zombiesColony);
        modelBuilder.setZombiesLocations(zombiesLocations);
        modelBuilder.setMoral(moral);
        modelBuilder.setRounds(rounds);
        modelBuilder.addGoal(optLocationWithZombies, optBarricades, optSurvive);
    }

    /**
     * @param json         Json object of all cards.
     * @param modelBuilder The builder.
     * @param <M>          Generic type of model.
     */
    private static <M> void parseCards(final JSONObject json, final ModelBuilder<M> modelBuilder) {
        final JSONArray cards = json.getJSONArray("cards");
        for (int i = 0; i < cards.length(); i++) {
            final JSONObject card = cards.getJSONObject(i);
            final String cardType = card.keys().next();
            final JSONObject properties = card.getJSONObject(cardType);
            final int identifier = properties.getInt("identifier");
            final ParamMap params = new JSONParaMap(properties);
            modelBuilder.addCard(identifier, cardType, params);
        }
    }

    /**
     * @param json         Json object of all survivors.
     * @param modelBuilder The builder.
     * @param <M>          Generic type of model.
     */
    private static <M> void parseCharacters(
            final JSONObject json, final ModelBuilder<M> modelBuilder) {
        final JSONArray characters = json.getJSONArray("characters");
        for (int i = 0; i < characters.length(); i++) {
            final JSONObject character = characters.getJSONObject(i);
            final String name = character.getString("name");
            final int identifier = character.getInt("identifier");
            final int status = character.getInt("status");
            final int attack = character.getInt("attack");
            final int search = character.getInt("search");
            final JSONObject ability = character.getJSONObject("ability");
            final String abilityType = ability.keys().next();
            final JSONObject properties = ability.getJSONObject(abilityType);
            final ParamMap paraMap = new JSONParaMap(properties);
            modelBuilder.addSurvivor(
                    identifier, name, attack, search, status, abilityType, paraMap);
        }
    }

    private static <M> void parseCrossroad(final JSONObject json,
                                           final ModelBuilder<M> modelBuilder) {
        final JSONArray crossroads = json.getJSONArray("crossroads");
        for (int i = 0; i < crossroads.length(); i++) {
            final JSONObject crossroad = crossroads.getJSONObject(i);
            final String crossroadType = crossroad.keys().next();
            final JSONObject crossroadProperties = crossroad.getJSONObject(crossroadType);
            final ParamMap crossroadParams = new JSONParaMap(crossroadProperties);
            final int identifier = crossroadProperties.getInt("identifier");
            modelBuilder.addCrossroad(identifier, crossroadType, crossroadParams,
                    crossroadProperties);
        }
    }

    /**
     * @param json Json object which might have the given key.
     * @param key  The key.
     * @return An optional integer.
     */
    private static Optional<Integer> getOptionalInt(final JSONObject json, final String key) {
        Optional<Integer> value = Optional.empty();
        if (json.has(key)) {
            value = Optional.of(json.getInt(key));
        }
        return value;
    }

    /**
     * @param json Json object which might have the given key.
     * @param key  The key.
     * @return An optional boolean.
     */
    private static Optional<Boolean> getOptionalBool(final JSONObject json, final String key) {
        Optional<Boolean> value = Optional.empty();
        if (json.has(key)) {
            value = Optional.of(json.getBoolean(key));
        }
        return value;
    }

    /**
     * Class to capsule json object.
     */
    public static final class JSONParaMap implements ParamMap {

        private final JSONObject json;

        public JSONParaMap(final JSONObject json) {
            this.json = json;
        }

        @Override
        public int getInt(final String key) {
            return json.getInt(key);
        }

        @Override
        public String getString(final String key) {
            return json.getString(key);
        }

        @Override
        public boolean getBoolean(final String name) {
            return json.getBoolean(name);
        }

        @Override
        public boolean getBoolean(final String key, final boolean defaultValue) {
            return json.has(key) ? getBoolean(key) : defaultValue;
        }
    }
}
