package org.zibble.dbedwars.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.configuration.language.ConfigLang;

@PlayerOnly
@Permission("dbedwars.setlobby")
@SubCommandNode(parent = "bw", value = "setlobby")
public class SetLobby extends CommandNode {

    private final DBedwars plugin;
    private final Messaging messaging;

    public SetLobby(DBedwars plugin, Messaging messaging) {
        this.plugin = plugin;
        this.messaging = messaging;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        this.plugin.getGameManager().setLobbySpawn(player.getLocation());
        this.messaging.getMessagingMember(player).sendMessage(ConfigLang.LOBBY_LOCATION_SET.asMessage());
        return false;
    }

}
