package org.zibble.dbedwars.commands.framework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.utils.Debugger;

import java.util.Arrays;
import java.util.List;

public class BukkitCommand implements CommandExecutor, TabCompleter {

    private final CommandHolder holder;

    public BukkitCommand(CommandHolder holder) {
        this.holder = holder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Debugger.debug("Running command `" + command.getName() + "` with args " + Arrays.toString(args));
        this.holder.execute(sender, args);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // TODO: 30-03-2022
        return null;
    }

}
