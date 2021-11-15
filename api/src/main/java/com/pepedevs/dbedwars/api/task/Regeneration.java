package com.pepedevs.dbedwars.api.task;

import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.RegenerationType;
import org.bukkit.World;

import java.util.concurrent.Future;

public abstract class Regeneration {

    private RegenerationType type;
    private Arena arena;

    protected Regeneration(RegenerationType type, Arena arena) {
        this.type = type;
        this.arena = arena;
    }

    public abstract Future<World> regenerate();

    public RegenerationType getRegenerationType() {
        return type;
    }

    public void setRegenerationType(RegenerationType type) {
        this.type = type;
    }

    public Arena getArena() {
        return arena;
    }
}
