package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.commands.Command;
import de.unisaarland.cs.se.sopra.model.Model;
import org.slf4j.LoggerFactory;
import sopra.comm.TimeoutException;

public final class RegistrationState extends State {

    private static RegistrationState instance;

    private RegistrationState() {
        //Do nothing
    }

    public static RegistrationState getInstance() {
        if (instance == null) {
            instance = new RegistrationState();
        }
        return instance;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        try {
            final Command command = connection.nextCommand();
            command.execute(model, connection);
        } catch (final TimeoutException e) {
            connection.sendRegistrationAborted();
            model.setGameState(EndGameState.getInstance());
            LoggerFactory.getLogger(RegistrationState.class)
                    .info("Timed out waiting for players to register");
        }
    }
}
