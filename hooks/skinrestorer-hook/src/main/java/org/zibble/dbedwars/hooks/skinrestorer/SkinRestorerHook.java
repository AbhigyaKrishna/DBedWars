package org.zibble.dbedwars.hooks.skinrestorer;

import net.skinsrestorer.api.bukkit.events.SkinApplyBukkitEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.hooks.vanish.VanishHook;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

public class SkinRestorerHook extends PluginDependence implements Listener {

    private Class<? extends VanishHook> internalVanishHook;

    public SkinRestorerHook() {
        super("SkinsRestorer");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            try {
                internalVanishHook = Class.forName("org.zibble.dbedwars.hooks.defaults.VanishHook").asSubclass(VanishHook.class);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Bukkit.getServer().getPluginManager().registerEvents(this, DBedWarsAPI.getApi().getPlugin());
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into SkinsRestorer!"));
        }
        return true;
    }

    @EventHandler
    public void onSkinApply(SkinApplyBukkitEvent event) {
        if (event.isCancelled())
            return;

        if (DBedWarsAPI.getApi().getHookManager().getVanishHook().isVanished(event.getWho())
                && DBedWarsAPI.getApi().getHookManager().getVanishHook().getClass().equals(internalVanishHook)) {
            DBedWarsAPI.getApi().getHookManager().getVanishHook().vanish(event.getWho());
        }
    }

}
