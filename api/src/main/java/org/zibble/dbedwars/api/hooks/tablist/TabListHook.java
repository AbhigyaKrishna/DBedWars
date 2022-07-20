package org.zibble.dbedwars.api.hooks.tablist;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.Hook;

public interface TabListHook extends Hook {

    void setPrefix(Player player, String prefix);

    void setName(Player player, String name);

    void setSuffix(Player player, String suffix);

    void resetPrefix(Player player);

    void resetName(Player player);

    void resetSuffix(Player player);

    String getPrefix(Player player);

    String getName(Player player);

    String getSuffix(Player player);
    
}
