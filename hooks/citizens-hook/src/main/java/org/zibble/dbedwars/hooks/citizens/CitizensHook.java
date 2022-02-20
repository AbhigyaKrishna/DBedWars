package org.zibble.dbedwars.hooks.citizens;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.plugin.PluginDependence;
import org.bukkit.plugin.Plugin;

public class CitizensHook extends PluginDependence {

    public CitizensHook() {
        super("Citizens");
    }

    @Override
    public void disable() {}

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into Citizens!"));
        }
        return true;
    }



}
