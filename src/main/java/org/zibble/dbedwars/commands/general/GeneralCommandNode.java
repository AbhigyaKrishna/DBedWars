package org.zibble.dbedwars.commands.general;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;

public abstract class GeneralCommandNode extends CommandNode {

    protected final DBedwars plugin;
    protected final Messaging messaging;

    public GeneralCommandNode(DBedwars plugin, Messaging messaging) {
        this.plugin = plugin;
        this.messaging = messaging;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessagingMember member = this.messaging.getMemberOf(sender);
        return this.execute(member, sender, args);
    }

    public abstract boolean execute(MessagingMember member, CommandSender sender, String[] args);

}
