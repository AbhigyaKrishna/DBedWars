package me.abhigya.dbedwars.api.task;

import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.RegenerationType;
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
