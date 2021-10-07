package me.abhigya.dbedwars.api.game;

import me.abhigya.dbedwars.api.util.TrapEnum;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface Trap {

    void trigger( ArenaPlayer target, Team team );

    String getId( );

    TrapEnum.TriggerType getTrigger( );

    Map< TrapEnum.TargetType, Set< Consumer< ArenaPlayer > > > getActions( );

}
