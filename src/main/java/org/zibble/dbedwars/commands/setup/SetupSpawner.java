package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

@PlayerOnly
@Permission("dbedwars.setup")
@SubCommandNode(parent = "bw.setup", value = "addspawner")
public class SetupSpawner extends SetupSessionCommand {

    private final DBedwars plugin;

    public SetupSpawner(DBedwars plugin, SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
        this.plugin = plugin;
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        if (args.length == 0) {
            member.sendMessage(PluginLang.SETUP_SESSION_GEN_USAGE.asMessage());
            return;
        }

        DropInfo spawner = this.getSpawner(args[0]);
        if (spawner == null) {
            member.sendMessage(PluginLang.SETUP_SESSION_TEAM_GEN_INVALID_DROP_TYPE.asMessage());
            return;
        }

        if (args.length == 1) {
            setupSession.addCommonSpawner(spawner, player.getLocation());
            return;
        }

        Color color = EnumUtil.matchEnum(args[1], Color.VALUES);
        if (color == null) {
            member.sendMessage(PluginLang.SETUP_SESSION_INVALID_COLOR.asMessage());
            return;
        }

        if (!setupSession.isValidTeam(color)) {
            member.sendMessage(PluginLang.SETUP_SESSION_COLOR_NOT_FOUND_AS_TEAM.asMessage());
            return;
        }

        setupSession.addTeamSpawner(color, spawner, player.getLocation());
    }

    private DropInfo getSpawner(String s) {
        for (DropInfo dropType : this.plugin.getGameManager().getDropTypes()) {
            if (dropType.getKey().get().equalsIgnoreCase(s)) {
                return dropType;
            }
        }

        return null;
    }

}
