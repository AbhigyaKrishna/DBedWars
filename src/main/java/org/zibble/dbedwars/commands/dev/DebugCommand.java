package org.zibble.dbedwars.commands.dev;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;

@Permission("dbedwars.admin")
@SubCommandNode(parent = "bw", value = "debug")
public class DebugCommand extends CommandNode {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return true;
    }

}
