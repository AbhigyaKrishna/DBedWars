package org.zibble.dbedwars.menus.actions.defaults;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.menu.MenuActions;

public class TitleActionImpl implements MenuActions {

    @Override
    public String tag() {
        return "[TITLE]";
    }

    @Override
    public String description() {
        return "Shows a title to the player";
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull String[] args) {
        return true;
    }
}
