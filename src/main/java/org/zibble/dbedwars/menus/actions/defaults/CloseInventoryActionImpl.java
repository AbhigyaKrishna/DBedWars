package org.zibble.dbedwars.menus.actions.defaults;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.menu.MenuActions;

public class CloseInventoryActionImpl implements MenuActions {

    @Override
    public String tag() {
        return "[CLOSE]";
    }

    @Override
    public String description() {
        return "Close the current open menu of the player";
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull String[] args) {
        player.closeInventory();
        return true;
    }
}
