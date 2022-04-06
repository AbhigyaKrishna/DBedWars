package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

@PlayerOnly
@Permission("dbedwars.setup")
@SubCommandNode(parent = "bw.setup", value = "addteam")
public class SetupAddTeam extends SetupSessionCommand {

    public SetupAddTeam(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        if (args.length < 1) {
            member.sendMessage(PluginLang.SETUP_SESSION_ADD_TEAM_USAGE.asMessage());
            return;
        }

        Color color = EnumUtil.matchEnum(args[0], Color.VALUES);
        if (color == null) {
            member.sendMessage(PluginLang.SETUP_SESSION_INVALID_COLOR.asMessage());
            return;
        }

        if (setupSession.isValidTeam(color)) {
            member.sendMessage(PluginLang.SETUP_SESSION_COLOR_ALREADY_ADDED.asMessage());
            return;
        }

        setupSession.addTeam(color);
    }

}
