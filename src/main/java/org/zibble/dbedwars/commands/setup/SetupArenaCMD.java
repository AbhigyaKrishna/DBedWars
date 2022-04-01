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
import org.zibble.dbedwars.game.ArenaDataHolderImpl;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.utils.PluginFileUtils;

import java.io.File;

@SubCommandNode(parent = "bw", value = "setup")
@Permission(value = "dbedwars.setup")
@PlayerOnly(consoleTry = "<red>You need to be a player to run this command!")
public class SetupArenaCMD extends CommandNode {

    private final DBedwars plugin;
    private final Messaging messaging;

    public SetupArenaCMD(DBedwars plugin, Messaging messaging) {
        this.plugin = plugin;
        this.messaging = messaging;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerMember member = messaging.getMessagingMember(player);

        if (args.length < 1) {
            member.sendMessage(AdventureMessage.from("<red>You need to specify a world name!"));
            return;
        }

        String identifier = args[0];
        if (!PluginFileUtils.checkWorldFolder(new File(identifier))) {
            member.sendMessage(AdventureMessage.from("<red>The world you specified does not exist!"));
            return;
        }

        this.plugin.getHookManager().getWorldAdaptor().loadWorldFromFolder(identifier, World.Environment.NORMAL).thenAccept(world -> {
            SchedulerUtils.runTask(() -> player.teleport(world.getSpawnLocation()));
            ArenaDataHolderImpl dataHolder = this.plugin.getGameManager().getDataHolder(identifier);
            if (dataHolder == null) {
                dataHolder = ArenaDataHolderImpl.create(identifier);
                this.plugin.getGameManager().getArenaDataHolders().add(dataHolder);
            }
            SetupSession setupSession = this.plugin.getSetupSessionManager().startSetupSession(world, player, dataHolder);
            // TODO: 31-03-2022 prompt
        });
    }

}

