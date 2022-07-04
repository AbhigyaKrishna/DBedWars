package org.zibble.dbedwars.commands.dev;

import com.google.common.base.Strings;
import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.configuration.configurable.*;
import org.zibble.dbedwars.handler.ConfigHandler;

@Permission("dbedwars.dev")
@SubCommandNode(parent = "bw.debug", value = "config")
public class DebugConfigCommand extends DebugBaseCommand {

    private final ConfigHandler configHandler;

    public DebugConfigCommand(DBedwars plugin, Messaging messaging) {
        super(plugin, messaging);
        this.configHandler = plugin.getConfigHandler();
    }

    @Override
    public boolean execute(MessagingMember member, CommandSender sender, String[] args) {
        AdventureMessage message = AdventureMessage.from("<dark_blue>" + Strings.repeat("-", 20));
        switch (args[0].toLowerCase()) {
            case "spawners": {
                for (ConfigurableItemSpawner dropType : this.configHandler.getDropTypes()) {
                    message.addLine(this.formatColors(dropType.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            case "categories": {
                for (ConfigurableArenaCategory category : this.configHandler.getCategories()) {
                    message.addLine(this.formatColors(category.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            case "arenas": {
                for (ConfigurableArena arena : this.configHandler.getArenas()) {
                    message.addLine(this.formatColors(arena.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            case "traps": {
                for (ConfigurableTrap trap : this.configHandler.getTraps()) {
                    message.addLine(this.formatColors(trap.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            case "scoreboards": {
                for (ConfigurableScoreboard scoreboard : this.configHandler.getScoreboards()) {
                    message.addLine(this.formatColors(scoreboard.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            case "shops": {
                for (ConfigurableShop shop : this.configHandler.getShops().values()) {
                    message.addLine(this.formatColors(shop.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            default: {
                message.setMessage("<red>Unknown argument: " + args[0]);
            }
        }
        member.sendMessage(message);
        return true;
    }

}
