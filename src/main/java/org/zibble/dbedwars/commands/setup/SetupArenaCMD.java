package org.zibble.dbedwars.commands.setup;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.util.SchedulerUtils;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.ArenaDataHolderImpl;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.utils.PluginFileUtils;

import java.io.File;

@PlayerOnly
@Permission(value = "dbedwars.setup")
@SubCommandNode(parent = "bw", value = "setuparena")
public class SetupArenaCMD extends CommandNode {

    private final DBedwars plugin;
    private final Messaging messaging;

    public SetupArenaCMD(DBedwars plugin, Messaging messaging) {
        this.plugin = plugin;
        this.messaging = messaging;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerMember member = messaging.getMessagingMember(player);

        if (this.plugin.getGameManager().getLobbySpawn() == null) {
            member.sendMessage(AdventureMessage.from("<red>You need to set the lobby spawn first!"));
            member.sendMessage(AdventureMessage.from("<red>Use <yellow><click:run_command:'/bw setlobby'><hover:show_text:'<green>Click!'>/bw setlobby</hover></click> <red>to set the lobby spawn!"));
            return false;
        }

        if (this.plugin.getSetupSessionManager().isInSetupSession((Player) sender)) {
            member.sendMessage(PluginLang.ALREADY_IN_SETUP_SESSION.asMessage());
            return false;
        }

        if (args.length < 1) {
            member.sendMessage(AdventureMessage.from("<red>You need to specify a world name!"));
            return false;
        }

        String identifier = args[0];
        if (!PluginFileUtils.checkWorldFolder(new File(identifier))) {
            member.sendMessage(AdventureMessage.from("<red>The world you specified does not exist!"));
            return false;
        }

        this.plugin.getHookManager().getWorldAdaptor().loadWorldFromFolder(identifier, World.Environment.NORMAL).thenAccept(world -> {
            world.setKeepSpawnInMemory(true);
            SchedulerUtils.runTask(() -> player.teleport(world.getSpawnLocation()));
            ArenaDataHolderImpl dataHolder = this.plugin.getGameManager().getDataHolder(identifier);
            if (dataHolder == null) {
                dataHolder = ArenaDataHolderImpl.create(identifier);
                this.plugin.getGameManager().getArenaDataHolders().add(dataHolder);
            }
            SetupSession setupSession = this.plugin.getSetupSessionManager().startSetupSession(world, player, dataHolder);
            setupSession.promptWholeList();
        });

        return true;
    }

}

