package me.abhigya.dbedwars.utils;

import me.Abhigya.core.util.tasks.Workload;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.cache.DataCache;
import me.abhigya.dbedwars.cache.PlayerStats;

import java.util.List;

public class DatabaseUtils {

    public static void saveGameData(Arena arena) {
        for (ArenaPlayer player : arena.getPlayers()) {
            DBedwars.getInstance()
                    .getThreadHandler()
                    .addAsyncWork(
                            new Workload() {
                                @Override
                                public void compute() {
                                    List<DataCache> saved =
                                            DBedwars.getInstance()
                                                    .getDatabaseBridge()
                                                    .getAsDataCache(
                                                            "UUID",
                                                            player.getUUIDPlayer()
                                                                    .getUniqueId()
                                                                    .toString(),
                                                            "PLAYER_STATS");
                                    PlayerStats stat;
                                    if (!saved.isEmpty()) {
                                        stat = (PlayerStats) saved.get(0);
                                        stat.setCOINS(stat.getCOINS() + 10);
                                        stat.setLEVEL(stat.getLEVEL() + 1);
                                        stat.setPOINTS(
                                                stat.getPOINTS() + Utils.calculatePoint(player));
                                    } else {
                                        stat =
                                                new PlayerStats(
                                                        player.getUUIDPlayer()
                                                                .getUniqueId()
                                                                .toString(),
                                                        player.getName());
                                        stat.setCOINS(10);
                                        stat.setLEVEL(1);
                                        stat.setPOINTS(Utils.calculatePoint(player));
                                    }
                                    DBedwars.getInstance()
                                            .getDatabaseBridge()
                                            .saveData(stat, "PLAYER_STATS", !saved.isEmpty());
                                }
                            });
        }
    }
}
