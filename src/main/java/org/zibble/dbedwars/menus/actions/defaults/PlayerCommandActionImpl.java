package org.zibble.dbedwars.menus.actions.defaults;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.menu.MenuAction;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;

public class PlayerCommandActionImpl implements MenuAction {
    @Override
    public String tag() {
        return "[PLAYER]";
    }

    @Override
    public String description() {
        return "Runs a command on the behalf of a player";
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull String[] args) {
        if(args.length <= 1){
            DBedwars.getInstance().getLogger().warning("The action of "+tag()+" didn't provide a statement to execute player command on... Skipping!");
            return false;
        }

        final String command = args[1];
        player.performCommand(PlaceholderEntry.symbol("name", player.getName()).apply(command));
        return true;
    }
}
