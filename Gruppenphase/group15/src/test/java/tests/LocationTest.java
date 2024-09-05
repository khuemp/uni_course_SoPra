package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.controlstructures.card.Food;
import de.unisaarland.cs.se.sopra.controlstructures.card.Fuel;
import de.unisaarland.cs.se.sopra.controlstructures.card.Lock;
import de.unisaarland.cs.se.sopra.controlstructures.card.Medicine;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Entrance;
import de.unisaarland.cs.se.sopra.maingame.Location;
import de.unisaarland.cs.se.sopra.maingame.Obstruction;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class LocationTest {

  @Test
  void initialisationTest() {
    final Location location = new Location(1, 3, "Station", 1, null);
    final Observer obs = new StubServer();
    location.setObs(obs);
    assertFalse(location.hasChars());
    assertFalse(location.hasChildren());
    assertEquals(0, location.numCharacters());
    assertEquals(1, location.getSurvivorSpaces());
    assertEquals(9, location.numObstruction(Obstruction.EMPTY));
    assertEquals(0, location.numObstruction(Obstruction.ZOMBIE));
    assertEquals(0, location.numObstruction(Obstruction.BARRICADE));
    assertEquals(0, location.getInitStackSize());
    assertFalse(location.areCardsLeft());
  }

  @Test
  void addCharacterTest() {
    final Character character = new Character(1, 5, null, null, 3, 5, 34, "Guenther");
    final Character character1 = new Character(2, 4, null, null, 4, 3, 1, "Anna");
    final Location location = new Location(1, 3, "Station", 1, null);
    final Observer obs = new StubServer();
    location.setObs(obs);
    assertFalse(location.hasChars());
    assertTrue(location.addCharacter(character));
    assertTrue(location.hasChars());
    assertEquals(1, location.numCharacters());
    assertFalse(location.addCharacter(character1));
    assertEquals(1, location.numCharacters());
  }

  @Test
  void hasCharactersTest() {
    final Character character = new Character(1, 5, null, null, 3, 5, 34, "Guenther");
    final Location location = new Location(1, 3, "Station", 1, null);
    final Observer obs = new StubServer();
    location.setObs(obs);
    assertFalse(location.hasChars());
    assertTrue(location.addCharacter(character));
    assertTrue(location.hasChars());
    assertTrue(location.removeCharacter(character));
    assertFalse(location.hasChars());
  }

  @Test
  void hasChildrenTests() {
    final Location location = new Location(1, 3, "Station", 1, null);
    final Observer obs = new StubServer();
    location.setObs(obs);
    assertFalse(location.hasChildren());
  }

  @Test
  void obstructionTest() {
    final Location location = new Location(1, 4, "Station", 1, null);
    final Observer obs = new StubServer();
    location.setObs(obs);
    final Entrance e5 = location.getEntranceById(5);
    assertNull(e5);
    assertEquals(12, location.numObstruction(Obstruction.EMPTY));
    assertEquals(0, location.numObstruction(Obstruction.ZOMBIE));
    assertEquals(0, location.numObstruction(Obstruction.BARRICADE));
    final Entrance e1 = location.getEntranceById(1);
    assertTrue(e1.addObstruction(Obstruction.ZOMBIE));
    assertEquals(1, location.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(11, location.numObstruction(Obstruction.EMPTY));
    assertEquals(2, e1.numObstruction(Obstruction.EMPTY));
    assertTrue(e1.addObstruction(Obstruction.ZOMBIE));
    assertEquals(2, location.numObstruction(Obstruction.ZOMBIE));
    assertEquals(2, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(10, location.numObstruction(Obstruction.EMPTY));
    assertEquals(1, e1.numObstruction(Obstruction.EMPTY));
  }

  @Test
  void obstructionTest2() {
    final Location location = new Location(1, 4, "Station", 1, null);
    final Observer obs = new StubServer();
    location.setObs(obs);
    final Entrance e1 = location.getEntranceById(1);
    e1.addObstruction(Obstruction.ZOMBIE);
    e1.addObstruction(Obstruction.ZOMBIE);
    assertTrue(e1.addObstruction(Obstruction.BARRICADE));
    assertEquals(2, location.numObstruction(Obstruction.ZOMBIE));
    assertEquals(2, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, location.numObstruction(Obstruction.BARRICADE));
    assertEquals(1, e1.numObstruction(Obstruction.BARRICADE));
    assertEquals(9, location.numObstruction(Obstruction.EMPTY));
    assertEquals(0, e1.numObstruction(Obstruction.EMPTY));
    assertTrue(e1.removeObstruction(Obstruction.ZOMBIE));
    assertEquals(1, location.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, location.numObstruction(Obstruction.BARRICADE));
    assertEquals(1, e1.numObstruction(Obstruction.BARRICADE));
    assertEquals(10, location.numObstruction(Obstruction.EMPTY));
    assertEquals(1, e1.numObstruction(Obstruction.EMPTY));
  }

  @Test
  void obstructionTest3() {
    final Location location = new Location(1, 4, "Station", 1, null);
    final Observer obs = new StubServer();
    location.setObs(obs);
    final Entrance e1 = location.getEntranceById(1);
    e1.addObstruction(Obstruction.ZOMBIE);
    e1.addObstruction(Obstruction.ZOMBIE);
    e1.addObstruction(Obstruction.BARRICADE);
    e1.removeObstruction(Obstruction.ZOMBIE);
    assertTrue(e1.removeObstruction(Obstruction.BARRICADE));
    assertEquals(1, location.numObstruction(Obstruction.ZOMBIE));
    assertEquals(1, e1.numObstruction(Obstruction.ZOMBIE));
    assertEquals(0, location.numObstruction(Obstruction.BARRICADE));
    assertEquals(0, e1.numObstruction(Obstruction.BARRICADE));
    assertEquals(11, location.numObstruction(Obstruction.EMPTY));
    assertEquals(2, e1.numObstruction(Obstruction.EMPTY));
  }

  @Test
  void cardTest() {
    final Card card1 = new Fuel(2);
    final Card card2 = new Food(3, 5);
    final Card card3 = new Lock(4);
    final Card card4 = new Medicine(5);
    final List<Card> cardList = new ArrayList<>(4);
    cardList.add(card1);
    cardList.add(card2);
    cardList.add(card3);
    cardList.add(card4);
    final Location location = new Location(1, 3, "Station", 1, cardList);
    final Observer obs = new StubServer();
    location.setObs(obs);
    for (int i = 0; i < 2; i++) {
      assertEquals(4 - 2 * i, location.numCards());
      final List<Card> cards = location.getCards(2);
      assertEquals(2, cards.size());
    }
    assertEquals(0, location.numCards());
    assertNull(location.getCards(2));
  }
}
