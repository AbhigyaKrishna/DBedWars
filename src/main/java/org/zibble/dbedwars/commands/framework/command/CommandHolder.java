package org.zibble.dbedwars.commands.framework.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.configuration.language.PluginLang;

import java.util.HashSet;
import java.util.Set;

public class CommandHolder {

    private final String name;
    private final String[] aliases;
    private final Permission permission;
    private final AbstractCommandNode executor;
    private final PlayerOnly playerOnly;
    private final Set<CommandHolder> subCommands = new HashSet<>();

    public CommandHolder(String name, String[] aliases, Permission permission, PlayerOnly playerOnly, AbstractCommandNode executor) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.executor = executor;
        this.playerOnly = playerOnly;
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

    public Permission permission() {
        return this.permission;
    }

    public AbstractCommandNode executor() {
        return this.executor;
    }

    public PlayerOnly isPlayerOnly() {
        return this.playerOnly;
    }

    void execute(CommandSender sender, String[] args) {
        if (this.playerOnly != null && !(sender instanceof Player)) {
            Message message = this.playerOnly.consoleTry().length > 0 ? AdventureMessage.from(this.playerOnly.consoleTry()) : PluginLang.DEFAULT_CONSOLE_TRY.asMessage();
            Messaging.get().getConsole().sendMessage(message);
            return;
        }

        if (this.permission != null && !(sender.hasPermission(this.permission.value()) || (this.permission.forOp() && sender.isOp()))) {
            Message noPerm = this.permission.noPerm().length > 0 ? AdventureMessage.from(this.permission.noPerm()) : ConfigLang.NO_PERMISSION.asMessage();
            if (sender instanceof Player) {
                Messaging.get().getMessagingMember((Player) sender).sendMessage(noPerm);
            } else {
                Messaging.get().getConsole().sendMessage(noPerm);
            }
            return;
        }

        boolean shouldExecuteSub = true;
        if (this.executor != null) {
            shouldExecuteSub = this.executor.accept(sender, args);
        }

        if (!shouldExecuteSub) {
            return;
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
