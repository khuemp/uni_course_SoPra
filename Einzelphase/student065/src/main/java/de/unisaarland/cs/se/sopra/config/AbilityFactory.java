package de.unisaarland.cs.se.sopra.config;

public interface AbilityFactory<T> {

    T createAbility(String name, ParamMap params);
}
