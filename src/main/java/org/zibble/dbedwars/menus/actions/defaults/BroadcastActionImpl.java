package org.zibble.dbedwars.menus.actions.defaults;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.menu.MenuActions;

public class BroadcastActionImpl implements MenuActions {

    @Override
    public String tag() {
        return "[BROADCAST]";
    }

    @Override
    public String description() {
        return "Broadcast a message to the entire server";
    }

    //TODO
    @Override
    public boolean execute(@NotNull Player player, @NotNull String[] args) {
        return false;
    }
}
