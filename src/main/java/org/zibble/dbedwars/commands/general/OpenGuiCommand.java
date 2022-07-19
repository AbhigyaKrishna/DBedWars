package org.zibble.dbedwars.commands.general;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.guis.cfginternal.ConfigGui;

@PlayerOnly
@Permission("dbedwars.opengui")
@SubCommandNode(parent = "bw", value = "opengui")
public class OpenGuiCommand extends GeneralCommandNode {

    public OpenGuiCommand(DBedwars plugin, Messaging messaging) {
        super(plugin, messaging);
    }

    @Override
    public boolean execute(MessagingMember member, CommandSender sender, String[] args) {
        if (args.length == 0) {
            member.sendMessage(AdventureMessage.from("<red>You must specify a gui to open."));
            return true;
        }
        ConfigGui gui = ConfigGui.GUIS.get(Key.of(args[0]));
        if (gui == null) {
            member.sendMessage(AdventureMessage.from("<red>No gui found for <yellow>" + args[0] + "."));
            return true;
        }
        gui.open((Player) sender);
        return true;
    }

}
