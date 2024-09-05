package de.unisaarland.cs.se.sopra.server;

import de.unisaarland.cs.se.sopra.controlstructures.command.Attack;
import de.unisaarland.cs.se.sopra.controlstructures.command.Barricade;
import de.unisaarland.cs.se.sopra.controlstructures.command.Command;
import de.unisaarland.cs.se.sopra.controlstructures.command.ContributeCard;
import de.unisaarland.cs.se.sopra.controlstructures.command.EndTurn;
import de.unisaarland.cs.se.sopra.controlstructures.command.Leave;
import de.unisaarland.cs.se.sopra.controlstructures.command.Move;
import de.unisaarland.cs.se.sopra.controlstructures.command.Register;
import de.unisaarland.cs.se.sopra.controlstructures.command.Search;
import de.unisaarland.cs.se.sopra.controlstructures.command.SelectCharacter;
import de.unisaarland.cs.se.sopra.controlstructures.command.StartGame;
import de.unisaarland.cs.se.sopra.controlstructures.command.Trash;
import de.unisaarland.cs.se.sopra.controlstructures.command.UseCard;
import de.unisaarland.cs.se.sopra.controlstructures.command.UseNoTargetAbility;
import de.unisaarland.cs.se.sopra.controlstructures.command.UseTargetAbility;
import de.unisaarland.cs.se.sopra.controlstructures.command.UseTargetCard;
import org.slf4j.LoggerFactory;

public class CommandFactoryImplement implements sopra.comm.CommandFactory<Command> {

  @Override
  public Command createRegister(final int i, final String s) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create register: commId: {} name: {}", i, s);
    return new Register(i, s);
  }

  @Override
  public Command createLeave(final int i) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create leave: commId: {}", i);
    return new Leave(i);
  }

  @Override
  public Command createStartGame(final int i) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create start game: commId: {}", i);
    return new StartGame(i);
  }

  @Override
  public Command createSelectCharacters(final int i, final int i1, final int i2) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create select character: commId: {} characterId1: {} characterId2: {}", i, i1, i2);
    return new SelectCharacter(i, i1, i2);
  }

  @Override
  public Command createUseAbility(final int i, final int i1) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create Use Ability: commId: {} characterId: {}", i, i1);
    return new UseNoTargetAbility(i, i1);
  }

  @Override
  public Command createUseAbility(final int i, final int i1, final int i2) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create Use Ability: commId: {} characterId: {} target: {}", i, i1, i2);
    return new UseTargetAbility(i, i1, i2);
  }

  @Override
  public Command createBarricade(final int i, final int i1, final int i2) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create Barricade: commId: {} characterId: {} entrance: {}", i, i1, i2);
    return new Barricade(i, i1, i2);
  }

  @Override
  public Command createEndTurn(final int i) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create End Turn: commId: {}", i);
    return new EndTurn(i);
  }

  @Override
  public Command createMove(final int i, final int i1, final int i2) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "Create Move: commId: {} characterId: {} locationId: {}", i, i1, i2);
    return new Move(i, i1, i2);
  }

  @Override
  public Command createUseCard(final int i, final int i1) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create Use Card: commId: {} cardId: {}", i, i1);
    return new UseCard(i, i1);
  }

  @Override
  public Command createUseCard(final int i, final int i1, final int i2, final int i3) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create Use Card: commId: {} cardId: {} characterId: {} target: {}", i, i1, i2, i3);
    return new UseTargetCard(i, i1, i2, i3);
  }

  @Override
  public Command createContributeCard(final int i, final int i1) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create Contribute Card: commId: {} cardId: {}", i, i1);
    return new ContributeCard(i, i1);
  }

  @Override
  public Command createCleanWaste(final int i, final int i1) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create Clean Waste: commId: {} characterId: {}", i, i1);
    return new Trash(i, i1);
  }

  @Override
  public Command createAttack(final int i, final int i1, final int i2) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create Attack: commId: {} characterId: {} entrance: {}", i, i1, i2);
    return new Attack(i, i1, i2);
  }

  @Override
  public Command createSearch(final int i, final int i1) {
    LoggerFactory.getLogger(CommandFactoryImplement.class).trace(
        "create Search: commId: {} characterId: {}", i, i1);
    return new Search(i, i1);
  }
}

