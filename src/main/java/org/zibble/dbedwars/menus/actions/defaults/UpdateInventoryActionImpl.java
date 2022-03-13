package org.zibble.dbedwars.menus.actions.defaults;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.menu.MenuAction;

public class UpdateInventoryActionImpl implements MenuAction {

    @Override
    public String tag() {
        return "[UPDATE]";
    }

    @Override
    public String description() {
        return "Updates the current open inventory";
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull String[] args) {
        //TODO

        return true;
    }
}
