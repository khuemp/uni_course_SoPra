package de.unisaarland.cs.se.sopra.crossroad;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.EndGameState;
import de.unisaarland.cs.se.sopra.commands.KillSurvivorCommand;
import de.unisaarland.cs.se.sopra.model.Colony;
import de.unisaarland.cs.se.sopra.model.Entrance;
import de.unisaarland.cs.se.sopra.model.Location;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.Survivor;
import java.util.Optional;
import sopra.comm.FoodChange;
import sopra.comm.MoralChange;

public class ConsequenceVisitor {

    private final Model model;
    private final ConnectionWrapper connection;

    public ConsequenceVisitor(final Model model, final ConnectionWrapper connection) {
        this.model = model;
        this.connection = connection;
    }

    public void visit(final FoodConsequence foodConsequence) {
        final int changeFood = foodConsequence.getChangeFood();
        if (model.getColony().getFood() + changeFood < 0) {
            model.getColony().increaseStarvationToken();
            connection.sendStarvationTokenAdded();
        } else {
            model.getColony().addFood(changeFood);
            connection.sendFoodChanged(changeFood, FoodChange.CROSSROAD);
        }
    }

    public void visit(final MoralConsequence moralConsequence) {
        final int changeMoral = moralConsequence.getChangeMoral();
        model.addMoral(changeMoral);
        connection.sendMoralChanged(changeMoral, MoralChange.CROSSROAD);
        checkMoral();
    }

    public void visit(final SurvivorConsequence survivorConsequence) {
        final Player player = model.getCurrentPlayer();
        final int numSpawn = survivorConsequence.getSpawnSurvivors();
        for (int i = 0; i < numSpawn; i++) {
            if (!model.hasNewSurvivorsLeft()) {
                break;
            }
            final Survivor newSurvivor = model.drawSurvivor();
            player.addSurvivor(newSurvivor);
            newSurvivor.setLocation(model.getColony());
            model.getColony().addSurvivor(newSurvivor);
            connection.sendCharacterSpawned(
                    player.getId(), newSurvivor.getId());
        }
        if (survivorConsequence.hasChildren()) {
            for (int i = 0; i < numSpawn; i++) {
                model.getColony().addChild();
                connection.sendChildSpawned();
            }
        }
    }

    public void visit(final ZombieConsequence zombieConsequence) {
        final int numZombies = zombieConsequence.getSpawnZombies();
        final Optional<Location> optLoc = model.getLocation(zombieConsequence.getLocId());
        if (optLoc.isPresent()) {
            final Location location = optLoc.get();
            spawnZombies(location, numZombies);
        }
    }

    public void visit(final ZombieNoLocConsequence zombieNoLocConsequence) {
        final Colony colony = model.getColony();
        final int numZombies = zombieNoLocConsequence.getSpawnZombies();
        spawnZombies(colony, numZombies);
        for (final Location l : model.getLocations()) {
            spawnZombies(l, numZombies);
        }
    }


    private void checkMoral() {
        if (model.getMoral() <= 0) {
            connection.sendGameEnd(false);
            model.setGameState(EndGameState.getInstance());
        }
    }

    private void spawnZombies(final Location location, final int numZombies) {
        int zombiesToSpawn = numZombies;
        while (zombiesToSpawn != 0) {
            for (final Entrance e : location.getEntrances()) {
                spawnAtEntrance(location, e);
                checkMoral();
                if (--zombiesToSpawn == 0) {
                    break;
                }
            }
        }
    }

    private void spawnAtEntrance(final Location location, final Entrance e) {
        if (e.getCapacityLeft() > 0) {
            e.addZombie();
            connection.sendZombieSpawned(location.getId(), e.getId());
        } else if (e.getBarricadeCount() > 0) {
            e.removeBarricade();
            connection.sendBarricadeDestroyed(location.getId(), e.getId());
        } else {
            killChildOrSurvivor(location);
        }
    }

    private void killChildOrSurvivor(final Location location) {
        if (location.getNumChildren() > 0) {
            location.killChild();
            connection.sendChildKilled();
            connection.sendMoralChanged(-1, MoralChange.CHARACTER_DIED);
            model.decreaseMoral();
            checkMoral();
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
}
