package me.abhigya.dbedwars.configuration;

import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableEntry;
import me.abhigya.dbedwars.DBedwars;
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

        @LoadableEntry(key = "disable-vanilla-achievement")
        private boolean disableAchievement;

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

        public boolean isDisableAchievement() {
            return disableAchievement;
        }
    }
}
