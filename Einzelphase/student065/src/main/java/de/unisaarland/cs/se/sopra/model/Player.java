package de.unisaarland.cs.se.sopra.model;

import de.unisaarland.cs.se.sopra.cards.Card;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Player {

    private final int id;
    private final List<Integer> diceValues;
    private final String name;
    private boolean left; //defaults to false
    private List<Card> cards;
    private List<Survivor> survivors;
    private Crossroad crossroad;
    private boolean crossroadActivate;
    private int numTrashTrigger;
    private boolean voted;

    public Player(final int id, final String name) {
        this.id = id;
        this.cards = new LinkedList<>();
        this.survivors = new LinkedList<>();
        this.diceValues = new LinkedList<>();
        this.name = name;
    }

    public void setLeft() {
        left = true;
    }

    public boolean hasLeft() {
        return left;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Card> getCards() {
        return cards;
    }

    /**
     * @param cardId The id of the card.
     * @return The card given the id (if existent).
     */
    public Optional<Card> getCard(final int cardId) {
        for (final Card card : cards) {
            if (card.getId() == cardId) {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    public List<Survivor> getSurvivors() {
        return survivors;
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
        final Player other = (Player) obj;
        return id == other.id;
    }

    /**
     * @return Highest die value if it exists.
     */
    public Optional<Integer> getHighestDie() {
        return diceValues.stream().max(Comparator.comparingInt(Integer::intValue));
    }

    /**
     * Removes the die with the lowest value that is greater or equal to the specified value.
     *
     * @param value Value the die hast to be greater or equal to.
     */
    public void removeDieLowerThan(final int value) {
        Collections.sort(diceValues);
        int indexToRemove = diceValues.size();
        for (int i = 0; i < diceValues.size(); i++) {
            if (diceValues.get(i) >= value) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove >= diceValues.size()) {
            throw new IllegalStateException("Die value could not be found");
        } else {
            diceValues.remove(indexToRemove);
        }
    }

    /**
     * Removes the lowest die of a player.
     */
    public void removeLowestDie() {
        Collections.sort(diceValues);
        diceValues.remove(0);
    }

    public void removeCard(final Card card) {
        cards.remove(card);
    }

    public void addCard(final Card card) {
        cards.add(card);
    }

    public void addDie(final int die) {
        diceValues.add(die);
    }

    public void removeSurvivor(final Survivor survivor) {
        survivors.remove(survivor);
    }

    public boolean hasSurvivorsLeft() {
        return !survivors.isEmpty();
    }

    public void resetCards() {
        cards = new LinkedList<>();
    }

    public void addSurvivor(final Survivor survivor) {
        survivors.add(survivor);
    }

    public Crossroad getCrossroad() {
        return crossroad;
    }

    public void setCrossroad(final Crossroad crossroad) {
        this.crossroad = crossroad;
    }

    public boolean getCrossroadActivate() {
        return crossroadActivate;
    }

    public void setCrossroadActivate(final boolean crossroadActivate) {
        this.crossroadActivate = crossroadActivate;
    }

    public int getNumTrashTrigger() {
        return numTrashTrigger;
    }

    public void setNumTrashTrigger(final int numTrash) {
        this.numTrashTrigger = numTrash;
    }

    public boolean hasVoted() {
        return voted;
    }

    public void setHasVoted(final boolean voted) {
        this.voted = voted;
    }

    /**
     * @param survivorId The id of a survivor.
     * @return Whether the player controls the survivor.
     */
    public boolean hasSurvivor(final int survivorId) {
        for (final Survivor survivor : survivors) {
            if (survivor.getId() == survivorId) {
                return true;
            }
        }
        return false;
    }

    public void resetSurvivors() {
        this.survivors = new LinkedList<>();
    }

    /**
     * Reset everything.
     */
    public void tick() {
        this.diceValues.clear();
        this.crossroadActivate = false;
        this.crossroad = null;
        this.numTrashTrigger = 0;
        this.voted = false;
        for (final Survivor survivor : this.getSurvivors()) {
            survivor.tick();
        }
    }
}
