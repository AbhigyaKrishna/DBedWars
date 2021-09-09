package me.abhigya.dbedwars.configuration.configurable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.Abhigya.core.util.configurable.Configurable;
import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableEntry;
import me.Abhigya.core.util.saveable.SaveableEntry;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.Color;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.api.util.LocationXYZYP;
import me.abhigya.dbedwars.utils.ConfigurationUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurableTeam implements Configurable {

    private Team team;

    @SaveableEntry(key = "color")
    @LoadableEntry(key = "color")
    private String color;

    @SaveableEntry(key = "bed")
    @LoadableEntry(key = "bed")
    private String bedLocation;

    @SaveableEntry(key = "spawn")
    @LoadableEntry(key = "spawn")
    private String spawn;

    @SaveableEntry(key = "shop")
    @LoadableEntry(key = "shop")
    private String shopNpc;

    @SaveableEntry(key = "upgrades")
    @LoadableEntry(key = "upgrades")
    private String upgrades;

    @SaveableEntry(key = "spawners")
    @LoadableEntry(key = "spawners")
    public List<String> spawners;

    public ConfigurableTeam() {
        this.spawners = new ArrayList<>();
    }

    public ConfigurableTeam(Team team) {
        this.team = team;
        this.update();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public int save(ConfigurationSection section) {
        return this.saveEntries(section);
    }

    public Color getColor() {
        return Color.valueOf(this.color);
    }

    public LocationXYZ getBedLocation() {
        return this.bedLocation == null ? null : LocationXYZ.valueOf(this.bedLocation);
    }

    public LocationXYZYP getSpawn() {
        return this.spawn == null ? null : LocationXYZYP.valueOf(this.spawn);
    }

    public LocationXYZYP getShopNpc() {
        return this.shopNpc == null ? null : LocationXYZYP.valueOf(this.shopNpc);
    }

    public LocationXYZYP getUpgrades() {
        return this.upgrades == null ? null : LocationXYZYP.valueOf(this.upgrades);
    }

    public Multimap<DropType, LocationXYZ> getSpawners() {
        Multimap<DropType, LocationXYZ> multimap = ArrayListMultimap.create();
        for (String s : this.spawners) {
            Map.Entry<DropType, LocationXYZ> entry = ConfigurationUtils.parseSpawner(s);
            if (entry != null)
                multimap.put(entry.getKey(), entry.getValue());
        }

        return multimap;
    }

    public void update() {
        if (this.team == null)
            throw new IllegalStateException("Team is null somehow in the configuration!");

        this.color = this.team.getColor().name();
        this.bedLocation = this.team.getBedLocation() != null ? this.team.getBedLocation().toString() : null;
        this.spawn = this.team.getSpawn() != null ? this.team.getSpawn().toString() : null;
        this.shopNpc = this.team.getShopNpc() != null ? this.team.getShopNpc().toString() : null;
        this.upgrades = this.team.getUpgradesNpc() != null ? this.team.getUpgradesNpc().toString() : null;
        this.spawners = this.team.getSpawners().entries().stream()
                .map(e -> ConfigurationUtils.serializeSpawner(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public Team toTeam() {
        if (this.team == null)
            return this.team = new me.abhigya.dbedwars.game.arena.Team(this);

        return this.team;
    }
}
