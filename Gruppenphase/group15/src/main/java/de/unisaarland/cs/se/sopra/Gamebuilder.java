package de.unisaarland.cs.se.sopra;


import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Crisis;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Goal;
import de.unisaarland.cs.se.sopra.maingame.Location;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


public class Gamebuilder {

  public Game buildGame(final Path path, final long seed) throws IOException {

    final JsonParser reader = new JsonParser(path);
    final int maxPlayers = reader.readMaxPlayers();
    final CardTuple cardTuple = reader.readInCards();
    final LocColTuple locColTuple = reader.readInLocations(cardTuple.getCardHashMap());

    // startcards >= maxplayers*5
    if (locColTuple.getColony().numCards() < maxPlayers * 5) {
      throw new IllegalArgumentException();
    }


    final int colonyId = locColTuple.getColony().getLocId();
    final List<Character> characterList =
        reader.readInChars(colonyId, locColTuple.getLocationHashMap());
    // survivors >= maxplayers*2+2
    if (characterList.size() < (maxPlayers * 2) + 2) {
      throw new IllegalArgumentException();
    }
    final Goal goal = reader.readInGoal();
    final List<Crisis> crisisList = reader.readCrisis();
    locColTuple.getColony().spawnZombies(goal.getZombiesColony());
    locColTuple.getColony().addChildren(goal.getChildrenColony());
    locColTuple.getColony().moralChange(goal.getMoral());
    // crisis cards amount >= rounds
    if (crisisList.size() < goal.getMaxRounds()) {
      throw new IllegalArgumentException();

    }
    // cards location >= maxplayers * 5
    for (final Location loc : locColTuple.getLocationHashMap().values()) {
      if (loc.getInitStackSize() < maxPlayers * 5) {
        throw new IllegalArgumentException();
      }
      this.checkLocation(loc, locColTuple);
      loc.spawnZombies(goal.getZombiesLoaction());
    }
    this.checkColony(locColTuple);
    //  Außerdem dürfen im Ziel nicht
    //mehr Zombies die initial spawnen angegeben sein, als Plätze für sie vorhanden sind
    if (locColTuple.getNumEntrances() * 3 < (goal.getZombiesLoaction() + goal.getZombiesColony())) {
      // TODO game beenden
      throw new IllegalArgumentException();
    }
    //  Außerdem
    //dürfen für die Zielbedingung nicht mehr Barrikaden gefordert werden, als Plätze für mögliche
    //Barrikaden vorhanden sind
    if (!goal.getBarricades().isEmpty()) {
      if (locColTuple.getNumEntrances() * 3 < goal.getBarricades().get()) {
        throw new IllegalArgumentException();
      }
    }
    // Ebenso darf die maximale Anzahl der Standorte mit Zombies die
    //Anzahl der Standorte im Spiel nicht überschreiten

    if (!goal.getLocationsWithZombies().isEmpty()) {
      if (goal.getLocationsWithZombies().get() > locColTuple.getLocationHashMap().size() + 1) {
        throw new IllegalArgumentException();
      }
    }
    return new Game(locColTuple.getColony(), locColTuple.getLocationHashMap(), goal, crisisList,
        characterList, maxPlayers, seed, reader.getJsonObj());
  }

  private void checkColony(final LocColTuple locColTuple) {
    for (final Card c : locColTuple.getColony().getCards()) {
      c.checkLocIdValid(locColTuple.getLocationHashMap(), locColTuple.getColony().getLocId());
    }
  }

  private void checkLocation(final Location loc, final LocColTuple locColTuple) {
    for (final Card c : loc.getCards()) {
      c.checkLocIdValid(locColTuple.getLocationHashMap(), locColTuple.getColony().getLocId());
    }
  }
}