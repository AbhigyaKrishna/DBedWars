package com.pepedevs.dbedwars.api.game.trap;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.Keyed;
import com.pepedevs.dbedwars.api.util.TrapEnum;

import java.util.Collection;

public interface Trap extends Keyed<String> {

    void trigger(ArenaPlayer target);

    TrapEnum.TriggerType getTriggerType();

    Team getTrapOwner();

    ArenaPlayer getTrapBuyer();

    interface TrapAction extends Action<Collection<ArenaPlayer>> {

        Trap getTrap();

        Collection<ArenaPlayer> getTarget(ArenaPlayer target);

        @Override
        void execute(Collection<ArenaPlayer> victim);

    }

}