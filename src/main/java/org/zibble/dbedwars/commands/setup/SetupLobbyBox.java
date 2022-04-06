package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

@PlayerOnly
@Permission("dbedwars.setup")
@SubCommandNode(parent = "bw.setup", value = "lobbyarea")
public class SetupLobbyBox extends SetupSessionCommand {

    public SetupLobbyBox(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        switch (args[0]) {
            case "1":
                setupSession.setupWaitingBoxCorner1(player.getLocation());
                break;
            case "2":
                setupSession.setupWaitingBoxCorner2(player.getLocation());
                break;
            default:
                setupSession.setupWaitingBoxArea();
        }
    }

}
