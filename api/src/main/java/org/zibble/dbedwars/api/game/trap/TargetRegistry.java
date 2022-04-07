package org.zibble.dbedwars.api.game.trap;

import java.util.Collection;

public interface TargetRegistry {

    void register(Target target);

    void remove(Target target);

    void remove(String key);

    Target get(String key);

    Collection<Target> getAll();

}
