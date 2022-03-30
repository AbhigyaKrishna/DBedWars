package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

public class SetupSpectatorLocation extends SetupSessionCommand {

    public SetupSpectatorLocation(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        setupSession.setupSpectatorLocation();
    }

}
