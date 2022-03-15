package org.zibble.dbedwars.commands.setup;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;
import org.zibble.dbedwars.utils.ConfigurationUtils;

public class SetupTeamShop extends CommandNode {

    private final SetupSessionManager manager;
    private final Messaging messaging;

    public SetupTeamShop(SetupSessionManager manager, Messaging messaging) {
        this.manager = manager;
        this.messaging = messaging;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerMember member = this.messaging.getMessagingMember(player);

        SetupSession setupSession = this.manager.getSetupSessionOf(player);
        if (setupSession == null) {
            member.sendMessage(PluginLang.NOT_IN_SETUP_SESSION.asMessage());
            return;
        }

        if (args.length < 1) {
            member.sendMessage(PluginLang.SETUP_SESSION_TEAM_SHOP_USAGE.asMessage());
            return;
        }

        Color color = EnumUtil.matchEnum(args[0], Color.VALUES);
        if (color == null) {
            member.sendMessage(PluginLang.SETUP_SESSION_INVALID_COLOR.asMessage());
            return;
        }

        if (!setupSession.isValidTeam(color)) {
            member.sendMessage(PluginLang.SETUP_SESSION_COLOR_NOT_FOUND_AS_TEAM.asMessage());
            return;
        }

        setupSession.setupTeamShopNPC(color, player.getLocation());
    }
}
