package org.zibble.dbedwars.action.actions;

import org.bukkit.Bukkit;

public class ConsoleCommandAction extends CommandAction {

    public ConsoleCommandAction(String command) {
        super(command, Bukkit.getConsoleSender());
    }

}
