package org.zibble.dbedwars.api.game;

import java.util.Set;
import java.util.function.Consumer;

public interface Upgrade {

    String getId();

    String getTier();

    Team getTeam();

    Set<Consumer<Team>> getPermanentActions();

    String toString();
}
