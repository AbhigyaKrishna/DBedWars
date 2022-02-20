package org.zibble.dbedwars.api.game.trap;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.util.Keyed;

import java.util.Collection;

public interface Trap extends Keyed<String> {

    void trigger(ArenaPlayer target);

    Team getTrapOwner();

    ArenaPlayer getTrapBuyer();

    interface TrapAction {

        Trap getTrap();

        Collection<ArenaPlayer> getActionTarget(ArenaPlayer target);

        void execute(Collection<ArenaPlayer> targets);

    }

}
