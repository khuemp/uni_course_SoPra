package de.unisaarland.cs.se.sopra.crossroad;

import java.util.List;
import org.json.JSONObject;

public interface ConsequenceFactory<T> {

    List<T> createConsequence(final String type, final JSONObject consequence);
}
