package org.zibble.dbedwars.commands.framework;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.commands.framework.command.BukkitCommand;
import org.zibble.dbedwars.commands.framework.command.CommandHolder;
import org.zibble.dbedwars.utils.reflection.annotation.ConstructorRef;
import org.zibble.dbedwars.utils.reflection.annotation.ReflectionAnnotations;
import org.zibble.dbedwars.utils.reflection.resolver.FieldResolver;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ConstructorWrapper;

import java.util.Arrays;

public class BukkitRegistryHandler {

    @ConstructorRef(clazz = PluginCommand.class, parameters = {String.class, Plugin.class})
    private static final ConstructorWrapper<PluginCommand> PLUGIN_COMMAND_CONSTRUCTOR = null;

    private final DBedwars plugin;

    private final CommandMap commandMap;

    public BukkitRegistryHandler(DBedwars plugin) {
        this.plugin = plugin;
        ReflectionAnnotations.INSTANCE.load(BukkitRegistryHandler.class, this);
        this.commandMap = new FieldResolver(Bukkit.getPluginManager().getClass()).resolveWrapper("commandMap").get(Bukkit.getPluginManager());
    }

    public void registerCommand(CommandHolder holder) {
        PluginCommand command = PLUGIN_COMMAND_CONSTRUCTOR.newInstance(holder.name(), this.plugin);
        command.setAliases(Arrays.asList(holder.aliases()));
        BukkitCommand bukkitCommand = new BukkitCommand(holder);
        command.setExecutor(bukkitCommand);
        command.setTabCompleter(bukkitCommand);
        this.commandMap.register(holder.name(), command);
    }

}
