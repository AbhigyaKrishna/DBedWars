package org.zibble.dbedwars.api.hooks.placholder;

import org.bukkit.OfflinePlayer;

public interface PlaceholderRegistry {

    String getId();

    String resolve(OfflinePlayer player, String params);

}
