package org.zibble.dbedwars.hooks.tab;

import me.neznamy.tab.api.TabAPI;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;
import org.zibble.dbedwars.hooks.tab.scoreboard.TabScoreboardHook;
import org.zibble.dbedwars.hooks.tab.tablist.TabTablistHook;

public class TabHook extends PluginDependence {

    private TabAPI api;
    private TabScoreboardHook scoreboardHook;
    private TabTablistHook tablistHook;

    public TabHook() {
        super("TAB");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            this.api = TabAPI.getInstance();
            this.scoreboardHook = new TabScoreboardHook(this);
            this.tablistHook = new TabTablistHook(this);
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into TAB!"));
        }
        return true;
    }

    public TabAPI getApi() {
        return this.api;
    }

    public TabScoreboardHook getScoreboardHook() {
        return this.scoreboardHook;
    }

    public TabTablistHook getTablistHook() {
        return tablistHook;
    }

}
