package de.unisaarland.cs.se.sopra.maingame;

import de.unisaarland.cs.se.sopra.controlstructures.activeabilities.ActiveAbility;
import de.unisaarland.cs.se.sopra.controlstructures.equipment.Equipment;
import de.unisaarland.cs.se.sopra.controlstructures.passivabilities.PassiveAbility;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Character {

  private final int charId;
  private final int status;
  //private final Set<Wound> wounds = new LinkedHashSet<>();
  //TODO : hier beschweren mit detail aus spotbugs
  /*
  DLC_DUBIOUS_LIST_COLLECTION: Class defines List based fields but uses them like Sets
  This class defines a field based on java.util.List, but uses it to some extent like a Set.
  Since lookup type operations are performed using a linear search for Lists,
  the performance for large Lists will be poor.
  If the list is known to only contain a small number of items (3, 4, etc), then it doesn't matter.
  Otherwise, consider changing this field's implementation to a set-based one.
  If order of iteration is important to maintain insert order, perhaps consider a LinkedHashSet.
   */
  private final List<Wound> wounds = new ArrayList<>(3);
  private final List<Equipment> equipments = new ArrayList<>();
  private final PassiveAbility passiveAbility;
  private final ActiveAbility activeAbility;
  //private final Observer obs;
  private final int search;
  private final int attack;
  private final String name;
  private int locId;
  private int lastMove;

  public Character(final int charId, final int status, final PassiveAbility passiveAbility,
                   final ActiveAbility activeAbility, final int search, final int attack,
                   final int locId, final String name/*final Observer obs,*/) {
    this.charId = charId;
    this.status = status;
    this.passiveAbility = passiveAbility;
    this.activeAbility = activeAbility;
    this.search = search;
    this.attack = attack;
    this.locId = locId;
    //this.obs = obs;
    this.lastMove = 0;
    this.name = name;
  }

  public void wound(final Wound w) {
    this.wounds.add(w);
  }

  public int getSearchValue() {
    return this.search;
  }

  public int getAttackValue() {
    return this.attack;
  }

  public ActiveAbility getActiveAbility() {
    return this.activeAbility;
  }

  public PassiveAbility getPassiveAbility() {
    return this.passiveAbility;
  }

  /*
  public void checkEquipment(final CharCommand c) {
    final List<Equipment> reversedEquipments = new ArrayList<>();
    for (int i = equipments.size() - 1; i >= 0; i--) {
      reversedEquipments.add(equipments.get(i));
    }

    for (final Equipment e : reversedEquipments) {
      if (c.getModified()) {
        break;
      } else {
        //e.modify(c);
      }
    }
  }*/

  public int getId() {
    return this.charId;
  }

  public int getLocId() {
    return this.locId;
  }

  public void setLocId(final int locId) {
    this.locId = locId;
  }


  public int getSocialStatus() {
    return this.status;
  }

  public void addEquipment(final Equipment e) {
    this.equipments.add(e);
  }

  public boolean beHealed() {
    /*
    if (this.wounds.contains(Wound.FROSTBITE)) {
      this.wounds.remove(Wound.FROSTBITE);
    } else {
      this.wounds.remove(Wound.WOUND);
    }*/
    final boolean removed = this.wounds.remove(Wound.FROSTBITE);
    boolean removed2 = false;
    if (!removed) {
      removed2 = this.wounds.remove(Wound.WOUND);
    }
    return removed || removed2;
  }

  public int numWounds() {
    return this.wounds.size();
  }

  public int getLastMove() {
    return this.lastMove;
  }

  public void setLastMove(final int round) {
    this.lastMove = round;
  }

  public boolean hasFrostbite() {
    return this.wounds.contains(Wound.FROSTBITE);
  }

  /*
  public List<Wound> getWounds(){
    return wounds;
  }*/

  public List<Equipment> getEquipments() {
    return this.equipments;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    final Character character = (Character) o;
    return this.charId == character.charId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.charId);
  }

  public String getName() {
    return this.name;
  }

  public List<Wound> getWounds() {
    return this.wounds;
  }
}
