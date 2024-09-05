package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.EndGameState;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;
import sopra.comm.MoralChange;

public class KillSurvivorCommand extends InternalCommand {

    private final Player player;
    private final Survivor survivor;
    private final boolean byBite;

    public KillSurvivorCommand(final int commId, final Player player, final Survivor survivor) {
        this(commId, player, survivor, false);
    }

    public KillSurvivorCommand(final int commId, final Player player,
                               final Survivor survivor,
                               final boolean byBite) {
        super(commId);
        this.player = player;
        this.survivor = survivor;
        this.byBite = byBite;
    }

    /**
     * Removes all traces of a given survivor from the game and
     * send according events.
     *
     * @param model      The game model to remove the survivor from.
     * @param player     The player which controls the survivor.
     * @param survivor   The survivor to remove.
     * @param connection To inform all players.
     * @return False if the game ended because of the moral decrease.
     */
    protected static boolean removeSurvivor(final Model model, final Player player,
                                            final Survivor survivor,
                                            final ConnectionWrapper connection) {
        survivor.getLocation().removeSurvivor(survivor);
        player.removeSurvivor(survivor);
        model.decreaseMoral();
        connection.sendMoralChanged(-1, MoralChange.CHARACTER_DIED);
        if (model.getMoral() <= 0) {
            model.setGameState(EndGameState.getInstance());
            connection.sendGameEnd(false);
            return false;
        }
        return true;
    }

    @Override
    protected void run(final Model model, final ConnectionWrapper connection) {
        if (byBite) {
            connection.sendBitten(survivor.getId());
        } else {
            connection.sendSurvivorKilled(survivor.getId());
        }
        if (!removeSurvivor(model, player, survivor, connection)) {
            return;
        }
        if (!player.hasSurvivorsLeft()) {
            player.resetCards();
            if (model.hasNewSurvivorsLeft()) {
                final Survivor newSurvivor = model.drawSurvivor();
                player.addSurvivor(newSurvivor);
                newSurvivor.setLocation(model.getColony());
                model.getColony().addSurvivor(newSurvivor);
                connection.sendCharacterSpawned(player.getId(), newSurvivor.getId());
                if (model.isCurrentPlayer(player.getId())) {
                    final EndTurnCommand command = new EndTurnCommand(super.getCommId());
                    command.execute(model, connection);
                }

            } else {
                Crossroad bufferCrossroad = null;
                boolean bufferCrossroadActivate = false;
                if (model.isCurrentPlayer(player.getId())) {
                    if (player.getCrossroadActivate()) {
                        bufferCrossroad = player.getCrossroad();
                        bufferCrossroadActivate = true;
                    }
                    final EndTurnCommand command = new EndTurnCommand(super.getCommId());
                    command.execute(model, connection);
                }
                model.removePlayer(player);
                player.setCrossroad(bufferCrossroad);
                player.setCrossroadActivate(bufferCrossroadActivate);
                connection.sendLeft(super.getCommId(), player.getId());
            }
        }
    }
}
