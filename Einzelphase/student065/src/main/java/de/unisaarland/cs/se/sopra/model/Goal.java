package de.unisaarland.cs.se.sopra.model;

import de.unisaarland.cs.se.sopra.ConnectionWrapper;

public interface Goal {

    void verify(Model model, ConnectionWrapper connection);
}
