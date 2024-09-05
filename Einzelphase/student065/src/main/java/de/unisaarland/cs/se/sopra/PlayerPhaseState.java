package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.abilities.WoundingAbilityVisitor;
import de.unisaarland.cs.se.sopra.commands.Command;
import de.unisaarland.cs.se.sopra.commands.KillSurvivorCommand;
import de.unisaarland.cs.se.sopra.commands.LeaveCommand;
import de.unisaarland.cs.se.sopra.crossroad.Consequence;
import de.unisaarland.cs.se.sopra.crossroad.ConsequenceVisitor;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.model.Crisis;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.List;
import org.slf4j.LoggerFactory;
import sopra.comm.TimeoutException;

public final class PlayerPhaseState extends State {

    private static PlayerPhaseState instance;

    private PlayerPhaseState() {
        // Do nothing
    }

    public static PlayerPhaseState getInstance() {
        if (instance == null) {
            instance = new PlayerPhaseState();
        }
        return instance;
    }

    @Override
    public boolean inGame() {
        return true;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        connection.sendNextRound(model.getRounds());
        model.nextCrisis();
        final Crisis crisis = model.getCrisis();
        connection.sendCrisis(crisis.getId());
        // send crisis
        model.setCurrentPlayer(0);
        final List<Player> sortedPlayers = model.getPlayers();
        for (final Player player : sortedPlayers) {
            if (player.hasLeft()) {
                continue;
            }
            final int numSurvivors = player.getSurvivors().size();
            for (int i = 0; i <= numSurvivors; i++) {
                final int die = model.rollActionDie();
                player.addDie(die);
                connection.sendDieRolled(player.getId(), die);
            }
        }

        Player player;
        while (model.getGameState().gameRunning()
                && (player = model.getCurrentPlayer()) != null) {
            if (player.hasLeft()) {
                model.nextPlayer();
                continue;
            }

            processFrostbites(player, model, connection);

            model.nextCrossroad();
            final Crossroad crossroad = model.getCrossroad();
            player.setCrossroad(crossroad);

            player.setNumTrashTrigger(model.getTrashPile() + player.getCrossroad().getNumTrash());

            final int crossroadId = player.getCrossroad().getId();
            final int numConsequences = player.getCrossroad().getConsequences().size();
            LoggerFactory.getLogger(PlayerPhaseState.class)
                    .debug("CROSSROAD ID: " + crossroadId + "!!! NUMBER CONSEQUENCES: "
                            + numConsequences);

            while (model.isCurrentPlayer(player.getId()) && model.getGameState().gameRunning()) {
                connection.sendActNow(model.getCommId(player.getId()));
                handleCommand(model, player, connection);
                if (player.getCrossroadActivate() && model.getGameState().gameRunning()
                        && this.remainPlayer(model)) {
                    processCrossroad(model, player, connection);
                }
            }
        }
        if (model.getGameState().gameRunning()) {
            model.setGameState(ColonyPhaseState.getInstance());
        }
    }

    private void handleCommand(
            final Model model, final Player player, final ConnectionWrapper connection) {
        final Command command = nextCommand(model, player, connection);
        command.execute(model, connection);
        if (!command.checkPlayer(model.getCommId(player.getId()))) {
            handleCommand(model, player, connection);
        }
    }

    private Command nextCommand(
            final Model model, final Player player, final ConnectionWrapper connection) {
        try {
            return connection.nextCommand();
        } catch (final TimeoutException e) {
            return new LeaveCommand(model.getCommId(player.getId()));
        }
    }

    /**
     * Frostbites turn into more wounds.
     *
     * @param player     The player which survivors get checked.
     * @param model      To possibly kill the survivor.
     * @param connection To inform all players.
     */
    private void processFrostbites(
            final Player player, final Model model, final ConnectionWrapper connection) {
        for (final Survivor s : player.getSurvivors()) {
            for (int i = 0; i < s.getTimesFrostbitten(); i++) {
                s.addWound();
                connection.sendWounded(s.getId());
                if (s.getTotalWounds() >= WoundingAbilityVisitor.MAX_TIMES_WOUNDED) {
                    final KillSurvivorCommand command =
                            new KillSurvivorCommand(model.getCommId(player.getId()), player, s);
                    command.execute(model, connection);
                    if (!model.getGameState().gameRunning()) {
                        return;
                    }
                    break;
                }
            }
        }
    }

    private void processCrossroad(final Model model, final Player player,
                                  final ConnectionWrapper connection) {
        final Crossroad crossroad = player.getCrossroad();
        connection.sendCrossroad(crossroad.getId());

        final ConsequenceVisitor consequenceVisitor = new ConsequenceVisitor(model, connection);
        Consequence finalConsequence = crossroad.getConsequences().get(0);
        boolean answer = true;
        if (crossroad.getConsequences().size() > 1) {
            model.setVotePhase(true);
            final List<Player> sortedPlayers = model.getPlayers();
            for (final Player votePlayer : sortedPlayers) {
                model.setCurrentVotePlayer(votePlayer);
                votePlayer.setHasVoted(false);
                while (!votePlayer.hasVoted() && !votePlayer.hasLeft()) {
                    connection.sendVoteNow(model.getCommId(votePlayer.getId()));
                    handleCommand(model, votePlayer, connection);
                }
            }
            model.setVotePhase(false);
            if (model.getNumVote1() < model.getNumVote2()) {
                finalConsequence = crossroad.getConsequences().get(1);
                answer = false;
            }
            connection.sendVoteResult(answer);
            model.setNumVote1(0);
            model.setNumVote2(0);
        }
        finalConsequence.accept(consequenceVisitor);
        player.setNumTrashTrigger(0);
        player.setCrossroadActivate(false);
        player.setCrossroad(null);
    }

    private boolean remainPlayer(final Model model) {
        for (final Player p : model.getPlayers()) {
            if (!p.hasLeft()) {
                return true;
            }
        }
        return false;
    }
}
