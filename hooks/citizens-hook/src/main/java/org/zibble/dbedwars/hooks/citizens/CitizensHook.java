package org.zibble.dbedwars.hooks.citizens;

import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

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
