package org.zibble.dbedwars.api.commands.nodes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommandNode {

    protected AbstractCommandNode() {}

    public abstract boolean accept(CommandSender sender, String[] args);

    public String[] hint(Player player) {
        return new String[0];
    }

}
