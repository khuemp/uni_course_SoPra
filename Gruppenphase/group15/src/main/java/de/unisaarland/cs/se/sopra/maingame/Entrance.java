package de.unisaarland.cs.se.sopra.maingame;

//import de.unisaarland.cs.se.sopra.server.Observer;

import java.util.ArrayList;
import java.util.List;

public class Entrance {
  private final int id;
  private final List<Obstruction> slots;
  //private final Observer obs;

  public Entrance(final int identifier/*, final Observer obs*/) {
    this.id = identifier;
    //this.obs = obs;
    this.slots = new ArrayList<>(3);
    for (int i = 0; i < 3; i++) {
      this.slots.add(Obstruction.EMPTY);
    }
  }

  public int getId() {
    return this.id;
  }

  // tries to remove a specified obstruction, false when none could be removed
  public boolean removeObstruction(final Obstruction obstruction) {
    for (int i = 0; i < 3; i++) {
      if (this.slots.get(i) == obstruction) {
        this.slots.set(i, Obstruction.EMPTY);
        return true;
      }
    }
    return false;
  }

  // tries to add obstruction, returns false if none could be added
  public boolean addObstruction(final Obstruction obstruction) {
    for (int i = 0; i < 3; i++) {
      if (this.slots.get(i) == Obstruction.EMPTY) {
        this.slots.set(i, obstruction);
        return true;
      }
    }
    return false;
  }

  // returns how many of specified obstructionsare at this entrance
  public int numObstruction(final Obstruction obstruction) {
    int res = 0;
    for (int i = 0; i < 3; i++) {
      if (this.slots.get(i) == obstruction) {
        res++;
      }
    }
    return res;
  }
}
