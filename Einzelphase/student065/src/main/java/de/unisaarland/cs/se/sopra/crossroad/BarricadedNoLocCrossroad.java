package de.unisaarland.cs.se.sopra.crossroad;

import java.util.List;

public class BarricadedNoLocCrossroad extends Crossroad {

    public BarricadedNoLocCrossroad(final int id, final CrossroadType type,
                                    final List<Consequence> consequences) {
        super(id, type, consequences);
    }
}
