package org.zibble.dbedwars.commands;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.commands.annotations.ParentCommandNode;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;

@ParentCommandNode(value = "bw", aliases = {"bedwars", "dbedwars", "dbedwar", "dbedwars", "dbw"})
public class BedWarsCommand extends CommandNode {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            // TODO: 10-04-2022 do something
            return true;
        }
        return true;
    }

}
