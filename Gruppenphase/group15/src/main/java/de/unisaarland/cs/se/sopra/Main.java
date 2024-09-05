package de.unisaarland.cs.se.sopra;

import de.unisaarland.cs.se.sopra.maingame.Game;
import de.unisaarland.cs.se.sopra.server.Server;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;
import sopra.comm.TimeoutException;

public class Main {
  //private static Server server;

  public static void main(final String[] args) {
    LoggerFactory.getLogger(Main.class).trace("Main is run");
    final String configPath;

    final int port;
    final long seed;
    final int timeout;


    final Options options = new Options();
    final CommandLineParser parser = new DefaultParser();
    options.addRequiredOption("config", "config", true, "Path to config file");
    options.addRequiredOption("port", "port", true, "Port to communicate");
    options.addRequiredOption("seed", "seed", true, "seed for rng");
    options.addRequiredOption("timeout", "timeout", true, "timeout in seconds");

    try {
      final CommandLine cmd = parser.parse(options, args);
      configPath = cmd.getOptionValue("config");
      port = Integer.parseInt(cmd.getOptionValue("port"));
      seed = Long.parseLong(cmd.getOptionValue("seed"));
      timeout = Integer.parseInt(cmd.getOptionValue("timeout"));

      final StringBuilder s = new StringBuilder(1024);
      s.append("Read in Arguments. config: ").append(configPath)
          .append("  port: ").append(port)
          .append("  seed: ").append(seed)
          .append("  timeout: ").append(timeout).append('\n');
      LoggerFactory.getLogger(Main.class).info(s.toString());

      try (final var server = new Server(port, timeout)) {
        final Gamebuilder builder = new Gamebuilder();
        final Game game = builder.buildGame(Path.of(configPath), seed);
        game.addObserver(server);
        server.giveGame(game);
        server.runServer();
      }

    } catch (final ParseException | TimeoutException | IOException e) {
      return;
    }
  }
}

//      CommandLine cmd = parser.parse(options, args);
//      // Path. of ??
//      configPath = Path.of(cmd.getOptionValue("config"));
//      port = Integer.parseInt(cmd.getOptionValue("port"));
//      seed = Long.parseLong(cmd.getOptionValue("seed"));
//      timeout = Integer.parseInt(cmd.getOptionValue("timeout"));
//      try (var server = new Server(port, timeout)) {
//        // TODO Gamebuilding
//        Gamebuilder builder = new Gamebuilder();
//        Game game = builder.buildGame(configPath);
//        server.giveGame(game);
//        server.runServer();

