package com.pepedevs.dbedwars.api.game;

import com.pepedevs.dbedwars.api.util.TrapEnum;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface Trap {

    void trigger(ArenaPlayer target, Team team);

    String getId();

    TrapEnum.TriggerType getTriggerType();

    Map<TrapEnum.TargetType, Set<Consumer<ArenaPlayer>>> getActions();
}
