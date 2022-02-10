package com.pepedevs.dbedwars.configuration.configurable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pepedevs.dbedwars.configuration.util.Configurable;
import com.pepedevs.dbedwars.configuration.util.Loadable;
import com.pepedevs.dbedwars.configuration.util.annotations.LoadableEntry;
import com.pepedevs.dbedwars.configuration.util.annotations.SaveableEntry;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurableTeam implements Configurable {

    @SaveableEntry(key = "spawners")
    @LoadableEntry(key = "spawners")
    public List<String> spawners;

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
        return Color.from(this.color).get();
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
            if (entry != null) multimap.put(entry.getKey(), entry.getValue());
        }

        return multimap;
    }

    public void update() {
        if (this.team == null)
            throw new IllegalStateException("Team is null somehow in the configuration!");

        this.color = this.team.getColor().name();
        this.bedLocation =
                this.team.getBedLocation() != null ? this.team.getBedLocation().toString() : null;
        this.spawn = this.team.getSpawn() != null ? this.team.getSpawn().toString() : null;
        this.shopNpc = this.team.getShopNpc() != null ? this.team.getShopNpc().toString() : null;
        this.upgrades =
                this.team.getUpgradesNpc() != null ? this.team.getUpgradesNpc().toString() : null;
        this.spawners =
                this.team.getSpawners().entries().stream()
                        .map(e -> ConfigurationUtils.serializeSpawner(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
    }

    public Team toTeam() {
        if (this.team == null)
            return this.team =
                    new com.pepedevs.dbedwars.game.arena.Team(DBedwars.getInstance(), this);

        return this.team;
    }

    @Override
    public String toString() {
        return "ConfigurableTeam{" +
                "spawners=" + spawners +
                ", team=" + team +
                ", color='" + color + '\'' +
                ", bedLocation='" + bedLocation + '\'' +
                ", spawn='" + spawn + '\'' +
                ", shopNpc='" + shopNpc + '\'' +
                ", upgrades='" + upgrades + '\'' +
                '}';
    }
}
