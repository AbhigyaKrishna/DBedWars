package org.zibble.dbedwars.api.commands.nodes;

import org.bukkit.command.CommandSender;

public abstract class CommandNode extends AbstractCommandNode {

    @Override
    public final void accept(CommandSender sender, String[] args) {
        this.execute(sender, args);
    }

    public abstract void execute(CommandSender sender, String[] args);

}
