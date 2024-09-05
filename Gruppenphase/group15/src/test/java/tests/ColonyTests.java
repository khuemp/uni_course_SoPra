package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.controlstructures.card.Food;
import de.unisaarland.cs.se.sopra.controlstructures.card.Fuel;
import de.unisaarland.cs.se.sopra.controlstructures.card.Lock;
import de.unisaarland.cs.se.sopra.controlstructures.card.Medicine;
import de.unisaarland.cs.se.sopra.controlstructures.equipment.Blueprint;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Colony;
import de.unisaarland.cs.se.sopra.maingame.Crisis;
import de.unisaarland.cs.se.sopra.maingame.Entrance;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Goal;
import de.unisaarland.cs.se.sopra.maingame.Location;
import de.unisaarland.cs.se.sopra.maingame.Obstruction;
import de.unisaarland.cs.se.sopra.maingame.Place;
import de.unisaarland.cs.se.sopra.maingame.Wound;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class ColonyTests {

  @Test
  void initialisationTest() {
    final Colony colony = new Colony(12, 4, null);
    assertFalse(colony.hasChars());
    assertFalse(colony.hasChildren());
    assertEquals(0, colony.numCharacters());
    assertEquals(0, colony.getNumChildren());
    assertEquals(0, colony.getFood());
    assertEquals(0, colony.getTrash());
  }

  @Test
  void addCharacterTest() {
    final Character character = new Character(1, 5, null, null, 3, 5, 34, "Guenther");
    final Colony colony = new Colony(12, 4, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    assertFalse(colony.hasChars());
    assertTrue(colony.addCharacter(character));
    assertTrue(colony.hasChars());
    assertEquals(1, colony.numCharacters());
  }

  @Test
  void hasCharactersTest() {
    final Character character = new Character(1, 5, null, null, 3, 5, 34, "Guenther");
    final Colony colony = new Colony(12, 4, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    assertFalse(colony.hasChars());
    assertTrue(colony.addCharacter(character));
    assertTrue(colony.hasChars());
    assertTrue(colony.removeCharacter(character));
    assertFalse(colony.hasChars());
  }

  /*
  @Test
  void hasChildrenTests() {
    final Colony colony = new Colony(12, 4, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
  }*/

  @Test
  void trashTest() {
    final Colony colony = new Colony(12, 4, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    for (int i = 0; i < 7; i++) {
      colony.addToTrash();
      assertEquals(i + 1, colony.getTrash());
    }
    assertTrue(colony.takeOutTrash(5));
    assertEquals(2, colony.getTrash());
    assertTrue(colony.takeOutTrash(2));
    assertEquals(0, colony.getTrash());
    assertFalse(colony.takeOutTrash(3));
    assertEquals(0, colony.getTrash());
  }

  @Test
  void moralTest() {
    final Colony colony = new Colony(12, 4, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    for (int i = 0; i < 7; i++) {
      colony.moralChange(2);
      assertEquals((i + 1) * 2, colony.getMoral());
    }
    colony.moralChange(-3);
    assertEquals(11, colony.getMoral());
    colony.moralChange(-15);
    assertEquals(0, colony.getMoral());
    colony.moralChange(-3);
    assertEquals(0, colony.getMoral());
  }

  @Test
  void foodTest() {
    final Colony colony = new Colony(12, 4, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    for (int i = 0; i < 7; i++) {
      colony.addFood(3);
      assertEquals((i + 1) * 3, colony.getFood());
    }
    assertTrue(colony.removeFood(3));
    assertEquals(18, colony.getFood());
    assertTrue(colony.removeFood(15));
    assertEquals(3, colony.getFood());
    assertFalse(colony.removeFood(6));
    assertEquals(0, colony.getMoral());
    colony.addStarvationPoint();
    colony.addStarvationPoint();
    assertEquals(2, colony.getStarvationPoints());
  }

  @Test
  void obstructionTest() {
    final Colony colony = new Colony(12, 4, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    final Entrance e5 = colony.getEntranceById(5);
    assertNull(e5);
    assertEquals(12, colony.numObstruction(Obstruction.EMPTY));
    assertEquals(0, colony.numObstruction(Obstruction.ZOMBIE));
    assertEquals(0, colony.numObstruction(Obstruction.BARRICADE));
    final Entrance e1 = colony.getEntranceById(1);
    assertTrue(e1.addObstruction(Obstruction.ZOMBIE));
    assertEquals(1, colony.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(11, colony.numObstruction(Obstruction.EMPTY));
    assertEquals(2, e1.numObstruction(Obstruction.EMPTY));
    assertTrue(e1.addObstruction(Obstruction.ZOMBIE));
    assertEquals(2, colony.numObstruction(Obstruction.ZOMBIE));
    assertEquals(2, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(10, colony.numObstruction(Obstruction.EMPTY));
    assertEquals(1, e1.numObstruction(Obstruction.EMPTY));
  }

  @Test
  void obstructionTest2() {
    final Colony colony = new Colony(12, 4, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    final Entrance e1 = colony.getEntranceById(1);
    e1.addObstruction(Obstruction.ZOMBIE);
    e1.addObstruction(Obstruction.ZOMBIE);
    assertTrue(e1.addObstruction(Obstruction.BARRICADE));
    assertEquals(2, colony.numObstruction(Obstruction.ZOMBIE));
    assertEquals(2, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, colony.numObstruction(Obstruction.BARRICADE));
    assertEquals(1, e1.numObstruction(Obstruction.BARRICADE));
    assertEquals(9, colony.numObstruction(Obstruction.EMPTY));
    assertEquals(0, e1.numObstruction(Obstruction.EMPTY));
    assertTrue(e1.removeObstruction(Obstruction.ZOMBIE));
    assertEquals(1, colony.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, colony.numObstruction(Obstruction.BARRICADE));
    assertEquals(1, e1.numObstruction(Obstruction.BARRICADE));
    assertEquals(10, colony.numObstruction(Obstruction.EMPTY));
    assertEquals(1, e1.numObstruction(Obstruction.EMPTY));
  }

  @Test
  void obstructionTest3() {
    final Colony colony = new Colony(12, 4, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    final Entrance e1 = colony.getEntranceById(1);
    e1.addObstruction(Obstruction.ZOMBIE);
    e1.addObstruction(Obstruction.ZOMBIE);
    e1.addObstruction(Obstruction.BARRICADE);
    e1.removeObstruction(Obstruction.ZOMBIE);
    assertTrue(e1.removeObstruction(Obstruction.BARRICADE));
    assertEquals(1, colony.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(0, colony.numObstruction(Obstruction.BARRICADE));
    assertEquals(0, e1.numObstruction(Obstruction.BARRICADE));
    assertEquals(11, colony.numObstruction(Obstruction.EMPTY));
    assertEquals(2, e1.numObstruction(Obstruction.EMPTY));
  }

  @Test
  void cardTest() {
    final Card card1 = new Fuel(2);
    final Card card2 = new Food(3, 5);
    final Card card3 = new Lock(4);
    final Card card4 = new Medicine(5);
    final ArrayList<Card> cardList = new ArrayList<>(4);
    cardList.add(card1);
    cardList.add(card2);
    cardList.add(card3);
    cardList.add(card4);
    final Colony colony = new Colony(12, 4, cardList);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    for (int i = 0; i < 4; i++) {
      assertEquals(4 - i, colony.numCards());
      assertNotNull(colony.getCard());
    }
    assertEquals(0, colony.numCards());
    assertNull(colony.getCard());
  }


  @Test
  void distributeZombiesTest() {
    final Colony colony = new Colony(12, 3, null);
    final Map<Integer, Location> locationHashMap = new HashMap<>(3);
    final List<Card> cards = new ArrayList<>();
    for (int i = 1; i < 5; i++) {
      locationHashMap.put(i,
          new Location(i, i, "cool Location" + i, i, cards));
    }
    final Goal goal = new Goal(Optional.empty(), Optional.empty(), Optional.of(Boolean.TRUE), 10, 7,
        4, 4, 2);
    final List<Crisis> crisisList = new ArrayList<>();
    final List<Character> characterList = new ArrayList<>();
    final Game game = new Game(colony, locationHashMap, goal, crisisList,
        characterList, 5, 20, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);
    final Character char1 = new Character(1, 3,
        null, null, 4, 5, 12, "Anna");
    final Character char2 = new Character(2, 5,
        null, null, 3, 7, 12, "Anton");
    int i = 1;
    for (final Place p : locationHashMap.values()) {
      p.spawnZombies(3);
      loggerino("wie oft");
      assertEquals(3, game.getLocById(i).numObstruction(Obstruction.ZOMBIE));
      ++i;
    }
    colony.addCharacter(char1);
    colony.addCharacter(char2);
    colony.addChildren(1);
    final Entrance e1 = colony.getEntranceById(0);
    e1.addObstruction(Obstruction.ZOMBIE);
    e1.addObstruction(Obstruction.BARRICADE);

    colony.distributeZombies(game, 4);
    loggerino("und hier auch wie oft");
    // wegen obstruction nur 4
    assertEquals(4, colony.numObstruction(Obstruction.ZOMBIE));
  }


  public void loggerino(final String msg) {
    LoggerFactory.getLogger(ColonyTests.class).debug("{}", msg);
  }

  @Test
  void bittenEventPleaseFinder() {

    final var builder = new sopra.systemtest.framework.random.RandomNumberBuilder();


    builder.addEmpty(6);
    builder.addEmpty(6);
    builder.addEmpty(6);

    builder.addNumber(12, 11);
    builder.addNumber(12, 7);
    builder.addNumber(12, 7);


    //final var seed =
    //sopra.systemtest.framework.random.SeedFinder.findSeed(builder.get(), 1_000_000);
    //System.out.println(seed);
  }

  @Test
  void rollInfectionTest() {
    final Colony colony = new Colony(12, 3, null);
    final Map<Integer, Location> locationHashMap = new HashMap<>(3);
    final List<Card> cards = new ArrayList<>();
    final EnumMap<Wound, Wound> woundType = new EnumMap<>(Wound.class);
    for (int i = 1; i < 5; i++) {
      locationHashMap.put(i,
          new Location(i, i, "cool Location" + i, i, cards));
    }
    final Goal goal = new Goal(Optional.empty(), Optional.empty(), Optional.of(Boolean.TRUE), 10, 7,
        4, 4, 2);
    final List<Crisis> crisisList = new ArrayList<>();
    final List<Character> characterList = new ArrayList<>();
    final Character char1 = new Character(1, 3,
        null, null, 4, 5, 12, "Anna");
    final Character char2 = new Character(2, 5,
        null, null, 3, 7, 12, "Anton");
    final Character char3 = new Character(2, 6,
        null, null, 3, 7, 12, "Juan");
    characterList.add(char1);
    characterList.add(char2);
    characterList.add(char3);

    final Game game = new Game(colony, locationHashMap, goal, crisisList,
        characterList, 5, 434, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);

    game.rollDice2(6);
    game.rollDice2(6);
    game.rollDice2(6);

    colony.addChildren(1);

    locationHashMap.get(1).rollInfection(game, woundType, 1);
    locationHashMap.get(1).rollInfection(game, woundType, 2);

    // der test macht richtigen müll
    assertEquals(char2.numWounds(), char3.numWounds());

  }

  @Test
  void bluePrintTest() {
    //der test auch
    final Colony colony = new Colony(12, 3, null);
    final Map<Integer, Location> locationHashMap = new HashMap<>(3);
    final List<Card> cards = new ArrayList<>(10);

    for (int i = 0; i < 10; i++) {
      cards.add(new Blueprint(1, 1000 + i));
    }

    //final EnumMap<Wound, Wound> woundType = new EnumMap<>(Wound.class);
    for (int i = 1; i < 5; i++) {
      locationHashMap.put(i,
          new Location(i, i, "cool Location" + i, i, cards));
    }
    final Goal goal = new Goal(Optional.empty(), Optional.empty(), Optional.of(Boolean.TRUE), 10, 7,
        4, 4, 2);
    final List<Crisis> crisisList = new ArrayList<>();
    final List<Character> characterList = new ArrayList<>(3);

    final Character char1 = new Character(1, 3,
        null, null, 4, 5, 12, "Anna");
    final Character char2 = new Character(2, 5,
        null, null, 3, 7, 12, "Anton");
    final Character char3 = new Character(2, 5,
        null, null, 3, 7, 12, "Juan");
    characterList.add(char1);
    characterList.add(char2);
    characterList.add(char3);
    char1.addEquipment(new Blueprint(1, 1001));
    final Game game = new Game(colony, locationHashMap, goal, crisisList,
        characterList, 5, 434, null);
    final Observer obs = new StubServer();
    colony.setObs(obs);

    game.rollDice2(6);
    game.rollDice2(6);
    game.rollDice2(6);

    locationHashMap.get(1).addCharacter(char1);
    locationHashMap.get(1).addCharacter(char2);
    locationHashMap.get(1).addCharacter(char3);

    assert (locationHashMap.get(1).areCardsLeft());


    assertEquals(char2.getWounds().size(), char3.getWounds().size());
  }

}
