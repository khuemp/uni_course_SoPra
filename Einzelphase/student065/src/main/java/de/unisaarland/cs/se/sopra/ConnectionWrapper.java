package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.commands.Command;
import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;
import sopra.comm.FoodChange;
import sopra.comm.MoralChange;
import sopra.comm.ServerConnection;
import sopra.comm.TimeoutException;

public class ConnectionWrapper implements Closeable {

    private final ServerConnection<Command> connection;
    private final Set<Integer> playerCommIds;

    public ConnectionWrapper(final ServerConnection<Command> connection) {
        this.connection = connection;
        this.playerCommIds = new HashSet<>();
    }

    public void addCommId(final int commId) {
        playerCommIds.add(commId);
    }

    public void removeCommId(final int commId) {
        playerCommIds.remove(commId);
    }

    public final Command nextCommand() throws TimeoutException {
        return connection.nextCommand();
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Individual
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public final void sendConfig(final int commId, final String config) {
        connection.sendConfig(commId, config);
    }

    public final void sendActNow(final int commId) {
        connection.sendActNow(commId);
    }

    public final void sendCharacters(final int commId, final int survivorId0,
                                     final int survivorId1,
                                     final int survivorId2, final int survivorId3) {
        connection.sendCharacters(commId, survivorId0, survivorId1, survivorId2, survivorId3);
    }

    public final void sendCommandFailed(final int commId, final String message) {
        connection.sendCommandFailed(commId, message);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Broadcast
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    public final void sendPlayer(final int player, final String playerName) {
        for (final int id : playerCommIds) {
            connection.sendPlayer(id, player, playerName);
        }
    }

    public final void sendGameEnd(final boolean win) {
        for (final int id : playerCommIds) {
            connection.sendGameEnd(id, win);
        }
    }

    public final void sendGameStarted() {
        for (final int id : playerCommIds) {
            connection.sendGameStarted(id);
        }
    }

    public final void sendCharacterSpawned(final int player, final int survivorId) {
        for (final int id : playerCommIds) {
            connection.sendCharacterSpawned(id, player, survivorId);
        }
    }

    public final void sendNextRound(final int round) {
        for (final int id : playerCommIds) {
            connection.sendNextRound(id, round);
        }
    }

    public final void sendRegistrationAborted() {
        for (final int id : playerCommIds) {
            connection.sendRegistrationAborted(id);
        }
    }


    public final void sendZombieSpawned(final int locationId, final int entrance) {
        for (final int id : playerCommIds) {
            connection.sendZombieSpawned(id, locationId, entrance);
        }
    }

    public final void sendCardDrawn(final int player, final int cardId) {
        for (final int id : playerCommIds) {
            connection.sendCardDrawn(id, player, cardId);
        }
    }

    public final void sendCrisis(final int crisisId) {
        for (final int id : playerCommIds) {
            connection.sendCrisis(id, crisisId);
        }
    }

    public final void sendDieRolled(final int player, final int value) {
        for (final int id : playerCommIds) {
            connection.sendDieRolled(id, player, value);
        }
    }

    public final void sendAbilityUsed(final int survivorId) {
        for (final int id : playerCommIds) {
            connection.sendAbilityUsed(id, survivorId);
        }
    }

    public final void sendAbilityUsed(final int survivorId, final int target) {
        for (final int id : playerCommIds) {
            connection.sendAbilityUsed(id, survivorId, target);
        }
    }

    public final void sendBarricaded(final int survivorId, final int locationId,
                                     final int entrance) {
        for (final int id : playerCommIds) {
            connection.sendBarricaded(id, survivorId, locationId, entrance);
        }
    }

    public final void sendMoved(final int survivorId, final int locationId) {
        for (final int id : playerCommIds) {
            connection.sendMoved(id, survivorId, locationId);
        }
    }

    public final void sendFrostbitten(final int survivorId) {
        for (final int id : playerCommIds) {
            connection.sendFrostbitten(id, survivorId);
        }
    }

    public final void sendWounded(final int survivorId) {
        for (final int id : playerCommIds) {
            connection.sendWounded(id, survivorId);
        }
    }

    public final void sendContributed(final int player, final int cardId) {
        for (final int id : playerCommIds) {
            connection.sendContributed(id, player, cardId);
        }
    }

    public final void sendCardUsed(final int cardId) {
        for (final int id : playerCommIds) {
            connection.sendCardUsed(id, cardId);
        }
    }

    public final void sendCardUsed(final int cardId, final int survivorId, final int target) {
        for (final int id : playerCommIds) {
            connection.sendCardUsed(id, cardId, survivorId, target);
        }
    }

    public final void sendFoodChanged(final int amount, final FoodChange reason) {
        for (final int id : playerCommIds) {
            connection.sendFoodChanged(id, amount, reason);
        }
    }

    public final void sendColonyPhaseStarted() {
        for (final int id : playerCommIds) {
            connection.sendColonyPhaseStarted(id);
        }
    }

    public final void sendStarvationTokenAdded() {
        for (final int id : playerCommIds) {
            connection.sendStarvationTokenAdded(id);
        }
    }

    public final void sendMoralChanged(final int amount, final MoralChange reason) {
        for (final int id : playerCommIds) {
            connection.sendMoralChanged(id, amount, reason);
        }
    }

    public final void sendWasteChanged(final int amount) {
        for (final int id : playerCommIds) {
            connection.sendWasteChanged(id, amount);
        }
    }

    public final void sendSurvivorKilled(final int survivorId) {
        for (final int id : playerCommIds) {
            connection.sendSurvivorKilled(id, survivorId);
        }
    }

    public final void sendChildKilled() {
        for (final int id : playerCommIds) {
            connection.sendChildKilled(id);
        }
    }

    public final void sendBitten(final int survivorId) {
        for (final int id : playerCommIds) {
            connection.sendBitten(id, survivorId);
        }
    }

    public final void sendBitten() {
        for (final int id : playerCommIds) {
            connection.sendBitten(id);
        }
    }

    public final void sendBarricadeDestroyed(final int locationId, final int entrance) {
        for (final int id : playerCommIds) {
            connection.sendBarricadeDestroyed(id, locationId, entrance);
        }
    }

    public final void sendZombieKilled(final int survivorId, final int locationId,
                                       final int entrance) {
        for (final int id : playerCommIds) {
            connection.sendZombieKilled(id, survivorId, locationId, entrance);
        }
    }

    public final void sendSearched(final int survivorId, final int locationId) {
        for (final int id : playerCommIds) {
            connection.sendSearched(id, survivorId, locationId);
        }
    }

    public final void sendChildSpawned() {
        for (final int id : playerCommIds) {
            connection.sendChildSpawned(id);
        }
    }

    public void sendLeft(final int commId, final int playerLeftId) {
        for (final int id : playerCommIds) {
            connection.sendLeft(id, playerLeftId);
        }
        removeCommId(commId);
    }

    public final void sendCrossroad(final int crossroadId) {
        for (final int id : playerCommIds) {
            connection.sendCrossroad(id, crossroadId);
        }
    }

    public final void sendVoteNow(final int commId) {
        connection.sendVoteNow(commId);
    }

    public final void sendVoteResult(final boolean answer) {
        for (final int id : playerCommIds) {
            connection.sendVoteResult(id, answer);
        }
    }

    @Override
    public void close() {
        this.connection.close();
    }
}
