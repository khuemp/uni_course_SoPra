package de.unisaarland.cs.se.sopra.model;

import de.unisaarland.cs.se.sopra.abilities.Ability;

public class Survivor {

    private final int id;
    private final int attack;
    private final int search;
    private final int socialStatus;
    private final String name;
    private int timesFrostbitten;
    private int timesWounded;
    private Location location;
    private Ability ability;
    private boolean moved;

    public Survivor(
            final int id,
            final String name,
            final int attack,
            final int search,
            final int socialStatus,
            final Location location,
            final Ability ability) {
        this.id = id;
        this.attack = attack;
        this.search = search;
        this.socialStatus = socialStatus;
        this.location = location;
        this.ability = ability;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAttack() {
        return attack;
    }

    public int getSearch() {
        return search;
    }

    public int getSocialStatus() {
        return socialStatus;
    }

    public int getTimesFrostbitten() {
        return timesFrostbitten;
    }

    public int getTotalWounds() {
        return timesWounded + timesFrostbitten;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public void addFrostbite() {
        timesFrostbitten++;
    }

    public void addWound() {
        timesWounded++;
    }

    public Ability getAbility() {
        return this.ability;
    }

    public void setAbility(final Ability ability) {
        this.ability = ability;
    }

    public boolean isWounded() {
        return timesWounded + timesFrostbitten > 0;
    }

    public void healFrostbite() {
        if (timesFrostbitten < 1) {
            throw new IllegalStateException("Cannot heal non-existing forstbite");
        }
        timesFrostbitten--;
    }

    public void heal() {
        if (timesWounded < 1) {
            throw new IllegalStateException("Cannot heal non-existing wound");
        }
        timesWounded--;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void moved() {
        this.moved = true;
    }

    /**
     * Reset everything.
     */
    public void tick() {
        this.moved = false;
        getAbility().tick();
    }
}
