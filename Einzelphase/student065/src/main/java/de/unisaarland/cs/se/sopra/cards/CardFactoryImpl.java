package de.unisaarland.cs.se.sopra.cards;

import de.unisaarland.cs.se.sopra.config.CardFactory;
import de.unisaarland.cs.se.sopra.config.ParamMap;

public class CardFactoryImpl implements CardFactory<Card> {

    @Override
    public Card createCard(final int id, final String name, final ParamMap params) {
        return switch (name) {
            case "colt" -> {
                yield new Colt(id);
            }
            case "fuel" -> {
                yield new Fuel(id);
            }
            case "hammer" -> {
                yield new Hammer(id);
            }
            case "lock" -> {
                yield new Lock(id);
            }
            case "medicine" -> {
                yield new Medicine(id);
            }
            case "scissors" -> {
                yield new Scissors(id);
            }
            case "snow_boots" -> {
                yield new Snowboots(id);
            }
            case "stuff" -> {
                yield new Stuff(id);
            }
            case "swab" -> {
                yield new Swab(id);
            }
            case "blueprint" -> {
                final int locationId = params.getInt("location");
                yield new Blueprint(id, locationId);
            }
            case "food" -> {
                final int amount = params.getInt("amount");
                yield new Food(id, amount);
            }
            default -> {
                throw new IllegalArgumentException("Unknown card: %s".formatted(name));
            }
        };
    }
}
