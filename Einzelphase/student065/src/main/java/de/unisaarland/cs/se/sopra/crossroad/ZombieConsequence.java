package de.unisaarland.cs.se.sopra.crossroad;

public class ZombieConsequence extends Consequence {

    private final int spawnZombies;
    private final int locId;

    public ZombieConsequence(final int spawnZombies, final int locId) {
        this.spawnZombies = spawnZombies;
        this.locId = locId;
    }

    @Override
    public void accept(final ConsequenceVisitor consequenceVisitor) {
        consequenceVisitor.visit(this);
    }

    public int getSpawnZombies() {
        return spawnZombies;
    }

    public int getLocId() {
        return locId;
    }
}
