package com.pepedevs.dbedwars.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import me.Abhigya.core.util.StringUtils;
import org.bukkit.entity.Player;

@CommandAlias("bedwars|bedwar|bw")
public class BedwarsCommand extends BaseCommand {

    private final DBedwars plugin;

    public BedwarsCommand(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Subcommand("start")
    public void onStart(Player player, String arenaName) {
        if (!this.plugin.getGameManager().containsArena(arenaName)
                || !this.plugin.getGameManager().getArena(arenaName).isEnabled()) {
            player.sendMessage(
                    StringUtils.translateAlternateColorCodes("&cNo arena found with this name!"));
            return;
        }

        Arena arena = this.plugin.getGameManager().getArena(arenaName);
        this.plugin
                .getThreadHandler()
                .submitAsync(
                        () -> {
                            if (arena.getWorld() == null) {
                                arena.load();
                            }
                            this.plugin
                                    .getThreadHandler()
                                    .submitSync(() -> arena.addPlayer(player));
                        });
    }

    @Subcommand("end")
    public void onEnd(Player player) {
        Arena arena = this.plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena == null) {
            player.sendMessage(
                    StringUtils.translateAlternateColorCodes("&cYou are not in a arena!"));
            return;
        }

        arena.end();
    }
}
