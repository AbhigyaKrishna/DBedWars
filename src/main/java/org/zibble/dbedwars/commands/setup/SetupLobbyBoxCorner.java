package org.zibble.dbedwars.commands.setup;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

public class SetupLobbyBoxCorner extends CommandNode {

    private final SetupSessionManager manager;
    private final Messaging messaging;

    public SetupLobbyBoxCorner(SetupSessionManager manager, Messaging messaging) {
        this.manager = manager;
        this.messaging = messaging;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerMember member = messaging.getMessagingMember(player);

        SetupSession setupSession = this.manager.getSetupSessionOf(player);
        if (setupSession == null) {
            member.sendMessage(PluginLang.NOT_IN_SETUP_SESSION.asMessage());
            return;
        }

        switch (args[0]) {
            case "1": {
                setupSession.setupWaitingBoxCorner1();
                break;
            }
            case "2": {
                setupSession.setupWaitingBoxCorner2();
                break;
            }
            default: {
                member.sendMessage(PluginLang.SETUP_SESSION_BOX_CORNER_USAGE.asMessage());
                break;
            }
        }
    }

}
