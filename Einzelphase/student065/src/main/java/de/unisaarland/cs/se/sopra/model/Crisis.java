package de.unisaarland.cs.se.sopra.model;

public class Crisis {

    private final int id;
    private final CrisisType type;
    private final int moralChange;
    private final int requiredCards;
    private int cardsContributed;

    public Crisis(final int id, final CrisisType type, final int moralChange,
                  final int requiredCards) {
        this.id = id;
        this.type = type;
        this.moralChange = moralChange;
        this.requiredCards = requiredCards;
    }

    public int getCardsRequired() {
        return requiredCards;
    }

    public int getCardsContributed() {
        return cardsContributed;
    }

    public int getId() {
        return id;
    }

    public CrisisType getType() {
        return type;
    }

    public int getMoralChange() {
        return moralChange;
    }

    public void cardContributed() {
        this.cardsContributed++;
    }

}
