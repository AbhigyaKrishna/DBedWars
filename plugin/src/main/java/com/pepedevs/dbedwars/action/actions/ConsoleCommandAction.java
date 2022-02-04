package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import org.bukkit.Bukkit;

public class ConsoleCommandAction implements Action<Void> {

    private final String command;

    public ConsoleCommandAction(String command) {
        this.command = command;
    }

    @Override
    public void execute(Void unused) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command);
    }

    public String getCommand() {
        return command;
    }
}
