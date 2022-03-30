package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

public class SetupTeamShop extends SetupSessionTeamCommand {

    public SetupTeamShop(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected Message invalidArgs(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        return PluginLang.SETUP_SESSION_TEAM_SHOP_USAGE.asMessage();
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, Color color, String[] args) {
        setupSession.addTeamShop(color, );
    }

}
