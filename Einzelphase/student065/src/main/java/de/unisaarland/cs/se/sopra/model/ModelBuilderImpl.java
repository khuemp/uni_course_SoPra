package de.unisaarland.cs.se.sopra.model;

import de.unisaarland.cs.se.sopra.abilities.Ability;
import de.unisaarland.cs.se.sopra.cards.Card;
import de.unisaarland.cs.se.sopra.config.AbilityFactory;
import de.unisaarland.cs.se.sopra.config.CardFactory;
import de.unisaarland.cs.se.sopra.config.ModelBuilder;
import de.unisaarland.cs.se.sopra.config.ParamMap;
import de.unisaarland.cs.se.sopra.crossroad.Consequence;
import de.unisaarland.cs.se.sopra.crossroad.ConsequenceFactory;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class ModelBuilderImpl implements ModelBuilder<Model> {

    public final CardFactory<Card> cardFactory;
    public final AbilityFactory<Ability> abilityFactory;
    public final CrossroadFactory<Crossroad> crossroadFactory;
    public final ConsequenceFactory<Consequence> consequenceFactory;

    private final Map<Integer, Card> cardMap;
    private final List<Survivor> survivors;
    private final List<Location> locations;
    private final List<Crisis> crises;
    private final List<Crossroad> crossroads;

    private Colony colony;
    private RandomManager randomManager;
    private Goal goal;
    private Path configPath;

    private int maxPlayers;
    private int zombiesLocations;
    private int zombiesColony;
    private int childrenInColony;
    private int moral;
    private int rounds;

    public ModelBuilderImpl(
            final CardFactory<Card> cardFactory, final AbilityFactory<Ability> abilityFactory, final
                    CrossroadFactory<Crossroad> crossroadFactory,
            final ConsequenceFactory<Consequence> consequenceFactory) {

        cardMap = new HashMap<>();
        survivors = new ArrayList<>();
        locations = new ArrayList<>();
        crises = new ArrayList<>();
        crossroads = new ArrayList<>();

        this.cardFactory = cardFactory;
        this.abilityFactory = abilityFactory;
        this.crossroadFactory = crossroadFactory;
        this.consequenceFactory = consequenceFactory;
    }

    @Override
    public Model build() {
        locations.sort(Comparator.comparingInt(Location::getId));
        return new Model(
                colony,
                locations,
                randomManager,
                survivors,
                crises,
                crossroads,
                goal,
                configPath,
                maxPlayers,
                moral,
                rounds);
    }

    @Override
    public void addColony(final int id, final int entrances, final List<Integer> cardIds) {
        final List<Entrance> colonyEntrances = createEntrances(entrances, zombiesColony);
        final List<Card> cards = getCardList(cardIds);
        this.colony = new Colony(id, colonyEntrances, cards, childrenInColony);
    }

    @Override
    public void addLocation(
            final int id,
            final String name,
            final int entrances,
            final List<Integer> cardIds,
            final int survivorSpaces) {
        final List<Entrance> locationEntrances = createEntrances(entrances, zombiesLocations);
        final List<Card> cards = getCardList(cardIds);
        this.locations.add(new Location(id, locationEntrances, cards, survivorSpaces));
    }

    @Override
    public void addSurvivor(
            final int id,
            final String name,
            final int attack,
            final int search,
            final int status,
            final String abilityName,
            final ParamMap abilityParams) {
        final Ability ability = abilityFactory.createAbility(abilityName, abilityParams);
        final Survivor survivor = new Survivor(id, name, attack, search, status, colony, ability);
        survivors.add(survivor);
    }

    @Override
    public void addCrisis(
            final int id, final String type, final int moralChange, final int requiredCards) {
        final CrisisType crisisType = CrisisType.valueOf(type.toUpperCase(Locale.ENGLISH));
        final Crisis crisis = new Crisis(id, crisisType, moralChange, requiredCards);
        crises.add(crisis);
    }

    @Override
    public void addCard(final int id, final String name, final ParamMap param) {
        final Card card = cardFactory.createCard(id, name, param);
        cardMap.put(id, card);
    }

    @Override
    public void addGoal(
            final Optional<Integer> locationWithZombies,
            final Optional<Integer> barricades,
            final Optional<Boolean> survive) {
        if (locationWithZombies.isPresent()) {
            this.goal = new LocationsWithZombiesGoal(locationWithZombies.get());
        } else if (barricades.isPresent()) {
            this.goal = new BarricadesGoal(barricades.get());
        } else if (survive.isPresent()) {
            this.goal = new SurviveGoal();
        }
    }

    @Override
    public void addCrossroad(final int id, final String type, final ParamMap crossroadParams,
                             final JSONObject crossroadProperties) {
        final JSONObject consequence = crossroadProperties.getJSONObject("consequence");
        final String consequenceType = consequence.keys().next();
        final List<Consequence> consequences =
                consequenceFactory.createConsequence(consequenceType, consequence);
        final Crossroad crossroad =
                crossroadFactory.createCrossroad(id, type, crossroadParams, consequences);
        crossroads.add(crossroad);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Setter
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    public void setZombiesLocations(final int zombiesLocations) {
        this.zombiesLocations = zombiesLocations;
    }

    @Override
    public void setZombiesColony(final int zombiesColony) {
        this.zombiesColony = zombiesColony;
    }

    @Override
    public void setChildrenInColony(final int childrenInColony) {
        this.childrenInColony = childrenInColony;
    }

    @Override
    public void setConfigPath(final Path configPath) {
        this.configPath = configPath;
    }

    @Override
    public void setMoral(final int moral) {
        this.moral = moral;
    }

    @Override
    public void setRounds(final int rounds) {
        this.rounds = rounds;
    }

    @Override
    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Helper
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Creates entrances and spreads zombies.
     *
     * @param entrances The amount of entrances.
     * @param zombies   The amount of zombies.
     * @return The list of entrances.
     */
    private List<Entrance> createEntrances(final int entrances, final int zombies) {
        final List<Entrance> locationEntrances = new ArrayList<>(entrances);
        for (int id = 0; id < entrances; id++) {
            locationEntrances.add(new Entrance(id));
        }
        for (int i = 0; i < zombies; i++) {
            locationEntrances.get(i % entrances).addZombie();
        }
        return locationEntrances;
    }

    /**
     * Returns the cards corresponding to the list of their ids.
     *
     * @param cardIds The ids of the cards.
     * @return The cards.
     */
    private List<Card> getCardList(final Collection<Integer> cardIds) {
        return cardIds.stream().map(cardMap::get).collect(Collectors.toList());
    }

    @Override
    public void setSeed(final long seed) {
        this.randomManager = new RandomManager(seed);
    }
}
