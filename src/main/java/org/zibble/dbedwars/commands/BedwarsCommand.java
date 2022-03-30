package org.zibble.dbedwars.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.setup.BaseTeamDetection;
import org.zibble.dbedwars.setup.GenDetection;

import java.util.Optional;

@CommandAlias("bedwars|bedwar|bw")
public class BedwarsCommand extends BaseCommand {

    private final DBedwars plugin;

    public BedwarsCommand(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Subcommand("start")
    public void onStart(Player player, String arenaName) {
        if (!this.plugin.getGameManager().containsArena(arenaName)) {
            Messaging.get().getMessagingMember(player).sendMessage(ConfigLang.NO_ARENA_FOUND_W_NAME.asMessage());
            return;
        }

        Arena arena = this.plugin.getGameManager().getArena(arenaName);
        this.plugin.getThreadHandler().submitAsync(() -> {
            if (arena.getWorld() == null)
                arena.load();
            BedwarsCommand.this.plugin.getThreadHandler().submitSync(() -> arena.joinGame(player));
        });
    }

    @Subcommand("end")
    public void onEnd(Player player) {
        Arena arena = this.plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena == null) {
            Messaging.get().getMessagingMember(player).sendMessage(ConfigLang.NOT_IN_AN_ARENA.asMessage());
            return;
        }

        arena.end();
    }

    @Subcommand("message")
    public void test(Player player, String type) {
        Messaging.get().getMessagingMember(player).sendMessage(ConfigLang.valueOf(type).asMessage());
    }

    @Subcommand("testspawn")
    public void testSpawn(Player player) {
        Optional<Block> block = GenDetection.detect(player.getLocation(), 10);
        if (block.isPresent()) {
            player.sendMessage("BLOCK FOUND");
            block.get().setType(Material.GLOWSTONE);
            return;
        }
        player.sendMessage("COULD NOT FIND");
    }

    @Subcommand("testteam")
    public void testTeam(Player player) {
        Optional<Color> color = BaseTeamDetection.detect(player.getLocation(), 20);
        if (color.isPresent()) {
            player.sendMessage(color.get().getName());
            return;
        }
        player.sendMessage("COULD NOT FIND!!!");
    }
}
