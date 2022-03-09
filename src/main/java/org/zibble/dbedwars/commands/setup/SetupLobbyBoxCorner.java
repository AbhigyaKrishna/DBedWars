package org.zibble.dbedwars.commands.setup;

import org.bukkit.block.Block;
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
                Block block = player.getTargetBlockExact(5);
                if (block == null) {
                    member.sendMessage(PluginLang.SETUP_SESSION_NOT_LOOKING_AT_BLOCK.asMessage());
                    return;
                }
                setupSession.setupLobbyCorner1(block);
                break;
            }
            case "2": {
                Block block = player.getTargetBlockExact(5);
                if (block == null) {
                    member.sendMessage(PluginLang.SETUP_SESSION_NOT_LOOKING_AT_BLOCK.asMessage());
                    return;
                }
                setupSession.setupLobbyCorner2(block);
                break;
            }
            default: {
                member.sendMessage(PluginLang.SETUP_SESSION_BOX_CORNER_USAGE.asMessage());
                break;
            }
        }
    }

}
