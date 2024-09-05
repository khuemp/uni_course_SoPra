package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.commands.Command;
import de.unisaarland.cs.se.sopra.commands.LeaveCommand;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.LinkedList;
import java.util.List;
import sopra.comm.TimeoutException;

public final class PreGameState extends State {

    private static PreGameState instance;

    private PreGameState() {
        // Do nothing..
    }

    public static PreGameState getInstance() {
        if (instance == null) {
            instance = new PreGameState();
        }
        return instance;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        connection.sendGameStarted();
        final List<Player> sortedPlayers = model.getPlayers();
        for (final Player player : sortedPlayers) {
            connection.sendPlayer(player.getId(), player.getName());
        }
        model.setCurrentPlayer(sortedPlayers.get(0));
        model.shuffleCards();
        model.shuffleCrises();
        model.shuffleCrossroad();
        final LinkedList<Survivor> survivors = new LinkedList<>();
        for (final Player player : sortedPlayers) {

            // shuffle character list
            model.shuffleSurvivors();
            // pop 4 survivors
            survivors.clear();
            for (int i = 0; i < 4; i++) {
                final Survivor survivor = model.popSurvivor();
                survivors.add(survivor);
                // add 4 players to player (temporarily)
                player.addSurvivor(survivor);
            }
            // send them
            connection.sendCharacters(
                    model.getCommId(player.getId()),
                    survivors.get(0).getId(),
                    survivors.get(1).getId(),
                    survivors.get(2).getId(),
                    survivors.get(3).getId());

            while (model.getGameState().gameRunning() && model.isCurrentPlayer(player.getId())) {
                try {
                    final Command command = connection.nextCommand();
                    command.execute(model, connection);
                } catch (final TimeoutException e) {
                    final LeaveCommand leaveCommand =
                            new LeaveCommand(model.getCommId(player.getId()));
                    leaveCommand.execute(model, connection);
                }
            }

            if (!model.getGameState().gameRunning()) {
                return;
            }
        }
        // shuffle character list again
        model.getColony().resetCards();
        model.shuffleSurvivors();
        model.setGameState(PlayerPhaseState.getInstance());
    }
}
