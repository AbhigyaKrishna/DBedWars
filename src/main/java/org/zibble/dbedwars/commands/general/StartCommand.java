package org.zibble.dbedwars.commands.general;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;

@Permission("dbedwars.start")
@SubCommandNode(parent = "bw", value = "start")
public class StartCommand extends CommandNode {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return false;
    }

}
