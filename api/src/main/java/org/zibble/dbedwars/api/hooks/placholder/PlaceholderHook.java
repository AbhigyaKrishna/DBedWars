package org.zibble.dbedwars.api.hooks.placholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.Hook;

import java.util.List;

public interface PlaceholderHook extends Hook {

    void register(PlaceholderRegistry registry);

    String setPlaceholders(Player player, String message);

    List<String> setPlaceholders(Player player, List<String> messages);

    String setPlaceholders(OfflinePlayer player, String message);

    List<String> setPlaceholders(OfflinePlayer player, List<String> messages);

}
