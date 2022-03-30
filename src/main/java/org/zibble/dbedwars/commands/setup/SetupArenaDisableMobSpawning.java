package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

@SubCommandNode(parent = "bw.admin.setup", value = "disable_mobs")
public class SetupArenaDisableMobSpawning extends SetupSessionCommand {

    public SetupArenaDisableMobSpawning(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        setupSession.disableMobSpawning();
    }

}
