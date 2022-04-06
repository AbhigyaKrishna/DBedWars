package org.zibble.dbedwars.commands.setup;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

public abstract class SetupSessionOptionalTeamCommand extends SetupSessionTeamCommand {

    public SetupSessionOptionalTeamCommand(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        Color color;
        if (args.length >= this.teamNameIndex() + 1) {
            color = EnumUtil.matchEnum(args[teamNameIndex()], Color.VALUES);
        } else {
            color = this.findTeam(player, setupSession, player.getLocation());
        }
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

    @Override
    protected Message invalidArgs(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        return null;
    }

    protected abstract Color findTeam(Player player, SetupSession session, Location location);

}
