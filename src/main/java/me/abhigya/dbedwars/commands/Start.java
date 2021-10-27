package me.abhigya.dbedwars.commands;

import me.Abhigya.core.commands.CommandArgument;
import me.Abhigya.core.util.StringUtils;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Start implements CommandArgument {

    private DBedwars plugin;

    public Start(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (args.length < 1) return false;

        if (!this.plugin.getGameManager().containsArena(args[0])
                || !this.plugin.getGameManager().getArena(args[0]).isEnabled()) {
            player.sendMessage(
                    StringUtils.translateAlternateColorCodes("&cNo arena found with this name!"));
            return true;
        }

        Arena arena = this.plugin.getGameManager().getArena(args[0]);
        this.plugin
                .getThreadHandler()
                .addAsyncWork(
                        () -> {
                            if (arena.getWorld() == null) {
                                arena.load();
                            }
                            this.plugin
                                    .getThreadHandler()
                                    .addSyncWork(() -> arena.addPlayer(player));
                        });
        return true;
    }

    @Override
    public List<String> tab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
