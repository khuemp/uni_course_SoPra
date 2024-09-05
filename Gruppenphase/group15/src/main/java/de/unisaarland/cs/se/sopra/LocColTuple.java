package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.maingame.Colony;
import de.unisaarland.cs.se.sopra.maingame.Location;
import java.util.Map;

public class LocColTuple {
  private final Map<Integer, Location> locationHashMap;
  private final Colony colony;
  private final int numEntrances;

  public LocColTuple(final Map<Integer, Location> locationHashMap,
                     final Colony colony, final int numEntrances) {
    this.locationHashMap = locationHashMap;
    this.colony = colony;
    this.numEntrances = numEntrances;
  }

  public Colony getColony() {
    return this.colony;
  }

  public Map<Integer, Location> getLocationHashMap() {
    return this.locationHashMap;
  }

  public int getNumEntrances() {
    return this.numEntrances;
  }
}
