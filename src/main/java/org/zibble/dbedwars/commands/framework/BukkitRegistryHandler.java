package org.zibble.dbedwars.commands.framework;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BukkitRegistryHandler {

    private final DBedwars plugin;
    private final CommandRegistryImpl registry;

    private Constructor<PluginCommand> commandConstructor;
    private CommandMap commandMap;

    public BukkitRegistryHandler(CommandRegistryImpl registry, DBedwars plugin) {
        this.plugin = plugin;
        this.registry = registry;
        try {
            commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            commandConstructor.setAccessible(true);
            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void registerCommand(String name, String[] aliases, AbstractCommandNode commandNode) {
        try {
            PluginCommand command = commandConstructor.newInstance(name, this.plugin);
            command.setAliases(Arrays.asList(aliases));
            Pair<String, String[]> pair = this.registry.getPermissions().get(commandNode);
            if (pair == null) command.setExecutor(Generators.executor(this.registry, commandNode));
            else command.setExecutor(Generators.executor(this.registry, commandNode, pair));
            command.setTabCompleter(Generators.completer(this.registry, commandNode));
            commandMap.register(name, command);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(((SimpleCommandMap) this.commandMap).getCommands().stream().map(Command::getLabel).collect(Collectors.toList()));
    }

}
