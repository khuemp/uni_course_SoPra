package de.unisaarland.cs.se.sopra.server;

import de.unisaarland.cs.se.sopra.controlstructures.command.Command;
import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.maingame.Player;
import de.unisaarland.cs.se.sopra.maingame.State;
import java.util.Comparator;
import java.util.List;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import sopra.comm.FoodChange;
import sopra.comm.MoralChange;
import sopra.comm.ServerConnection;
import sopra.comm.TimeoutException;

public class Server implements Observer, AutoCloseable {

  private final ServerConnection<Command> connection;
  private Game game;
  private boolean commandFailed;
  private int commandFailedCommId;

  public Server(final int port, final int timeout) {
    this.connection = new ServerConnection<>(port, timeout * 1000, new CommandFactoryImplement());
    LoggerFactory.getLogger(Server.class).debug("ServerConnection: {} {} {}",
        port, timeout, this.connection);
  }

  @Override
  public void close() {
    this.connection.close();
  }

  private void runRegistrationPhase() throws TimeoutException {
    while (this.game.getPhase() == State.REGISTRATION
        && this.game.getCommIdList().size() < this.game.getMaxPlayers()
        && !this.game.getGameEnded()) {
      final Command c = this.connection.nextCommand();
      c.execute(this.game);
    }
  }

  private void takeCommand(final Player p) throws TimeoutException {
    while (!p.getTurnEnded() && !this.game.getGameEnded()) {
      this.notifyActNow(p.getCommId());

      this.setCommandFailedTrue();
      while (this.commandFailed) {
        this.resetCommandFailed();
        final Command c = this.connection.nextCommand();
        c.execute(this.game);
        if (this.game.getGameEnded()) {
          return;
        }
        if (this.commandFailedCommId == p.getCommId()) {
          break;
        }
      }

    }
  }

  private void runPlayerPhase() throws TimeoutException {
    this.game.beginPlayerPhase();
    //todo : game
    if (this.game.getGameEnded()) {
      return;
    }
    // auf commands von den spielern warten und checken ob der zug beendet wurde
    final Comparator<Player> comp = (o1, o2) -> {
      final Integer id1 = o1.getId();
      final Integer id2 = o2.getId();
      return id1.compareTo(id2);
    };
    final List<Player> playersList = this.game.getPlayersList();
    playersList.sort(comp);
    for (final Player p : playersList) {
      if (p == null) {
        break;
      }
      p.setTurnEnded(false);
      this.takeCommand(p);
    }
  }

  public void runServer() throws TimeoutException {
    LoggerFactory.getLogger(Server.class).trace("Server is run");
    LoggerFactory.getLogger(Server.class).trace("Starting Registration Phase");
    this.game.setPhase(State.REGISTRATION);
    this.runRegistrationPhase();
    if (this.game.getGameEnded()) {
      return;
    }
    LoggerFactory.getLogger(Server.class).trace("Starting Preparation Phase");
    this.game.setPhase(State.PREPARATION);
    this.game.beginPreparationPhase();

    while (!this.game.getGameEnded()) {
      LoggerFactory.getLogger(Server.class).trace("Starting Player Phase");
      this.game.setPhase(State.PLAYER);
      this.runPlayerPhase();
      LoggerFactory.getLogger(Server.class).trace("Starting Colony Phase");
      this.game.setPhase(State.COLONY);
      this.game.beginColonyPhase();
    }
  }


  public void giveGame(final Game game) {
    this.game = game;
  }


  @Override
  public void broadcastRegistrationAborted() {
    LoggerFactory.getLogger(Server.class).trace("broadcast Registration Aborted");
    this.game.setGameEnded();
    for (final int i : this.game.getCommIdList()) {
      this.connection.sendRegistrationAborted(i);
    }
  }

