package de.unisaarland.cs.se.sopra.config;

public interface ParamMap {

    int getInt(String key);

    String getString(String key);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defaultValue);
}
