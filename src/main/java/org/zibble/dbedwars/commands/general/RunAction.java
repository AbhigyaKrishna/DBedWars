package org.zibble.dbedwars.commands.general;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionProvider;
import org.zibble.dbedwars.commands.general.GeneralCommandNode;

@PlayerOnly
@Permission("dbedwars.testaction")
@SubCommandNode(parent = "bw", value = "runaction")
public class RunAction extends GeneralCommandNode {

    public RunAction(DBedwars plugin, Messaging messaging) {
        super(plugin, messaging);
    }

    @Override
    public boolean execute(MessagingMember member, CommandSender sender, String[] args) {
        String action = String.join(" ", args);
        ActionProvider provider = ActionProvider.fromString(action);
        try {
            ScriptVariable<MessagingMember> messaging = ScriptVariable.of("member", member);
            ScriptVariable<Location> loc = ScriptVariable.of("location", ((Player) sender).getLocation());
            ScriptVariable<Player> player = ScriptVariable.of("player", (Player) sender);
            provider.execute(messaging, loc, player);
            member.sendMessage(AdventureMessage.from("<green>Action executed."));
        } catch (Exception e) {
            e.printStackTrace();
            member.sendMessage(AdventureMessage.from("<red>Action failed."));
        }
        return true;
    }

}
