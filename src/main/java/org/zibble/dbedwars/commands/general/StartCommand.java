package org.zibble.dbedwars.commands.general;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;

@PlayerOnly
@Permission("dbedwars.start")
@SubCommandNode(parent = "bw", value = "start")
public class StartCommand extends GeneralCommandNode {

    public StartCommand(DBedwars plugin, Messaging messaging) {
        super(plugin, messaging);
    }

    @Override
    public boolean execute(MessagingMember member, CommandSender sender, String[] args) {

        if (args.length == 0) {
            member.sendMessage(AdventureMessage.from("<red>Usage: /bw start <arena>"));
            return false;
        }

        String arenaName = args[0];

        Arena arena = this.plugin.getGameManager().createArena(arenaName);
        if (arena == null) {
            member.sendMessage(AdventureMessage.from("<red>Arena <gold>" + arenaName + "<red> does not exist."));
            return true;
        }

        arena.joinGame((Player) sender);

        return true;
    }

}
