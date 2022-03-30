package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

public abstract class SetupSessionTeamCommand extends SetupSessionCommand {

    public SetupSessionTeamCommand(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        if (args.length < 1) {
            member.sendMessage(this.invalidArgs(member, player, setupSession, args));
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

        this.execute(member, player, setupSession, color, args);
    }

    protected abstract Message invalidArgs(PlayerMember member, Player player, SetupSession setupSession, String[] args);

    protected abstract void execute(PlayerMember member, Player player, SetupSession setupSession, Color color, String[] args);

}
