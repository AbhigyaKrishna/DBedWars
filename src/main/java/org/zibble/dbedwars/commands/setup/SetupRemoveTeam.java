package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

@PlayerOnly
@Permission("dbedwars.setup")
@SubCommandNode(parent = "bw.setup", value = "removeteam")
public class SetupRemoveTeam extends SetupSessionTeamCommand {

    public SetupRemoveTeam(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected int teamNameIndex() {
        return 0;
    }

    @Override
    protected Message invalidArgs(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        return PluginLang.SETUP_SESSION_REMOVE_TEAM_USAGE.asMessage();
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, Color color, String[] args) {
        setupSession.removeTeam(color);
    }

}
