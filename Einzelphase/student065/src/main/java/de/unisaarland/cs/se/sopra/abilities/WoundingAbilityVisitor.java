package de.unisaarland.cs.se.sopra.abilities;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.EndGameState;
import de.unisaarland.cs.se.sopra.commands.KillSurvivorCommand;
import de.unisaarland.cs.se.sopra.model.Location;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;
import de.unisaarland.cs.se.sopra.model.StatusEffect;
import de.unisaarland.cs.se.sopra.model.Survivor;
import sopra.comm.MoralChange;

public abstract class WoundingAbilityVisitor extends AbilityVisitor {

    public static final int MAX_TIMES_WOUNDED = 3;

    private final Player player;
    private final Survivor survivor;

    public WoundingAbilityVisitor(final Model model, final ConnectionWrapper connection,
                                  final Player player,
                                  final Survivor survivor) {
        super(model, connection);
        this.player = player;
        this.survivor = survivor;
    }

    /**
     * @param wound The wound to deal to the survivor.
     */
    protected void dealWound(final StatusEffect wound) {
        switch (wound) {
            case UNHARMED:
                return;
            case BITE:
                killSurvivor(true);
                spreadInfection();
                return;
            case FROSTBITE:
                getConnection().sendFrostbitten(survivor.getId());
                survivor.addFrostbite();
                if (survivor.getTotalWounds() >= MAX_TIMES_WOUNDED) {
                    killSurvivor(false);
                }
                return;
            case WOUND:
                getConnection().sendWounded(survivor.getId());
                survivor.addWound();
                if (survivor.getTotalWounds() >= MAX_TIMES_WOUNDED) {
                    killSurvivor(false);
                }
                return;
            default:
                throw new IllegalStateException("This should never happen!");
        }
    }

    /**
     * Starts an infection chain at the survivor's location.
     */
    private void spreadInfection() {
        final Location location = survivor.getLocation();
        if (location.hasChildren()) {
            final StatusEffect wound = getModel().rollInfectionDie();
            if (wound != StatusEffect.UNHARMED) {
                location.killChild();
                getConnection().sendBitten();
                getConnection().sendMoralChanged(-1, MoralChange.CHARACTER_DIED);
                getModel().decreaseMoral();
                if (getModel().getMoral() <= 0) {
                    getModel().setGameState(EndGameState.getInstance());
                    getConnection().sendGameEnd(false);
                    return;
                }
                spreadInfection();
            }
        } else if (location.hasSurvivors()) {
            spreadInfectionNoChildren(location);
        }
    }

    /**
     * Continues the infection chain if no children are at location.
     *
     * @param location Where the infection spreads.
     */
    private void spreadInfectionNoChildren(final Location location) {
        final StatusEffect wound = getModel().rollInfectionDie();
        if (wound != StatusEffect.UNHARMED) {
            Survivor survivor = null;
            for (final Survivor s : location.getSurvivors()) {
                survivor =
                        (survivor == null || s.getSocialStatus() < survivor.getSocialStatus())
                                ? s : survivor;
            }
            if (survivor == null) {
                return;
            }
            final Player player = getModel().getPlayerForSurvivor(survivor.getId()).orElseThrow(()
                    -> new IllegalStateException("This should never happen!"));
            final KillSurvivorCommand command = new KillSurvivorCommand(
                    getModel().getCommId(player.getId()), player, survivor, true);
            command.execute(getModel(), getConnection());
            spreadInfection();
        }
    }

    /**
     * @param byBite Whether the survivor died because of a zombie bite.
     */
    private void killSurvivor(final boolean byBite) {
        final KillSurvivorCommand command =
                new KillSurvivorCommand(getModel().getCommId(player.getId()),
                        player, survivor, byBite);
        command.execute(getModel(), getConnection());
    }

    public Player getPlayer() {
        return player;
    }

    public Survivor getSurvivor() {
        return survivor;
    }
}
