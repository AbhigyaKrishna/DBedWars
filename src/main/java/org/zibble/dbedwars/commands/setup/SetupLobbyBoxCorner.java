package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

public class SetupLobbyBoxCorner extends SetupSessionCommand {

    public SetupLobbyBoxCorner(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
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
