package org.zibble.dbedwars.api.game.trap;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.util.key.Keyed;

import java.util.Collection;

public interface Target extends Keyed {

    Collection<? extends ArenaPlayer> getTargets(Trap trap, ArenaPlayer trigger);

}
