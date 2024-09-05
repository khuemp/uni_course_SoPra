package de.unisaarland.cs.se.sopra.controlstructures.passivabilities;

import de.unisaarland.cs.se.sopra.controlstructures.command.Attack;

public class NoInfectionAbility extends PassiveAbility {


  @Override
  public void modify(final Attack a) {
    a.setInfectionDiceRolled(false);
    a.setModified(true);
  }

}