  @Override
  public void broadcastPlayer(final int player, final String playerName) {
    final StringBuilder s = new StringBuilder("broadcast Player: Name: ");
    s.append(playerName).append("PlayerId: ").append(player);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendPlayer(i, player, playerName);
    }
  }

  @Override
  public void broadcastCharacterSpawned(final int player, final int characterId) {
    final StringBuilder s = new StringBuilder("broadcast Character Spawned: CharacterId: ");
    s.append(characterId).append(" PlayerId: ").append(player);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendCharacterSpawned(i, player, characterId);
    }
  }

  @Override
  public void broadcastZombieSpawned(final int locationId, final int entrance) {
    final StringBuilder s = new StringBuilder("broadcast Zombie Spawned: LocationId: ");
    s.append(locationId).append(" entrance: ").append(entrance);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendZombieSpawned(i, locationId, entrance);
    }
  }

  @Override
  public void broadcastChildSpawned() {
    LoggerFactory.getLogger(Server.class).trace("broadcast Child spawned");

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendChildSpawned(i);
    }
  }

  @Override
  public void broadcastCardDrawn(final int player, final int cardId) {
    final StringBuilder s = new StringBuilder("broadcast Card Drawn: PlayerId: ");
    s.append(player).append(" CardId: ").append(cardId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendCardDrawn(i, player, cardId);
    }
  }

  @Override
  public void broadcastGameEnd(final boolean win) {
    final StringBuilder s = new StringBuilder("broadcast Game End: ");
    if (win) {
      s.append("won");
    } else {
      s.append("lost");
    }
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    this.game.setGameEnded();
    for (final int i : this.game.getCommIdList()) {
      this.connection.sendGameEnd(i, win);
    }
  }

  @Override
  public void broadcastGameStarted() {
    LoggerFactory.getLogger(Server.class).trace("broadcast Game started");

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendGameStarted(i);
    }
  }

  @Override
  public void broadcastNextRound(final int round) {
    final StringBuilder s = new StringBuilder("broadcast Next Round: ");
    s.append(round);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendNextRound(i, round);
    }
  }

  @Override
  public void broadcastCrisis(final int crisisId) {
    final StringBuilder s = new StringBuilder("broadcast Crisis: CrisisId");
    s.append(crisisId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendCrisis(i, crisisId);
    }
  }

  @Override
  public void broadcastDieRolled(final int player, final int value) {
    final StringBuilder s = new StringBuilder("broadcast Dice Rolled: PlayerId: ");
    s.append(player).append(" Value: ").append(value);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendDieRolled(i, player, value);
    }
  }

  @Override
  public void broadcastAbilityUsed(final int characterId) {
    final StringBuilder s = new StringBuilder("broadcast Ability Used: CharacterId: ");
    s.append(characterId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendAbilityUsed(i, characterId);
    }
  }

  @Override
  public void broadcastAbilityUsed(final int characterId, final int target) {
    final StringBuilder s = new StringBuilder("broadcast Ability Used: CharacterId: ");
    s.append(characterId).append(" target: ").append(target);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendAbilityUsed(i, characterId, target);
    }
  }

  @Override
  public void broadcastBarricaded(final int characterId, final int locationId, final int entrance) {
    final StringBuilder s = new StringBuilder(128);
    s.append("broadcast Barricaded: CharacterId: ")
        .append(characterId).append(" LocationId: ").append(locationId)
        .append(" entrance: ").append(entrance);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendBarricaded(i, characterId, locationId, entrance);
    }
  }

  @Override
  public void broadcastMoved(final int characterId, final int locationId) {
    final StringBuilder s = new StringBuilder("broadcast Moved: CharacterId: ");
    s.append(characterId).append(" locationId: ").append(locationId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendMoved(i, characterId, locationId);
    }
  }

  @Override
  public void broadcastFrostbitten(final int characterId) {
    final StringBuilder s = new StringBuilder("broadcast Frostbitten: CharacterId: ");
    s.append(characterId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendFrostbitten(i, characterId);
    }
  }

  @Override
  public void broadcastWounded(final int characterId) {
    final StringBuilder s = new StringBuilder("broadcast wounded: CharacterId: ");
    s.append(characterId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendWounded(i, characterId);
    }
  }

  @Override
  public void broadcastBitten(final int characterId) {
    final StringBuilder s = new StringBuilder("broadcast bitten: CharacterId: ");
    s.append(characterId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendBitten(i, characterId);
    }
  }

  //todo why does this exist
  @Override
  public void broadcastBitten() {
    LoggerFactory.getLogger(Server.class).trace("broadcast Bitten without CharacterId??");

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendBitten(i);
    }
  }

  @Override
  public void broadcastContributed(final int player, final int cardId) {
    final StringBuilder s = new StringBuilder("broadcast Contributed: PlayerId: ");
    s.append(player).append(" cardId: ").append(cardId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendContributed(i, player, cardId);
    }
  }

  @Override
  public void broadcastCardUsed(final int cardId) {
    final StringBuilder s = new StringBuilder("broadcast card used: cardId: ");
    s.append(cardId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendCardUsed(i, cardId);
    }
  }

  @Override
  public void broadcastCardUsed(final int cardId, final int characterId, final int target) {
    final StringBuilder s = new StringBuilder("broadcast card used: cardId: ");
    s.append(cardId).append(" target: ").append(target);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendCardUsed(i, cardId, characterId, target);
    }
  }

  @Override
  public void broadcastFoodChanged(final int amount, final FoodChange reason) {
    final StringBuilder s = new StringBuilder("broadcast food changed: amount: ");
    s.append(amount).append(" reason: ").append(reason);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendFoodChanged(i, amount, reason);
    }
  }

  @Override
  public void broadcastColonyPhaseStarted() {
    LoggerFactory.getLogger(Server.class).trace("broadcast Colony Phase Started");

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendColonyPhaseStarted(i);
    }
  }

  @Override
  public void broadcastStarvationTokenAdded() {
    LoggerFactory.getLogger(Server.class).trace("broadcast Starvation Token Added");

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendStarvationTokenAdded(i);
    }
  }

  @Override
  public void broadcastMoralChanged(final int amount, final MoralChange reason) {
    final StringBuilder s = new StringBuilder("broadcast Moral changed: amount: ");
    s.append(amount).append(" reason: ").append(reason);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendMoralChanged(i, amount, reason);
    }
  }

  @Override
  public void broadcastSurvivorKilled(final int characterId) {
    final StringBuilder s = new StringBuilder("broadcast survivor killed: characterId: ");
    s.append(characterId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendSurvivorKilled(i, characterId);
    }
  }

  @Override
  public void broadcastChildKilled() {
    LoggerFactory.getLogger(Server.class).trace("broadcast Child killed");

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendChildKilled(i);
    }
  }

  @Override
  public void broadcastBarricadeDestroyed(final int locationId, final int entrance) {
    final StringBuilder s = new StringBuilder("broadcast Barricade Destroyed: locationId: ");
    s.append(locationId).append(" entrance: ").append(entrance);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendBarricadeDestroyed(i, locationId, entrance);
    }
  }

  @Override
  public void broadcastWasteChanged(final int amount) {
    final StringBuilder s = new StringBuilder("broadcast Waste Changed: amount: ");
    s.append(amount);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendWasteChanged(i, amount);
    }
  }

  @Override
  public void broadcastZombieKilled(final int characterId, final int locationId,
                                    final int entrance) {
    final StringBuilder s = new StringBuilder(128);
    s.append("broadcast Zombie Killed: characterId: ")
        .append(characterId).append(" locationId: ").append(locationId)
        .append(" entrance: ").append(entrance);
    LoggerFactory.getLogger(Server.class).trace(s.toString());


    for (final int i : this.game.getCommIdList()) {
      this.connection.sendZombieKilled(i, characterId, locationId, entrance);
    }
  }

  @Override
  public void broadcastSearched(final int characterId, final int locationId) {
    final StringBuilder s = new StringBuilder("broadcast searched: characterId: ");
    s.append(characterId).append(" locationId: ").append(locationId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendSearched(i, characterId, locationId);
    }
  }

  @Override
  public void broadcastLeft(final int player) {
    final StringBuilder s = new StringBuilder("broadcast Left: playerId: ");
    s.append(player);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    for (final int i : this.game.getCommIdList()) {
      this.connection.sendLeft(i, player);
    }
  }

  @Override
  public void notifyConfig(final int commId, final JSONObject config) {
    final StringBuilder s = new StringBuilder("notify Config: commId: ");
    s.append(commId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    this.connection.sendConfig(commId, String.valueOf(config));
  }

  @Override
  public void notifyActNow(final int commId) {
    final StringBuilder s = new StringBuilder("notify Act Now: commId: ");
    s.append(commId);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    this.connection.sendActNow(commId);
  }

  @Override
  public void notifyCharacters(final int commId, final int characterId0, final int characterId1,
                               final int characterId2, final int characterId3)
      throws TimeoutException {
    final StringBuilder s = new StringBuilder(128);
    s.append("notify Characters: commId: ")
        .append(commId).append(" characterIds: ")
        .append(characterId0).append(' ')
        .append(characterId1).append(' ')
        .append(characterId2).append(' ')
        .append(characterId3).append(' ');
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    this.connection.sendCharacters(commId, characterId0, characterId1, characterId2, characterId3);
    this.game.setCharacterSelectCommId(commId);
    this.setCommandFailedTrue();
    while (this.commandFailed) {
      this.resetCommandFailed();
      final Command c = this.connection.nextCommand();
      c.execute(this.game);
    }


  }

  private void resetCommandFailed() {
    this.commandFailed = false;
  }

  private void setCommandFailedTrue() {
    this.commandFailed = true;
  }


  @Override
  public void notifyCommandFailed(final int commId, final String message) {
    final StringBuilder s = new StringBuilder("notify Command Failed: ");
    s.append(message);
    LoggerFactory.getLogger(Server.class).trace(s.toString());

    this.commandFailed = true;
    this.commandFailedCommId = commId;

    this.connection.sendCommandFailed(commId, message);
  }

}