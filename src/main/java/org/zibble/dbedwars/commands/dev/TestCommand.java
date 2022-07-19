package org.zibble.dbedwars.commands.dev;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.placeholders.PlayerPlaceholderEntry;
import org.zibble.dbedwars.commands.general.GeneralCommandNode;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@PlayerOnly
@Permission("dbedwars.test")
@SubCommandNode(parent = "bw", value = "test")
public class TestCommand extends GeneralCommandNode {

    public TestCommand(DBedwars plugin, Messaging messaging) {
        super(plugin, messaging);
    }

    @Override
    public boolean execute(MessagingMember member, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ScoreboardData scoreboard = this.plugin.getGameManager().getScoreboardData().get("lobby");
        scoreboard.show(player,
                PlaceholderEntry.symbol("date", SimpleDateFormat.getDateInstance().format(Date.from(Instant.now()))),
                PlayerPlaceholderEntry.symbol("player_name", HumanEntity::getName),
                PlayerPlaceholderEntry.symbol("player_uuid", p -> p.getUniqueId().toString())
        );
        return true;
    }

}
