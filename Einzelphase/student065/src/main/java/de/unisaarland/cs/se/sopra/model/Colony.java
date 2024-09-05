package de.unisaarland.cs.se.sopra.model;

import de.unisaarland.cs.se.sopra.cards.Card;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Colony extends Location {

    private static final int MAX_SURVIVOR_SPACES = 3;
    private int numChildren;
    private int food;
    private int starvationTokens;

    public Colony(final int id, final List<Entrance> entrances, final Collection<Card> cards,
                  final int numChildren) {
        super(id, entrances, cards, MAX_SURVIVOR_SPACES);
        this.numChildren = numChildren;
    }

    @Override
    public int getNumChildren() {
        return numChildren;
    }

    public void addChild() {
        this.numChildren++;
    }

    public void addFood(final int numFood) {
        this.food += numFood;
    }

    public int getFood() {
        return this.food;
    }

    public void removeFood(final int amount) {
        this.food -= amount;
        assert (this.food >= 0);
    }

    public void increaseStarvationToken() {
        starvationTokens++;
    }

    public int getNumStarvationTokens() {
        return starvationTokens;
    }

    @Override
    public boolean hasChildren() {
        return numChildren > 0;
    }

    @Override
    public void killChild() {
        numChildren--;
    }

    public void resetCards() {
        super.setCards(new LinkedList<>());
    }

    @Override
    public boolean hasPlacesLeft() {
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return this.getId() == ((Colony) o).getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
