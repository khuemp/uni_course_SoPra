package de.unisaarland.cs.se.sopra.crossroad;

import java.util.List;

public class MovedNoLocCrossroad extends Crossroad {

    public MovedNoLocCrossroad(final int id, final CrossroadType type,
                               final List<Consequence> consequences) {
        super(id, type, consequences);
    }
}
