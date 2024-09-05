package de.unisaarland.cs.se.sopra.config;

public interface CardFactory<T> {

    T createCard(int id, String name, ParamMap params);
}
