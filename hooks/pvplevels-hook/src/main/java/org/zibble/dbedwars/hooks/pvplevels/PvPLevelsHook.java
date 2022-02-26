package org.zibble.dbedwars.hooks.pvplevels;

import me.MathiasMC.PvPLevels.api.PvPLevelsAPI;
import me.MathiasMC.PvPLevels.data.PlayerConnect;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.points.PointsHook;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

public class PvPLevelsHook extends PluginDependence implements PointsHook {

    private PvPLevelsAPI api;

    public PvPLevelsHook() {
        super("PvPLevels");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if(plugin != null){
            api = PvPLevelsAPI.getInstance();
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into PvPLevels!"));
        }
        return true;
    }

    @Override
    public long getCurrentXP(@NotNull Player player) {
        PlayerConnect playerConnect = api.getPlayerConnect(player.getUniqueId().toString());
        if(playerConnect == null)
            return 0L;

        return playerConnect.getXp();
    }

    @Override
    public void addXP(@NotNull Player player, long toAdd) {
        PlayerConnect playerConnect = api.getPlayerConnect(player.getUniqueId().toString());
        if(playerConnect == null)
            return;

        playerConnect.setXp(playerConnect.getXp() + toAdd);
    }

    @Override
    public void reduceXP(@NotNull Player player, long toReduce) {
        PlayerConnect playerConnect = api.getPlayerConnect(player.getUniqueId().toString());
        if(playerConnect == null)
            return;

        long xpPos = playerConnect.getXp() - toReduce;
        if(xpPos <= 0)
            playerConnect.setXp(0);
        else
            playerConnect.setXp(playerConnect.getXp() - toReduce);
    }
}
