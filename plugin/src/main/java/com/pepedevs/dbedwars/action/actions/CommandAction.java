package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class CommandAction implements Action<CommandSender> {

    private final String command;

    public CommandAction(String command) {
        this.command = command;
    }

    @Override
    public void execute(CommandSender commandSender) {
        Bukkit.dispatchCommand(commandSender, command);
    }

    public String getCommand() {
        return command;
    }
}
