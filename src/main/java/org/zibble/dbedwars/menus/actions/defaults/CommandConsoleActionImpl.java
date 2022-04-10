package org.zibble.dbedwars.menus.actions.defaults;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.menu.MenuAction;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;

public class CommandConsoleActionImpl implements MenuAction {

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
        if (args.length <= 1) {
            DBedwars.getInstance().getLogger().warning("The action of " + tag() + " didn't provide a statement to execute console command on... Skipping!");
            return false;
        }

        final String command = args[1];
        DBedwars.getInstance().getServer().dispatchCommand(
                DBedwars.getInstance().getServer().getConsoleSender(),
                PlaceholderEntry.symbol("name", player.getName()).apply(command)
        );
        return true;
    }

}
