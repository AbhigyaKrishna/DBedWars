package org.zibble.dbedwars.hooks.skinrestorer;

import net.skinsrestorer.api.bukkit.events.SkinApplyBukkitEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

public class SkinRestorerHook extends PluginDependence implements Listener {

    public SkinRestorerHook(String name) {
        super("SkinsRestorer");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if(plugin != null){
            Bukkit.getServer().getPluginManager().registerEvents(this, DBedWarsAPI.getApi().getPlugin());
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into SkinsRestorer!"));
        }
        return true;
    }

    @EventHandler
    public void onSkinApply(SkinApplyBukkitEvent event){
        if(event.isCancelled())
            return;


    }
}
