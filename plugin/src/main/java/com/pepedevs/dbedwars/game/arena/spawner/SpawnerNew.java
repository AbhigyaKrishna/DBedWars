package com.pepedevs.dbedwars.game.arena.spawner;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.Keyed;
import com.pepedevs.dbedwars.api.util.properies.NamedProperties;
import com.pepedevs.radium.utils.Initializable;
import com.pepedevs.radium.utils.math.collision.BoundingBox;
import org.bukkit.Location;

import java.util.Optional;

public class SpawnerNew implements Initializable, Keyed<DropTypeNew> {

    private final DBedwars plugin;
    private final Key<DropTypeNew> key;
    private final Arena arena;
    private final Optional<Team> team;
    private Location location;
    private BoundingBox box;

    private int level;
    private boolean teamSpawner;
    private DropTypeNew.Tier currentTier;

    private boolean initialized;

    public SpawnerNew(DBedwars plugin, DropTypeNew dropType, Arena arena, Optional<Team> team) {
        this.plugin = plugin;
        this.key = Key.of(dropType);
        this.arena = arena;
        this.team = team;
    }

    public void init(Location location, NamedProperties properties) {
        this.location = location;
        this.level = properties.getValue("level", 1);
        this.box = new BoundingBox(
                this.location.getX() - this.getDropType().getRadius(),
                this.location.getY() - 2,
                this.location.getZ() - this.getDropType().getRadius(),
                this.location.getX() + this.getDropType().getRadius(),
                this.location.getY() + 2,
                this.location.getZ() + this.getDropType().getRadius());

        this.currentTier = this.getDropType().

        this.initialized = true;
    }

    public DropTypeNew getDropType() {
        return key.get();
    }

    @Override
    public Key<DropTypeNew> getKey() {
        return key;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

}
