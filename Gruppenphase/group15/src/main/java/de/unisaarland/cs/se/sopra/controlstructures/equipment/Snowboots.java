package de.unisaarland.cs.se.sopra.controlstructures.equipment;

import de.unisaarland.cs.se.sopra.controlstructures.command.Attack;
import de.unisaarland.cs.se.sopra.controlstructures.command.Barricade;
import de.unisaarland.cs.se.sopra.controlstructures.command.Move;
import de.unisaarland.cs.se.sopra.controlstructures.command.Search;
import de.unisaarland.cs.se.sopra.controlstructures.command.Trash;
import de.unisaarland.cs.se.sopra.maingame.Wound;
import java.util.EnumMap;

public class Snowboots extends Equipment {

  public Snowboots(final int id) {
    super.cardId = id;
  }

  @Override
  public void modify(final Trash t) {
    //
  }

  @Override
  public void modify(final Attack a) {
    //
  }

  @Override
  public void modify(final Search s) {
    //
  }

  @Override
  public void modify(final Move m) {
    final EnumMap<Wound, Wound> convertFrostbite = new EnumMap<>(Wound.class);
    convertFrostbite.put(Wound.BITE, Wound.BITE);
    convertFrostbite.put(Wound.FROSTBITE, Wound.WOUND);
    convertFrostbite.put(Wound.WOUND, Wound.WOUND);
    convertFrostbite.put(Wound.NONE, Wound.NONE);
    if (this.getLastUsed() != m.getCurrentRound()) {
      if (!m.getModified()) {
        m.setWoundType(convertFrostbite);
        this.setLastUsed(m.getCurrentRound());
        m.setModified(true);
      }
    }
  }

  @Override
  public void modify(final Barricade b) {
    //
  }
}
