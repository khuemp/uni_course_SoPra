package de.unisaarland.cs.se.sopra;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import org.everit.json.schema.Schema;
import org.everit.json.schema.SchemaException;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @version 1.0.0
 */
public class Utils {

  static boolean deleteDir(final Class<?> subclass, final File dir) throws IOException {
    return !dir.exists() || Files.walk(dir.toPath(), FileVisitOption.FOLLOW_LINKS)
        .sorted(Comparator.reverseOrder()).map(Path::toFile)
        .peek(i -> LoggerFactory.getLogger(subclass).trace("delete {}", i.getPath()))
        .allMatch(File::delete);
  }

  static Schema getSchema(final Class<?> subclass, final String name) {
    try {
      return SchemaLoader.builder().schemaClient(SchemaClient.classPathAwareClient()).schemaJson(
              new JSONObject(Objects.requireNonNull(loadResource(Utils.class, "schema/" + name))))
          .resolutionScope("classpath://schema/").build().load().build();
    } catch (final SchemaException exception) {
      LoggerFactory.getLogger(subclass).error("Could not load schema: {}", exception.getMessage());
      return SchemaLoader
          .load(new JSONObject("{\"$schema\": \"http://json-schema.org/draft-07/schema#\"}"));
    }
  }

  public static String loadResource(final Class<?> subclass, final String name) {
    LoggerFactory.getLogger(subclass)
        .trace("loading {}", subclass.getClassLoader().getResource(name));
    try (final InputStreamReader input = new InputStreamReader(
        Objects.requireNonNull(subclass.getClassLoader().getResourceAsStream(name)),
        StandardCharsets.UTF_8)) {
      try (final BufferedReader reader = new BufferedReader(input)) {
        return reader.lines().collect(Collectors.joining("\n"));
      }
    } catch (final IOException e) {
      LoggerFactory.getLogger(subclass).error("{}", e.getMessage(), e);
      return null;
    }
  }
}
