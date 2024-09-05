package de.unisaarland.cs.se.sopra.cards;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.Player;

public abstract class CardVisitor {

    private final Model model;
    private final ConnectionWrapper connection;
    private final Player player;
    private final int commId;

    public CardVisitor(final Model model, final ConnectionWrapper connection, final Player player,
                       final int commId) {
        this.model = model;
        this.connection = connection;
        this.player = player;
        this.commId = commId;
    }

    public int getCommId() {
        return commId;
    }

    public Player getPlayer() {
        return player;
    }

    public ConnectionWrapper getConnection() {
        return connection;
    }

    public Model getModel() {
        return model;
    }

    public void visit(final Blueprint card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Colt card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Food card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Fuel card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Hammer card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Lock card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Medicine card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Scissors card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Snowboots card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Stuff card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

    public void visit(final Swab card) {
        getConnection().sendCommandFailed(getModel().getCommId(getPlayer().getId()),
                "Cannot use card.");
    }

}
