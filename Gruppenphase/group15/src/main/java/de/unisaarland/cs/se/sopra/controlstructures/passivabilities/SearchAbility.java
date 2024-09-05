package de.unisaarland.cs.se.sopra.controlstructures.passivabilities;

import de.unisaarland.cs.se.sopra.controlstructures.command.Search;

public class SearchAbility extends PassiveAbility {
  final int locationId;
  final int maxActivations;
  int numCards;
  int currentActivations;
  int currentRound;

  public SearchAbility(final int numCards, final int locationId, final int maxActivations) {
    this.numCards = numCards;
    this.locationId = locationId;
    this.maxActivations = maxActivations;
  }

  @Override
  public void modify(final Search s) {
    if (this.locationId == s.getLocId()) {
      if (s.getCurrentRound() != currentRound) {
        currentRound = s.getCurrentRound();
        currentActivations = maxActivations;
      }
      if (currentActivations != 0) {
        s.setNumCards(this.numCards);
        s.setModified(true);
        currentActivations--;
      }
    }
  }

  public int getLocationId() {
    return this.locationId;
  }
}
