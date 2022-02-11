package com.pepedevs.dbedwars.hooks.tab;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.radium.plugin.PluginDependence;
import me.neznamy.tab.api.TabAPI;
import org.bukkit.plugin.Plugin;

public class TabHook extends PluginDependence {

    private TabAPI api;

    public TabHook(String name) {
        super("TAB");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            this.api = TabAPI.getInstance();
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into TAB!"));
        }
        return true;
    }

    public TabAPI getApi() {
        return this.api;
    }

}
