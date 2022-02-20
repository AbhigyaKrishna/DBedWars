package org.zibble.dbedwars.action.actions;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.action.Action;

public class CommandAction implements Action<CommandSender> {

    private final CommandSender sender;
    private final String command;

    public CommandAction(String command, CommandSender sender) {
        this.sender = sender;
        this.command = command;
    }

    @Override
    public void execute() {
        Bukkit.dispatchCommand(this.getHandle(), command);
    }

    @Override
    public CommandSender getHandle() {
        return this.sender;
    }

    public String getCommand() {
        return command;
    }

}
