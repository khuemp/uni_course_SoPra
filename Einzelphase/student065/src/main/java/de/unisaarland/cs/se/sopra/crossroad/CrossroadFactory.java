package de.unisaarland.cs.se.sopra.crossroad;

import de.unisaarland.cs.se.sopra.config.ParamMap;
import java.util.List;

public interface CrossroadFactory<T> {

    T createCrossroad(int id, String type, ParamMap crossroadParams,
                      List<Consequence> consequences);
}
