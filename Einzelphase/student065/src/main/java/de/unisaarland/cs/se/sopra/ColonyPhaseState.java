package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.commands.KillSurvivorCommand;
import de.unisaarland.cs.se.sopra.model.Colony;
import de.unisaarland.cs.se.sopra.model.Crisis;
import de.unisaarland.cs.se.sopra.model.Entrance;
import de.unisaarland.cs.se.sopra.model.Goal;
import de.unisaarland.cs.se.sopra.model.Location;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;
import sopra.comm.FoodChange;
import sopra.comm.MoralChange;

public final class ColonyPhaseState extends State {

    private static final int FOOD_ZOMBIE_DIVISOR = 2;
    private static final int CRISIS_THRESHOLD = 2;
    private static final int MORAL_PLUS = 1;

    private static ColonyPhaseState instance;

    private ColonyPhaseState() {
        // Do nothing
    }

    public static ColonyPhaseState getInstance() {
        if (instance == null) {
            instance = new ColonyPhaseState();
        }
        return instance;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        connection.sendColonyPhaseStarted();
        distributeFood(model, connection);
        checkMoral(model, connection);
        if (!model.getGameState().gameRunning()) {
            return;
        }
        checkTrash(model, connection);
        checkMoral(model, connection);
        if (!model.getGameState().gameRunning()) {
            return;
        }
        crisis(model, connection);
        checkMoral(model, connection);
        if (!model.getGameState().gameRunning()) {
            return;
        }
        spawnZombies(model, connection);

        if (!model.getGameState().gameRunning()) {
            return;
        }
        checkGoal(model, connection);
        if (model.getGameState().gameRunning()) {
            model.setGameState(PlayerPhaseState.getInstance());
            model.decreaseRounds();
        }
    }

    /**
     * Give food to the survivors and children at the colony.
     *
     * @param model      To take food (and moral) away.
     * @param connection To inform all players.
     */
    private void distributeFood(final Model model, final ConnectionWrapper connection) {
        final Colony colony = model.getColony();
        final int numFood =
                (int)
                        Math.round(
                                (double) (colony.getNumChildren() + colony.getNumSurvivors())
                                        / FOOD_ZOMBIE_DIVISOR);
        if (numFood != 0) {
            if (colony.getFood() >= numFood) {
                colony.removeFood(numFood);
                connection.sendFoodChanged(-numFood, FoodChange.FOOD_CONSUMED);
            } else {
                colony.increaseStarvationToken();
                connection.sendStarvationTokenAdded();
            }
        }
        final int moralChange = colony.getNumStarvationTokens();
        if (moralChange > 0) {
            model.decreaseMoral(moralChange);
            connection.sendMoralChanged(-moralChange, MoralChange.STARVATION_TOKEN);
        }
    }

    /**
     * @param model      To check the waste.
     * @param connection To inform all players.
     */
    private void checkTrash(final Model model, final ConnectionWrapper connection) {
        // integer division
        final int moralChange = model.getTrashPile() / 10;
        if (moralChange > 0) {
            model.decreaseMoral(moralChange);
            connection.sendMoralChanged(-moralChange, MoralChange.WASTE);
        }
    }

    /**
     * Check if the crisis was passed.
     *
     * @param model      To check the crisis status.
     * @param connection To inform all Players.
     */
    private void crisis(final Model model, final ConnectionWrapper connection) {
        final Crisis crisis = model.getCrisis();
        if (crisis.getCardsContributed() >= crisis.getCardsRequired()) {
            if (crisis.getCardsContributed() >= crisis.getCardsRequired() + CRISIS_THRESHOLD) {
                model.addMoral(MORAL_PLUS);
                connection.sendMoralChanged(MORAL_PLUS, MoralChange.CRISIS);
            }
        } else {
            model.decreaseMoral(crisis.getMoralChange());
            connection.sendMoralChanged(-crisis.getMoralChange(), MoralChange.CRISIS);
        }
    }

