package de.unisaarland.cs.se.sopra.maingame;

import java.util.Optional;

public class Goal {
  private final int maxRounds;
  private final int moral;
  private final int zombiesColony;
  private final int zombiesLocation;
  private final int childrenColony;
  private final Optional<Integer> locationsWithZombies;
  private final Optional<Integer> barricades;
  private final Optional<Boolean> survive;

  public Goal(final Optional<Integer> locationsWithZombies, final Optional<Integer> barricades,
              final Optional<Boolean> survive,
              final int maxRounds, final int moral,
              final int zombiesColony, final int zombiesLocation, final int childrenColony) {
    this.locationsWithZombies = locationsWithZombies;
    this.barricades = barricades;
    this.survive = survive;
    this.maxRounds = maxRounds;
    this.moral = moral;
    this.zombiesColony = zombiesColony;
    this.zombiesLocation = zombiesLocation;
    this.childrenColony = childrenColony;
  }

  public boolean checkGoalFulfilled(final int numZombieLocations,
                                    final int numBarricades, final int round) {
    //todo
    if (this.locationsWithZombies.isEmpty()) {
      if (!this.survive.isEmpty()) {
        return 1 == round;
      } else {
        return this.barricades.get() == numBarricades;
      }
    } else {
      return this.maxRounds == round && this.locationsWithZombies.get() < numZombieLocations;
    }
  }

  public int getMaxRounds() {
    return this.maxRounds;
  }

  public int getMoral() {
    return this.moral;
  }

  public int getZombiesColony() {
    return this.zombiesColony;
  }

  public int getZombiesLoaction() {
    return this.zombiesLocation;
  }

  public int getChildrenColony() {
    return this.childrenColony;
  }

  public Optional<Integer> getLocationsWithZombies() {
    return this.locationsWithZombies;
  }

  public Optional<Integer> getBarricades() {
    return this.barricades;
  }
}
