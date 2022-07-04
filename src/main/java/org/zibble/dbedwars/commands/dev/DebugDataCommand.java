package org.zibble.dbedwars.commands.dev;

import com.google.common.base.Strings;
import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.game.ArenaDataHolderImpl;
import org.zibble.dbedwars.game.GameManagerImpl;
import org.zibble.dbedwars.game.arena.ArenaCategoryImpl;
import org.zibble.dbedwars.game.arena.view.ShopInfoImpl;

@Permission("dbedwars.dev")
@SubCommandNode(parent = "bw.debug", value = "data")
public class DebugDataCommand extends DebugBaseCommand {

    private final GameManagerImpl manager;

    public DebugDataCommand(DBedwars plugin, Messaging messaging) {
        super(plugin, messaging);
        this.manager = plugin.getGameManager();
    }

    @Override
    public boolean execute(MessagingMember member, CommandSender sender, String[] args) {
        AdventureMessage message = AdventureMessage.from("<dark_blue>" + Strings.repeat("-", 20));
        switch (args[0].toLowerCase()) {
            case "spawners": {
                for (DropInfo dropType : this.manager.getDropTypes()) {
                    message.addLine(this.formatColors(dropType.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            case "categories": {
                for (ArenaCategoryImpl category : this.manager.getCategories()) {
                    message.addLine(this.formatColors(category.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            case "arenas": {
                for (ArenaDataHolderImpl arena : this.manager.getArenaDataHolders()) {
                    message.addLine(this.formatColors(arena.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            case "scoreboards": {
                for (ScoreboardData scoreboard : this.manager.getScoreboardData().values()) {
                    message.addLine(this.formatColors(scoreboard.toString()));
                    message.addLine("<dark_blue>" + Strings.repeat("-", 20));
                }
                break;
            }
            case "shops": {
                for (ShopInfoImpl shop : this.manager.getShops()) {
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
