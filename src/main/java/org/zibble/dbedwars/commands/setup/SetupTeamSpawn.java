package org.zibble.dbedwars.commands.setup;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;
import org.zibble.dbedwars.utils.ConfigurationUtils;

public class SetupTeamSpawn extends CommandNode {

    private final SetupSessionManager manager;
    private final Messaging messaging;

    public SetupTeamSpawn(SetupSessionManager manager, Messaging messaging) {
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

        if (args.length < 1) {
            member.sendMessage(PluginLang.SETUP_SESSION_TEAM_SPAWN_USAGE.asMessage());
            return;
        }

        Color color = ConfigurationUtils.matchEnum(args[0], Color.VALUES);
        if (color == null) {
            member.sendMessage(PluginLang.SETUP_SESSION_INVALID_COLOR.asMessage());
            return;
        }

        if (!setupSession.isValidTeam(color)) {
            member.sendMessage(PluginLang.SETUP_SESSION_COLOR_NOT_FOUND_AS_TEAM.asMessage());
            return;
        }

        setupSession.setupTeamSpawn(color, player.getLocation());
    }
}
