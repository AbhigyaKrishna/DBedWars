package com.pepedevs.dbedwars.api.game.trap;

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

    interface TrapAction {

        Trap getTrap();

        Collection<ArenaPlayer> getActionTarget(ArenaPlayer target);

        void execute(Collection<ArenaPlayer> targets);

    }

}
