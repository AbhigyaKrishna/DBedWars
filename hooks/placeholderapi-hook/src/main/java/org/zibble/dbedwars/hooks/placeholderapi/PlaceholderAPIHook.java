package org.zibble.dbedwars.hooks.placeholderapi;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderHook;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderRegistry;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

import java.util.List;

public class PlaceholderAPIHook extends PluginDependence implements PlaceholderHook {

    public PlaceholderAPIHook() {
        super("PlaceholderAPI");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into PlaceholderAPI!"));
        }
        return true;
    }

    @Override
    public void register(PlaceholderRegistry registry) {
        new PAPIPlaceholderBridge(registry).register();
    }

    @Override
    public String setPlaceholders(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    @Override
    public List<String> setPlaceholders(Player player, List<String> messages) {
        return PlaceholderAPI.setPlaceholders(player, messages);
    }

    @Override
    public String setPlaceholders(OfflinePlayer player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    @Override
    public List<String> setPlaceholders(OfflinePlayer player, List<String> messages) {
        return PlaceholderAPI.setPlaceholders(player, messages);
    }

}
