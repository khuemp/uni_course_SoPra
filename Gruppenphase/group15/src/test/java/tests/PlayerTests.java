package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.controlstructures.card.Food;
import de.unisaarland.cs.se.sopra.controlstructures.card.Fuel;
import de.unisaarland.cs.se.sopra.controlstructures.card.Medicine;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.server.Observer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerTests {

  @Test
  void initialisationTest() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    assertFalse(player.getTurnEnded());
    assertEquals(1337, player.getCommId());
    assertEquals(23, player.getId());
    assertEquals(1, player.getNumDice());
    assertEquals("ich", player.getName());
  }

  @Test
  void turnEndedTest() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    assertFalse(player.getTurnEnded());
    player.setTurnEnded(false);
    assertFalse(player.getTurnEnded());
    player.setTurnEnded(true);
    assertTrue(player.getTurnEnded());
  }

  @Test
  void checkDiceTest() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    assertFalse(player.checkDiceExist());
    player.addDice(3);
    player.addDice(2);
    player.addDice(6);
    assertTrue(player.checkDiceExist());
  }

  @Test
  void checkDiceTest2() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    final Character character = new Character(3, 5, null, null, 4, 2, 42, "anne");
    player.addCharacter(character);
    assertTrue(player.charactersLeft());
    player.addDice(3);
    player.addDice(2);
    player.addDice(6);
    assertTrue(player.checkDiceValue(3, true));
  }

  @Test
  void checkDiceTest3() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    final Character character = new Character(3, 5, null, null, 4, 2, 42, "anne");
    player.addCharacter(character);
    assertTrue(player.charactersLeft());
    player.addDice(1);
    assertFalse(player.checkDiceValue(3, false));
    player.addDice(2);
    assertTrue(player.checkDiceValue(3, false));
  }

  @Test
  void checkDiceKillValue() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    final Character character = new Character(3, 5, null, null, 4, 2, 42, "anne");
    player.addCharacter(character);
    player.addDice(3);
    player.addDice(2);
    assertFalse(player.checkDiceValueKill(4));
    player.addDice(6);
    assertTrue(player.checkDiceValueKill(4));
  }

  @Test
  void removeDiceValue() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    player.addDice(3);
    player.addDice(2);
    assertTrue(player.removeDiceValue(2));
    final List<Integer> rolls = player.getRollResults();
    assertEquals(1, rolls.size());
    assertFalse(rolls.contains(2));
    assertTrue(rolls.contains(3));
  }

  @Test
  void removeDiceValue2() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    player.addDice(4);
    player.addDice(2);
    assertTrue(player.removeDiceValue(3));
    final List<Integer> rolls = player.getRollResults();
    assertEquals(1, rolls.size());
    assertFalse(rolls.contains(4));
    assertTrue(rolls.contains(2));
  }

  @Test
  void removeDiceValue3() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    player.addDice(4);
    player.addDice(2);
    player.addDice(6);
    assertTrue(player.removeDice());
    final List<Integer> rolls = player.getRollResults();
    assertEquals(2, rolls.size());
    assertFalse(rolls.contains(2));
    assertTrue(rolls.contains(4));
    assertTrue(rolls.contains(6));
  }

  @Test
  void removeCharacterTest() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    final Character character = new Character(3, 5, null, null, 4, 2, 42, "anne");
    final Character character1 = new Character(5, 13, null, null, 2, 1, 23, "alex");
    player.addCharacter(character);
    player.addCharacter(character1);
    assertTrue(player.killCharacter(5));
    assertFalse(player.killCharacter(5));
    assertFalse(player.killCharacter(10));
  }

  @Test
  void charByIdTest() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    final Character character = new Character(3, 5, null, null, 4, 2, 42, "anne");
    final Character character1 = new Character(5, 13, null, null, 2, 1, 23, "alex");
    player.addCharacter(character);
    player.addCharacter(character1);
    final Character character2 = player.getCharById(character.getId());
    final Character character3 = player.getCharById(15);
    assertNotNull(character2);
    assertEquals("anne", character2.getName());
    assertNull(character3);
  }

  @Test
  void containsCharacterTest() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    final Character character = new Character(3, 5, null, null, 4, 2, 42, "anne");
    final Character character1 = new Character(5, 13, null, null, 2, 1, 23, "alex");
    player.addCharacter(character);
    player.addCharacter(character1);
    assertTrue(player.containsCharacter(3));
    assertTrue(player.containsCharacter(5));
    assertFalse(player.containsCharacter(12));
  }

  @Test
  void cardsTest() {
    final Observer obs = new StubServer();
    final Player player = new Player(23, 1337, obs, "ich");
    final Card card = new Food(1, 4);
    final Card card1 = new Fuel(2);
    final Card card2 = new Medicine(3);
    final Card card3 = new Fuel(4);
    final List<Card> cards = new ArrayList<>(4);
    cards.add(card3);
    cards.add(card2);
    cards.add(card1);
    cards.add(card);
    player.addCards(cards);
    for (int i = 0; i < 4; i++) {
      assertTrue(player.containsCard(i + 1));
    }
    assertFalse(player.removeCards(5));
    assertTrue(player.removeCards(4));
    assertFalse(player.containsCard(4));
    assertTrue(player.removeCards(1));
    assertFalse(player.containsCard(1));
  }
}
