package de.unisaarland.cs.se.sopra.controlstructures.card;

import de.unisaarland.cs.se.sopra.maingame.CrisisCardType;
import de.unisaarland.cs.se.sopra.maingame.Location;
import java.util.Map;

public interface Card {

  void accept(CardVisitor v);

  CrisisCardType checkCrisisType();

  int getId();

  public void checkLocIdValid(Map<Integer, Location> locationMap, int colonyId);
}
