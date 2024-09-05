package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.abilities.Ability;
import de.unisaarland.cs.se.sopra.abilities.AbilityFactoryImpl;
import de.unisaarland.cs.se.sopra.cards.Card;
import de.unisaarland.cs.se.sopra.cards.CardFactoryImpl;
import de.unisaarland.cs.se.sopra.commands.Command;
import de.unisaarland.cs.se.sopra.commands.ServerCommandFactory;
import de.unisaarland.cs.se.sopra.config.AbilityFactory;
import de.unisaarland.cs.se.sopra.config.CardFactory;
import de.unisaarland.cs.se.sopra.config.JSONParser;
import de.unisaarland.cs.se.sopra.config.ModelBuilder;
import de.unisaarland.cs.se.sopra.config.Validator;
import de.unisaarland.cs.se.sopra.crossroad.Consequence;
import de.unisaarland.cs.se.sopra.crossroad.ConsequenceFactory;
import de.unisaarland.cs.se.sopra.crossroad.ConsequenceFactoryImpl;
import de.unisaarland.cs.se.sopra.crossroad.Crossroad;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadFactory;
import de.unisaarland.cs.se.sopra.crossroad.CrossroadFactoryImpl;
import de.unisaarland.cs.se.sopra.model.Model;
import de.unisaarland.cs.se.sopra.model.ModelBuilderImpl;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONException;
import org.slf4j.LoggerFactory;
import sopra.comm.ServerConnection;

public class Main {

    public static void main(final String[] args) {
        final Options options = new Options();
        final Option configOption =
                Option.builder()
                        .required()
                        .longOpt("config")
                        .desc(
                                "The path to the file from which the game configuration should"
                                        + " be loaded in JSON format.")
                        .hasArg()
                        .argName("<path>")
                        .build();
        final Option portOption =
                Option.builder()
                        .required()
                        .longOpt("port")
                        .desc("The port the client can use to communicate to the server.")
                        .hasArg()
                        .argName("<int>")
                        .build();
        final Option seedOption =
                Option.builder()
                        .required()
                        .longOpt("seed")
                        .desc(
                                "The seed which is used to for the random number generator.")
                        .hasArg()
                        .argName("<long>")
                        .build();
        final Option timeoutOption =
                Option.builder()
                        .required()
                        .longOpt("timeout")
                        .desc(
                                "The timeout in seconds (the maximal time the server will wait"
                                        + " for commands)")
                        .hasArg()
                        .argName("<int>")
                        .build();
        options.addOption(configOption);
        options.addOption(portOption);
        options.addOption(seedOption);
        options.addOption(timeoutOption);
        final CommandLineParser parser = new DefaultParser();
        try {
            // Parse command line arguments
            final CommandLine cmd = parser.parse(options, args);
            final Path config = Paths.get(cmd.getOptionValue("config"));
            final int port = Integer.parseInt(cmd.getOptionValue("port"));
            final long seed = Long.parseLong(cmd.getOptionValue("seed"));
            final int timeout = Integer.parseInt(cmd.getOptionValue("timeout")) * 1000;

            // Create model and the server connection

            final ServerConnection<Command> serverConnection =
                    new ServerConnection<>(port, timeout, new ServerCommandFactory());
            try (final ConnectionWrapper connection = new ConnectionWrapper(serverConnection)) {
                final CardFactory<Card> cardFactory = new CardFactoryImpl();
                final AbilityFactory<Ability> abilityFactory = new AbilityFactoryImpl();
                final CrossroadFactory<Crossroad> crossroadFactory = new CrossroadFactoryImpl();
                final ConsequenceFactory<Consequence> consequenceFactory =
                        new ConsequenceFactoryImpl();
                final ModelBuilder<Model> modelBuilder =
                        new Validator<>(
                                new ModelBuilderImpl(cardFactory, abilityFactory, crossroadFactory,
                                        consequenceFactory));
                modelBuilder.setSeed(seed);

                try {
                    final Model model = JSONParser.parse(config, modelBuilder);
                    final Server server = new Server(model, connection);
                    server.run();
                } catch (final JSONException | IOException e) {
                    LoggerFactory.getLogger(Main.class).info("Exception while reading file");
                }
            }

        } catch (final ParseException e) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("sorpa21", options);
        }
    }

}
