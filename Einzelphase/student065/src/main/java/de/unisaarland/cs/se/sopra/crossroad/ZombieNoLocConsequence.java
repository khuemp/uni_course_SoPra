package de.unisaarland.cs.se.sopra.crossroad;

public class ZombieNoLocConsequence extends Consequence {

    private final int spawnZombies;

    public ZombieNoLocConsequence(final int spawnZombies) {
        this.spawnZombies = spawnZombies;
    }

    @Override
    public void accept(final ConsequenceVisitor consequenceVisitor) {
        consequenceVisitor.visit(this);
    }

    public int getSpawnZombies() {
        return spawnZombies;
    }
}
