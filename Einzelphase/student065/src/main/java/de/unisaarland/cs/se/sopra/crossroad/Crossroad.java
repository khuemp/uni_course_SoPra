package de.unisaarland.cs.se.sopra.crossroad;

import java.util.List;

public abstract class Crossroad {

    private final int id;
    private final CrossroadType type;
    private final List<Consequence> consequences;

    public Crossroad(final int id, final CrossroadType type, final List<Consequence> consequences) {
        this.id = id;
        this.type = type;
        this.consequences = consequences;
    }

    public CrossroadType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public boolean hasLoc() {
        return false;
    }

    public int getLocId() {
        return 0;
    }

    public int getNumTrash() {
        return 0;
    }

    public List<Consequence> getConsequences() {
        return consequences;
    }
}
