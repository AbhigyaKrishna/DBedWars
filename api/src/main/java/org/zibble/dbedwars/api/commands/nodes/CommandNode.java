package org.zibble.dbedwars.api.commands.nodes;

import org.bukkit.command.CommandSender;

public abstract class CommandNode extends AbstractCommandNode {

    @Override
    public final boolean accept(CommandSender sender, String[] args) {
        return this.execute(sender, args);
    }

    public abstract boolean execute(CommandSender sender, String[] args);

}
