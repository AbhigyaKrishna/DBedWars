package org.zibble.dbedwars.commands.setup;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;

@Permission(value = "dbedwars.setup")
@PlayerOnly(consoleTry = "<red>You need to be a player to run this command!")
public class SetupArenaCMD extends CommandNode {

    public SetupArenaCMD() {

    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }

}