    /**
     * Spawning zombies at the colony and all other locations.
     *
     * @param model      To get the locations.
     * @param connection To inform all players.
     */
    private void spawnZombies(final Model model, final ConnectionWrapper connection) {
        final Colony colony = model.getColony();
        final int numZombies =
                (int)
                        Math.round(
                                (double) (colony.getNumChildren() + colony.getNumSurvivors())
                                        / FOOD_ZOMBIE_DIVISOR);
        spawnZombies(colony, numZombies, model, connection);

        model.getLocations().stream()
                .forEach(l -> spawnZombies(l, l.getNumSurvivors(), model, connection));
    }

    /**
     * Spawn zombies at specific location.
     *
     * @param location   Where to spawn zombies.
     * @param numZombies How many zombies to spawn.
     * @param model      To potentially decrease the moral.
     * @param connection To inform all players.
     */
    private void spawnZombies(
            final Location location,
            final int numZombies,
            final Model model,
            final ConnectionWrapper connection) {
        if (!model.getGameState().gameRunning()) {
            return;
        }
        int zombiesToSpawn = numZombies;
        while (zombiesToSpawn != 0) {
            for (final Entrance e : location.getEntrances()) {
                spawnAtEntrance(location, model, connection, e);
                checkMoral(model, connection);
                if (--zombiesToSpawn == 0) {
                    break;
                }
            }
        }
    }

    /**
     * Spawn zombies at an entrance.
     *
     * @param location   To check for children.
     * @param model      To potentially decrease the moral.
     * @param connection To inform all players.
     * @param e          Which entrance to spawn zombies at.
     */
    private void spawnAtEntrance(
            final Location location,
            final Model model,
            final ConnectionWrapper connection,
            final Entrance e) {
        if (e.getCapacityLeft() > 0) {
            e.addZombie();
            connection.sendZombieSpawned(location.getId(), e.getId());
        } else if (e.getBarricadeCount() > 0) {
            e.removeBarricade();
            connection.sendBarricadeDestroyed(location.getId(), e.getId());
        } else {
            killChildOrSurvivor(location, model, connection);
        }
    }

    private void killChildOrSurvivor(final Location location, final Model model,
                                     final ConnectionWrapper connection) {
        if (location.getNumChildren() > 0) {
            location.killChild();
            connection.sendChildKilled();
            connection.sendMoralChanged(-1, MoralChange.CHARACTER_DIED);
            model.decreaseMoral();
            if (model.getMoral() <= 0) {
                model.setGameState(EndGameState.getInstance());
                connection.sendGameEnd(false);
            }
        } else {
            final Optional<Survivor> optionalSurvivor = location.getSurvivorSmallestStatus();
            if (optionalSurvivor.isPresent()) {
                final Survivor survivor = optionalSurvivor.get();
                final KillSurvivorCommand command =
                        new KillSurvivorCommand(
                                model.getCommId(model.getPlayer(survivor).getId()),
                                model.getPlayer(survivor),
                                survivor);
                command.execute(model, connection);
            }
        }
    }

    /**
     * This method checks if the moral dropped to 0.
     *
     * @param model      To check the moral.
     * @param connection To inform all players.
     */
    private void checkMoral(final Model model, final ConnectionWrapper connection) {
        if (model.getMoral() <= 0) {
            connection.sendGameEnd(false);
            model.setGameState(EndGameState.getInstance());
        }
    }

    /**
     * This method checks whether the goal is fulfilled and whether
     * there are no rounds left to play.
     *
     * @param model      To check the goal.
     * @param connection To inform all players.
     */
    private void checkGoal(final Model model, final ConnectionWrapper connection) {
        final Goal goal = model.getGoal();
        goal.verify(model, connection);

        // independent of previous checks
        // if the game has not yet ended any other way
        if (model.getRounds() <= 1 && model.getGameState().gameRunning()) {
            connection.sendGameEnd(false);
            model.setGameState(EndGameState.getInstance());
        }
    }
}
