package org.zibble.dbedwars.commands.framework.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.configuration.language.ConfigLang;

import java.util.HashSet;
import java.util.Set;

public class CommandHolder {

    private final String name;
    private final String[] aliases;
    private final String permission;
    private final AbstractCommandNode executor;
    private final boolean playerOnly;
    private final String consoleTry;
    private final Set<CommandHolder> subCommands = new HashSet<>();

    public CommandHolder(String name, String[] aliases, String permission, boolean playerOnly, String consoleTry, AbstractCommandNode executor) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.executor = executor;
        this.playerOnly = playerOnly;
        this.consoleTry = consoleTry;
    }

    public boolean matches(String name) {
        return this.name.equalsIgnoreCase(name) || this.anyMatch(name, this.aliases);
    }

    public String name() {
        return this.name;
    }

    public String[] aliases() {
        return this.aliases;
    }

    public String permission() {
        return this.permission;
    }

    public AbstractCommandNode executor() {
        return this.executor;
    }

    public boolean isPlayerOnly() {
        return this.playerOnly;
    }

    public String consoleTry() {
        return this.consoleTry;
    }

    void execute(CommandSender sender, String[] args) {
        if (this.playerOnly && !(sender instanceof Player)) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from(this.consoleTry));
            return;
        }

        if (this.permission != null && !sender.hasPermission(this.permission)) {
            if (sender instanceof Player) {
                Messaging.get().getMessagingMember((Player) sender).sendMessage(ConfigLang.NO_PERMISSION.asMessage());
            } else {
                Messaging.get().getConsole().sendMessage(ConfigLang.NO_PERMISSION.asMessage());
            }
            return;
        }

        if (this.executor != null) {
            this.executor.accept(sender, args);
        }

        if (args.length > 0) {
            CommandHolder sub = this.subCommand(args[0]);
            if (sub != null) {
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                sub.execute(sender, newArgs);
            }
        }
    }

    public CommandHolder subCommand(String name) {
        for (CommandHolder subCommand : this.subCommands) {
            if (subCommand.matches(name)) {
                return subCommand;
            }
        }
        return null;
    }

    public void addSubCommand(CommandHolder command) {
        this.subCommands.add(command);
    }

    boolean anyMatch(String name, String[] aliases) {
        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}
