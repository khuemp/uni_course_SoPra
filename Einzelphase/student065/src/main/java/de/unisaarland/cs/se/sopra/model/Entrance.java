package de.unisaarland.cs.se.sopra.model;

public class Entrance {

    public static final int DEFAULT_CAPACITY = 3;
    private final int capacity;
    private final int id;
    private int zombieCount;
    private int barricadeCount;

    public Entrance(final int id) {
        this.capacity = DEFAULT_CAPACITY;
        this.id = id;
    }

    /**
     * @param amount Amount of zombies to remove.
     */
    public void removeZombies(final int amount) {
        if (zombieCount - amount < 0) {
            throw new IllegalArgumentException(
                    "Cannot remove more zombies than available at entrance");
        }
        zombieCount = zombieCount - amount;
    }

    /**
     * @param amount Amount of barricades to place.
     */
    public void placeBarricades(final int amount) {
        if (barricadeCount + zombieCount + amount > capacity) {
            throw new IllegalArgumentException("Barricade count exceeds capacity");
        }
        barricadeCount = barricadeCount + amount;
    }

    public int getZombieCount() {
        return zombieCount;
    }

    public int getBarricadeCount() {
        return barricadeCount;
    }

    public int getCapacityLeft() {
        return capacity - (zombieCount + barricadeCount);
    }

    /**
     * This method kills one zombie.
     */
    public void killZombie() {
        if (this.zombieCount == 0) {
            throw new IllegalStateException("Cannot remove zombie from empty entrance");
        }
        this.zombieCount--;
    }

    /**
     * This method spawns one zombie.
     */
    public void addZombie() {
        if (zombieCount + barricadeCount == capacity) {
            throw new IllegalStateException("Entrance already fully occupied");
        }
        zombieCount++;
    }

    /**
     * This method destroys one barricade.
     */
    public void removeBarricade() {
        if (barricadeCount == 0) {
            throw new IllegalStateException("No barricade to remove");
        }
        barricadeCount--;
    }

    public int getId() {
        return id;
    }
}
