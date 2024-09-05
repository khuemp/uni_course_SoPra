package de.unisaarland.cs.se.sopra.controlstructures.passivabilities;

import de.unisaarland.cs.se.sopra.controlstructures.command.Move;
import de.unisaarland.cs.se.sopra.maingame.Wound;
import java.util.EnumMap;

public class WoundAbility extends PassiveAbility {
  private final EnumMap<Wound, Wound> woundType = new EnumMap<>(Wound.class);

  public WoundAbility(final Wound before, final Wound after) {
    this.woundType.put(Wound.BITE, Wound.BITE);
    this.woundType.put(Wound.WOUND, Wound.WOUND);
    this.woundType.put(Wound.FROSTBITE, Wound.FROSTBITE);
    this.woundType.put(Wound.NONE, Wound.NONE);
    this.woundType.put(before, after);
  }

  @Override
  public void modify(final Move m) {
    m.setWoundType(this.woundType);
    m.setModified(true);
  }

}
