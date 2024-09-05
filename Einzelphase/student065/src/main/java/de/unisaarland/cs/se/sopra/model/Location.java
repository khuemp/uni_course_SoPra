package de.unisaarland.cs.se.sopra.model;

import de.unisaarland.cs.se.sopra.cards.Card;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Location {

    private final int id;
    private final List<Entrance> entrances;
    private final List<Survivor> survivors;
    private final int survivorSpaces;
    private final int initialStackSize;
    private List<Card> cards;

    public Location(final int id, final List<Entrance> entrances,
                    final Collection<Card> cards,
                    final int survivorSpaces) {
        this.id = id;
        this.entrances = entrances;
        this.cards = new LinkedList<>(cards);
        this.survivors = new LinkedList<>();
        this.survivorSpaces = survivorSpaces;
        this.initialStackSize = cards.size();
    }

    /**
     * @return The drawn card if one was available.
     */
    public Optional<Card> drawCard() {
        if (this.cards.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(cards.remove(0));
        }
    }

    /**
     * @return Whether this location still has spaces left.
     */
    public boolean hasPlacesLeft() {
        return this.survivorSpaces - this.survivors.size() > 0;
    }

    public void addSurvivor(final Survivor survivor) {
        this.survivors.add(survivor);
    }

    public void removeSurvivor(final Survivor survivor) {
        this.survivors.remove(survivor);
    }

    public int getId() {
        return id;
    }

    /**
     * @param entrance The index of the entrance.
     * @return returns The entrance if existent.
     */
    public Optional<Entrance> getEntrance(final int entrance) {
        if (entrance < entrances.size() && entrance >= 0) {
            return Optional.of(entrances.get(entrance));
        } else {
            return Optional.empty();
        }
    }

    public List<Survivor> getSurvivors() {
        return survivors;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        return id == other.id;
    }

    public boolean hasCardsLeft() {
        return !cards.isEmpty();
    }

    public int getInitialStackSize() {
        return initialStackSize;
    }

    public boolean hasChildren() {
        return false;
    }

    public void killChild() {
        throw new IllegalStateException("There are no children in a normal location!");
    }

    public boolean hasSurvivors() {
        return !survivors.isEmpty();
    }

    public int getNumSurvivors() {
        return survivors.size();
    }

    public List<Entrance> getEntrances() {
        return entrances;
    }

    public int getNumChildren() {
        return 0;
    }

    /**
     * @return The survivor currently at this location with the lowest
     * social status.
     */
    public Optional<Survivor> getSurvivorSmallestStatus() {
        Optional<Survivor> smallest = Optional.empty();
        for (final Survivor survivor : survivors) {
            if (smallest.isEmpty()) {
                smallest = Optional.of(survivor);
            } else if (survivor.getSocialStatus() < smallest.get().getSocialStatus()) {
                smallest = Optional.of(survivor);
            }
        }
        return smallest;
    }
}
