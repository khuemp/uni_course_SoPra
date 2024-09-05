package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Wound;
import java.util.List;
import org.junit.jupiter.api.Test;

class CharacterTests {

  @Test
  void healingTest1Frostbite() {
    final Character character = new Character(2, 5, null, null, 6, 7, 42, "anne");
    character.wound(Wound.FROSTBITE);
    final List<Wound> woundList = character.getWounds();
    assertTrue(woundList.contains(Wound.FROSTBITE));
    assertEquals(1, character.numWounds());
    character.beHealed();
    assertEquals(0, character.numWounds());
    assertFalse(woundList.contains(Wound.FROSTBITE));
  }

  @Test
  void healingTest1Wound1Frost() {
    final Character character = new Character(2, 5, null, null, 6, 7, 42, "anne");
    character.wound(Wound.FROSTBITE);
    character.wound(Wound.WOUND);
    final List<Wound> woundList = character.getWounds();
    assertTrue(woundList.contains(Wound.FROSTBITE));
    assertTrue(woundList.contains(Wound.WOUND));
    assertEquals(2, character.numWounds());
    character.beHealed();
    assertEquals(1, character.numWounds());
    assertFalse(woundList.contains(Wound.FROSTBITE));
    assertTrue(woundList.contains(Wound.WOUND));
  }

  @Test
  void healingTestNoWounds() {
    final Character character = new Character(2, 5, null, null, 6, 7, 42, "anne");
    assertFalse(character.beHealed());
  }

  @Test
  void healingTest3Wounds() {
    final Character character = new Character(2, 5, null, null, 6, 7, 42, "anne");
    for (int i = 0; i < 3; i++) {
      character.wound(Wound.WOUND);
    }
    assertEquals(3, character.numWounds());
    assertTrue(character.beHealed());
    assertEquals(2, character.numWounds());
  }
}
