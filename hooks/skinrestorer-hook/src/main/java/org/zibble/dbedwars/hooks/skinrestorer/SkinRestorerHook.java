package org.zibble.dbedwars.hooks.skinrestorer;

import net.skinsrestorer.api.bukkit.events.SkinApplyBukkitEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.hooks.Hook;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class SkinRestorerHook extends PluginDependence implements Listener, Hook {

    private final Collection<UUID> blacklisted = new ArrayList<>();

    public SkinRestorerHook() {
        super("SkinsRestorer");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Bukkit.getServer().getPluginManager().registerEvents(this, DBedWarsAPI.getApi().getPlugin());
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into SkinsRestorer!"));
        }
        return true;
    }

    public void addBlacklisted(UUID uuid) {
        this.blacklisted.add(uuid);
    }

    public void removeBlacklisted(UUID uuid) {
        this.blacklisted.remove(uuid);
    }

    @EventHandler
    public void onSkinApply(SkinApplyBukkitEvent event) {
        if (event.isCancelled())
            return;

        if (this.blacklisted.contains(event.getWho().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

}
