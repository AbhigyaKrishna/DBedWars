package org.zibble.dbedwars.script.action.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.script.action.Action;

public class CommandAction implements Action {

    private final CommandSender sender;
    private final String command;

    public CommandAction(String command, CommandSender sender) {
        this.sender = sender;
        this.command = command;
    }

    @Override
    public void execute() {
        Bukkit.dispatchCommand(this.getSender(), command);
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public String getCommand() {
        return command;
    }

}
