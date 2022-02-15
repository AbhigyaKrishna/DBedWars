package com.pepedevs.dbedwars.hooks.featherboard;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.plugin.PluginDependence;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FeatherBoardHook extends PluginDependence {

    public FeatherBoardHook() {
        super("FeatherBoard");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into FeatherBoard!"));
        }
        return true;
    }

    @Override
    public void disable() {

    }

    public void toggleScoreboard(Player player, boolean toggle) {
        FeatherBoardAPI.toggle(player, toggle);
    }

}
