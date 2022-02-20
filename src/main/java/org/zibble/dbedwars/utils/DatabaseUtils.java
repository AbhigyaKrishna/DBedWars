package org.zibble.dbedwars.utils;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.cache.DataCache;
import org.zibble.dbedwars.cache.PlayerStats;

import java.util.List;

public class DatabaseUtils {

    public static void saveGameData(Arena arena) {
        for (ArenaPlayer player : arena.getPlayers()) {
            DBedwars.getInstance()
                    .getThreadHandler()
                    .submitAsync(
                            () -> {
                                List<DataCache> saved =
                                        DBedwars.getInstance()
                                                .getDatabaseBridge()
                                                .getAsDataCache(
                                                        "UUID",
                                                        player.getUUID()
                                                                .toString(),
                                                        "PLAYER_STATS");
                                PlayerStats stat;
                                if (!saved.isEmpty()) {
                                    stat = (PlayerStats) saved.get(0);
                                    stat.setCOINS(stat.getCOINS() + 10);
                                    stat.setLEVEL(stat.getLEVEL() + 1);
                                    stat.setPOINTS(stat.getPOINTS() + Utils.calculatePoint(player));
                                } else {
                                    stat =
                                            new PlayerStats(
                                                    player.getUUID().toString(),
                                                    player.getName());
                                    stat.setCOINS(10);
                                    stat.setLEVEL(1);
                                    stat.setPOINTS(Utils.calculatePoint(player));
                                }
                                DBedwars.getInstance()
                                        .getDatabaseBridge()
                                        .saveData(stat, "PLAYER_STATS", !saved.isEmpty());
                            });
        }
    }
}