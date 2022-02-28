package org.zibble.dbedwars.menus.actions.defaults;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.menu.MenuActions;

public class CommandConsoleActionImpl implements MenuActions {

    @Override
    public String tag() {
        return "[CONSOLE]";
    }

    @Override
    public String description() {
        return "Runs a command on the behalf of the player";
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull String[] args) {
        return false;
    }
}
