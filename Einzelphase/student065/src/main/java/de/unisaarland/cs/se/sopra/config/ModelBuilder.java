package de.unisaarland.cs.se.sopra.config;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;

public interface ModelBuilder<M> {

    void addColony(final int id, final int entrances, final List<Integer> cardsIds);

    void addLocation(
            final int id,
            final String name,
            final int entrances,
            final List<Integer> cardIds,
            final int survivorSpaces);

    void addSurvivor(
            final int id,
            final String name,
            final int attack,
            final int search,
            final int status,
            final String abilityName,
            final ParamMap abilityParams);

    void addCrisis(final int id, final String type, final int moralChange, final int requiredCards);

    void addCard(final int id, final String name, final ParamMap param);

    void addGoal(
            Optional<Integer> locationWithZombies,
            Optional<Integer> barricades,
            Optional<Boolean> survive);

    void addCrossroad(final int id, final String type, final ParamMap crossroadParam,
                      final JSONObject crossroadProperties);

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Setter
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    void setMaxPlayers(final int maxPlayers);

    void setZombiesLocations(final int zombiesLocations);

    void setZombiesColony(final int zombiesColony);

    void setChildrenInColony(final int childrenInColony);

    void setConfigPath(final Path configPath);

    void setSeed(final long seed);

    void setMoral(final int moral);

    void setRounds(final int rounds);

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    M build();
}
