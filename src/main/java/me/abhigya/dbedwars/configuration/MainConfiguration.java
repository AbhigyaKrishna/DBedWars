package me.abhigya.dbedwars.configuration;

import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableEntry;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.exceptions.IllegalConfigException;
import org.bukkit.configuration.ConfigurationSection;

public class MainConfiguration implements Loadable {

    private final DBedwars plugin;

    private final ArenaSection arenaSection;

    public MainConfiguration(DBedwars plugin) {
        this.plugin = plugin;
        this.arenaSection = new ArenaSection();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        this.arenaSection.load(section.getConfigurationSection("arena"));
        return this;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    public ArenaSection getArenaSection() {
        return this.arenaSection;
    }

    public static class ArenaSection implements Loadable {

        @LoadableEntry(key = "start-timer")
        private int startTimer;

        @LoadableEntry(key = "respawn-delay")
        private int respawnTime;

        @LoadableEntry(key = "island-radius")
        private int islandRadius;

        @LoadableEntry(key = "min-y-axis")
        private int minYAxis;

        @LoadableEntry(key = "player-hit-tag-length")
        private int playerHitTagLength;

        @LoadableEntry(key = "game-end-delay")
        private int gameEndDelay;

        @LoadableEntry(key = "points.bed-destroy")
        private int bedDestroyPoint;

        @LoadableEntry(key = "points.kill")
        private int killPoint;

        @LoadableEntry(key = "points.final-kill")
        private int finalKillPoint;

        @LoadableEntry(key = "points.death")
        private int deathPoint;

        @LoadableEntry(key = "disable-hunger")
        private boolean disableHunger;

        public ArenaSection() {
            this.startTimer = -1;
            this.respawnTime = -1;
            this.islandRadius = -1;
            this.minYAxis = Integer.MIN_VALUE;
            this.playerHitTagLength = -1;
            this.gameEndDelay = -1;
            this.bedDestroyPoint = Integer.MIN_VALUE;
            this.killPoint = Integer.MIN_VALUE;
            this.finalKillPoint = Integer.MIN_VALUE;
            this.deathPoint = Integer.MIN_VALUE;
            this.disableHunger = true;
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            if (this.startTimer <= -1)
                throw new IllegalConfigException("arena.start-timer", "lower than 0", "config.yml");
            else if (this.respawnTime <= -1)
                throw new IllegalConfigException("arena.respawn-delay", "lower than 0", "config.yml");
            else if (this.islandRadius <= -1)
                throw new IllegalConfigException("arena.island-radius", "lower than 0", "config.yml");
            else if (this.minYAxis == Integer.MIN_VALUE)
                throw new IllegalConfigException("arena.min-y-axis", "Integer#MIN_VALUE", "config.yml");
            else if (this.playerHitTagLength <= -1)
                throw new IllegalConfigException("arena.player-hit-tag-length", "lower than 0", "config.yml");
            else if (this.gameEndDelay <= -1)
                throw new IllegalConfigException("arena.game-end-delay", "lower than 0", "config.yml");
            else if (this.bedDestroyPoint == Integer.MIN_VALUE)
                throw new IllegalConfigException("arena.points.bed-destroy", "Integer#MIN_VALUE", "config.yml");
            else if (this.killPoint == Integer.MIN_VALUE)
                throw new IllegalConfigException("arena.points.kill", "Integer#MIN_VALUE", "config.yml");
            else if (this.finalKillPoint == Integer.MIN_VALUE)
                throw new IllegalConfigException("arena.points.final-kill", "Integer#MIN_VALUE", "config.yml");
            else if (this.deathPoint == Integer.MIN_VALUE)
                throw new IllegalConfigException("arena.points.death", "Integer#MIN_VALUE", "config.yml");
            return true;
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
        }

        public int getStartTimer() {
            return startTimer;
        }

        public int getRespawnTime() {
            return respawnTime;
        }

        public int getIslandRadius() {
            return islandRadius;
        }

        public int getMinYAxis() {
            return minYAxis;
        }

        public int getPlayerHitTagLength() {
            return playerHitTagLength;
        }

        public int getGameEndDelay() {
            return gameEndDelay;
        }

        public int getBedDestroyPoint() {
            return bedDestroyPoint;
        }

        public int getKillPoint() {
            return killPoint;
        }

        public int getFinalKillPoint() {
            return finalKillPoint;
        }

        public int getDeathPoint() {
            return deathPoint;
        }

        public boolean isDisableHunger() {
            return disableHunger;
        }
    }
}
