package de.unisaarland.cs.se.sopra.commands;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;
import de.unisaarland.cs.se.sopra.PreGameState;
import de.unisaarland.cs.se.sopra.model.Model;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import org.slf4j.LoggerFactory;

public class RegisterCommand extends AdministrativeCommand {

    private final String playerName;

    public RegisterCommand(final int commId, final String playerName) {
        super(commId);
        this.playerName = playerName;
    }

    @Override
    public void run(final Model model, final ConnectionWrapper connection) {
        if (model.hasPlayer(super.getCommId())) {
            connection.sendCommandFailed(super.getCommId(), "Already registered");
        } else {
            model.addPlayer(super.getCommId(), playerName);
            final String config;
            try {
                config = Objects.requireNonNull(
                        Files.readString(model.getConfigPath(), StandardCharsets.UTF_8));
                connection.sendConfig(super.getCommId(), config);
                connection.addCommId(super.getCommId());
            } catch (final IOException e) {
                LoggerFactory.getLogger(RegisterCommand.class).info("failed to read config file");
            }
            if (model.getMaxPlayers() == model.getPlayers().size()) {
                model.setGameState(PreGameState.getInstance());
            }
        }

    }

}
