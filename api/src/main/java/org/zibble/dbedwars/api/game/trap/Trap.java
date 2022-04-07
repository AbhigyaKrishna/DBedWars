package org.zibble.dbedwars.api.game.trap;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.util.key.Keyed;

import java.util.Collection;
import java.util.Set;

public interface Trap extends Keyed {

    Set<TrapAction> getActions();

    void addAction(TrapAction action);

    boolean trigger(ArenaPlayer target);

    Team getTrapOwner();

    ArenaPlayer getTrapBuyer();

    interface TrapAction {

        Trap getTrap();

        Target getTarget();

        void execute(Collection<? extends ArenaPlayer> targets);

    }

}
