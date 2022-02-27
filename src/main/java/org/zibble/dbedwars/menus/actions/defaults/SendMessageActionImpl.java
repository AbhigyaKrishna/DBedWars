package org.zibble.dbedwars.menus.actions.defaults;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.menu.MenuActions;
import org.zibble.dbedwars.menus.actions.ActionRegistry;

public class SendMessageActionImpl implements MenuActions {

    @Override
    public String tag() {
        return "[SEND_MESSAGE]";
    }

    @Override
    public String description() {
        return "Sends a message to the clicked player!";
    }

    //TODO
    @Override
    public boolean execute(@NotNull Player player, @NotNull String[] args) {
        return false;
    }
}
