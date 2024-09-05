package de.unisaarland.cs.se.sopra.crossroad;

import java.util.List;

public class WasteChangedCrossroad extends Crossroad {

    private final int numTrash;

    public WasteChangedCrossroad(final int id, final CrossroadType type,
                                 final List<Consequence> consequences, final int numTrash) {
        super(id, type, consequences);
        this.numTrash = numTrash;
    }

    @Override
    public int getNumTrash() {
        return numTrash;
    }
}
