package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaveCommand extends InGameCommand {

    public LeaveCommand(final int commId) {
        super(commId);
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        final Player player = model.getPlayer(model.getPlayerID(super.getCommId()));
        connection.sendLeft(super.getCommId(), player.getId());
        model.nextPlayer();
        model.removePlayer(player);
        final List<Survivor> sortedSurvivors = new ArrayList<>(player.getSurvivors());
        sortedSurvivors.sort(Comparator.comparingInt(Survivor::getSocialStatus).reversed());
        for (final Survivor s : sortedSurvivors) {
            connection.sendSurvivorKilled(s.getId());
            if (!KillSurvivorCommand.removeSurvivor(model, player, s, connection)) {
                return;
            }
        }
    }

}
