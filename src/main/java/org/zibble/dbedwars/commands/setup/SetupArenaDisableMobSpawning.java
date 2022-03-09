package org.zibble.dbedwars.commands.setup;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

@PlayerOnly()
@SubCommandNode(parent = "bw.admin.setup", value = "disable_mobs")
public class SetupArenaDisableMobSpawning extends CommandNode {

    private final SetupSessionManager manager;
    private final Messaging messaging;

    public SetupArenaDisableMobSpawning(SetupSessionManager manager, Messaging messaging) {
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

        setupSession.disableMobSpawning();
    }

}
