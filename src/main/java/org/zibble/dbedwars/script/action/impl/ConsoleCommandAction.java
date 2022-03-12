package org.zibble.dbedwars.script.action.impl;

import org.bukkit.Bukkit;

public class ConsoleCommandAction extends CommandAction {

    public ConsoleCommandAction(String command) {
        super(command, Bukkit.getConsoleSender());
    }

}
