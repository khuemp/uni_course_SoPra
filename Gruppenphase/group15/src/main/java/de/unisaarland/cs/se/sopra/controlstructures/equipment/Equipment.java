package de.unisaarland.cs.se.sopra.controlstructures.equipment;

import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.controlstructures.card.CardVisitor;
import de.unisaarland.cs.se.sopra.controlstructures.command.Attack;
import de.unisaarland.cs.se.sopra.controlstructures.command.Barricade;
import de.unisaarland.cs.se.sopra.controlstructures.command.Move;
import de.unisaarland.cs.se.sopra.controlstructures.command.Search;
import de.unisaarland.cs.se.sopra.controlstructures.command.Trash;
import de.unisaarland.cs.se.sopra.maingame.CrisisCardType;
import de.unisaarland.cs.se.sopra.maingame.Location;
import java.util.Map;

public abstract class Equipment implements Card {
  protected int cardId;
  int lastUsed;

  public int getLastUsed() {
    return this.lastUsed;
  }

  public void setLastUsed(final int lastUsed) {
    this.lastUsed = lastUsed;
  }

  public abstract void modify(final Trash t);

  public abstract void modify(final Attack a);

  public abstract void modify(final Search s);

  public abstract void modify(final Move m);

  public abstract void modify(final Barricade b);

  @Override
  public void accept(final CardVisitor v) {
    v.visit(this);
  }

  @Override
  public CrisisCardType checkCrisisType() {
    return CrisisCardType.STUFF;
  }

  @Override
  public int getId() {
    return this.cardId;
  }

  @Override
  public void checkLocIdValid(final Map<Integer, Location> locationMap, final int colonyId) {
    // empty
  }

}
