package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.configurable.ConfigurableItemSpawner;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

public class SetupTeamGen extends SetupSessionTeamCommand {

    private final DBedwars plugin;

    public SetupTeamGen(DBedwars plugin, SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
        this.plugin = plugin;
    }

    @Override
    protected Message invalidArgs(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        return PluginLang.SETUP_SESSION_TEAM_GEN_USAGE.asMessage();
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, Color color, String[] args) {
        for (ConfigurableItemSpawner dropType : this.plugin.getConfigHandler().getDropTypes()) {
            if (dropType.getId().equalsIgnoreCase(args[0])) {
                //TODO dropinfos
                setupSession.addTeamSpawner(color, dropType);
                return;
            }
        }
        member.sendMessage(PluginLang.SETUP_SESSION_TEAM_GEN_INVALID_DROP_TYPE.asMessage());
    }

}
