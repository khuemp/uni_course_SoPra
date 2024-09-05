package de.unisaarland.cs.se.sopra.commands;

import sopra.comm.CommandFactory;

public class ServerCommandFactory implements CommandFactory<Command> {

    @Override
    public Command createAttack(final int commId, final int survivorId, final int entrance) {
        return new AttackCommand(commId, survivorId, entrance);
    }

    @Override
    public Command createBarricade(final int commId, final int survivorId, final int entrance) {
        return new BarricadeCommand(commId, survivorId, entrance);
    }

    @Override
    public Command createCleanWaste(final int commId, final int survivorId) {
        return new CleanWasteCommand(commId, survivorId);
    }

    @Override
    public Command createContributeCard(final int commId, final int cardId) {
        return new ContributeCardCommand(commId, cardId);
    }

    @Override
    public Command createEndTurn(final int commId) {
        return new EndTurnCommand(commId);
    }

    @Override
    public Command createLeave(final int commId) {
        return new LeaveCommand(commId);
    }

    @Override
    public Command createMove(final int commId, final int survivorId, final int locationId) {
        return new MoveCommand(commId, survivorId, locationId);
    }

    @Override
    public Command createRegister(final int commId, final String playerName) {
        return new RegisterCommand(commId, playerName);
    }

    @Override
    public Command createSearch(final int commId, final int survivorId) {
        return new SearchCommand(commId, survivorId);
    }

    @Override
    public Command createSelectCharacters(final int commId, final int survivorId1,
                                          final int survivorId2) {
        return new SelectCharactersCommand(commId, survivorId1, survivorId2);
    }

    @Override
    public Command createStartGame(final int commId) {
        return new StartGameCommand(commId);
    }

    @Override
    public Command createUseAbility(final int commId, final int survivorId) {
        return new UseAbilityCommand(commId, survivorId);
    }

    @Override
    public Command createUseAbility(final int commId, final int survivorId, final int target) {
        return new UseTargetedAbilityCommand(commId, survivorId, target);
    }

    @Override
    public Command createUseCard(final int commId, final int cardId) {
        return new UseCardCommand(commId, cardId);
    }

    @Override
    public Command createUseCard(final int commId, final int cardId, final int survivorId,
                                 final int target) {
        return new UseTargetedCardCommand(commId, cardId, survivorId, target);
    }

    @Override
    public Command createVote(final int commId, final boolean vote) {
        return new VoteCommand(commId, vote);
    }

}
