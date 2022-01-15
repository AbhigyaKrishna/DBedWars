package com.pepedevs.dbedwars.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.pepedevs.corelib.task.Workload;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.configuration.Lang;
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
            Messaging.get().getMessagingMember(player).sendMessage(Lang.NO_ARENA_FOUND_W_NAME.asMessage());
            return;
        }

        Arena arena = this.plugin.getGameManager().getArena(arenaName);
        this.plugin.getThreadHandler().submitAsync(new Workload() {
            @Override
            public void compute() {
                if (arena.getWorld() == null)
                    arena.load();
                BedwarsCommand.this.plugin.getThreadHandler().submitSync(new Runnable() {
                    @Override
                    public void run() {
                                arena.addPlayer(player);
                            }
                });
            }
        });
    }

    @Subcommand("end")
    public void onEnd(Player player) {
        Arena arena = this.plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena == null) {
            Messaging.get().getMessagingMember(player).sendMessage(Lang.NOT_IN_AN_ARENA.asMessage());
            return;
        }

        arena.end();
    }

    @Subcommand("test")
    public void test(Player player, String type) {
        Messaging.get().getMessagingMember(player).sendMessage(Lang.valueOf(type).asMessage());
    }
}
