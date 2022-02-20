package org.zibble.dbedwars.hooks.featherboard;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

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

    public void toggleScoreboard(Player player, boolean toggle) {
        FeatherBoardAPI.toggle(player, toggle);
    }

}
