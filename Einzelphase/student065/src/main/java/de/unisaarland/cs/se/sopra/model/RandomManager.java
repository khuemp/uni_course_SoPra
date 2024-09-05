package de.unisaarland.cs.se.sopra.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomManager {

    public static final int UNHARMED_BOUND = 6;
    public static final int SIX_SIDED_DIE = 6;
    public static final int TWELVE_SIDED_DIE = 12;
    public static final int WOUNDED_BOUND = 9;
    public static final int FROSTBITTEN_BOUND = 11;
    public static final int BITTEN_BOUND = 12;
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomManager.class);
    private final Random random;

    public RandomManager(final long seed) {
        random = new Random(seed);
        LOGGER.trace("Random Object initialized with seed: {}", seed);
    }

    /**
     * @return The eyes roled.
     */
    public int rollActionDie() {
        final int result = random.nextInt(SIX_SIDED_DIE) + 1;
        LOGGER.trace("Rolled ActionDie: {}", result);
        return result;
    }

    /**
     * Rolls the infection die and returns the rolled status effect.
     *
     * @return The status effect.
     */
    public StatusEffect rollInfectionDie() {
        final int randomValue = random.nextInt(TWELVE_SIDED_DIE);
        LOGGER.trace("Rolled InfectionDie: {}", randomValue);
        StatusEffect result = StatusEffect.UNHARMED;
        if (randomValue >= UNHARMED_BOUND && randomValue < WOUNDED_BOUND) {
            result = StatusEffect.WOUND;
        } else if (randomValue >= WOUNDED_BOUND && randomValue < FROSTBITTEN_BOUND) {
            result = StatusEffect.FROSTBITE;
        } else if (randomValue >= FROSTBITTEN_BOUND && randomValue < BITTEN_BOUND) {
            result = StatusEffect.BITE;
        }
        return result;
    }

    /**
     * @param n The upper border.
     * @return The random number.
     */
    public int nextInt(final int n) {
        final int randomInt = random.nextInt(n);
        LOGGER.trace("Rolled random int: {}", randomInt);
        return randomInt;
    }

    /**
     * @param cards The cards to shuffle.
     * @param <T>   The generic type of the cards.
     * @return The shuffled cards.
     */
    public <T> List<T> shuffleList(final List<T> cards) {
        final List<T> shuffledList = new LinkedList<>(cards);
        Collections.shuffle(shuffledList, random);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Shuffled list of size: {}", shuffledList.size());
        }
        return shuffledList;
    }

}
