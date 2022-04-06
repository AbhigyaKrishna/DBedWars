package org.zibble.dbedwars.commands.setup;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

@PlayerOnly
@Permission("dbedwars.setup")
@SubCommandNode(parent = "bw", value = "setup")
public class SetupBaseCommand extends CommandNode {

    private final DBedwars plugin;
    private final SetupSessionManager sessionManager;
    private final Messaging messaging;

    public SetupBaseCommand(DBedwars plugin, SetupSessionManager manager, Messaging messaging) {
        this.plugin = plugin;
        this.sessionManager = manager;
        this.messaging = messaging;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerMember member = messaging.getMessagingMember(player);

        if (this.plugin.getGameManager().getLobbySpawn() == null) {
            member.sendMessage(AdventureMessage.from("<red>You need to set the lobby spawn first!"));
            member.sendMessage(AdventureMessage.from("<red>Use <yellow><click:run_command:'/bw setlobby'><hover:show_text:'<green>Click!'>/bw setlobby</hover></click> <red>to set the lobby spawn!"));
            return false;
        }

        if (args.length == 0) {
            if (this.sessionManager.isInSetupSession(player)) {
                this.sessionManager.getSetupSessionOf(player).promptWholeList();
            } else {
                member.sendMessage(PluginLang.NOT_IN_SETUP_SESSION.asMessage());
                return false;
            }
        }

        return true;
    }

}
