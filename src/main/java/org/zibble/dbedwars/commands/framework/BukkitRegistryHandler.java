package org.zibble.dbedwars.commands.framework;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.utils.reflection.annotation.Constructor;
import org.zibble.dbedwars.utils.reflection.annotation.ReflectionAnnotations;
import org.zibble.dbedwars.utils.reflection.resolver.FieldResolver;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ConstructorWrapper;

import java.util.Arrays;

public class BukkitRegistryHandler {

    @Constructor(clazz = PluginCommand.class, parameters = {String.class, Plugin.class})
    private final ConstructorWrapper<PluginCommand> PLUGIN_COMMAND_CONSTRUCTOR = null;

    private final DBedwars plugin;
    private final CommandRegistryImpl registry;

    private final CommandMap commandMap;

    public BukkitRegistryHandler(CommandRegistryImpl registry, DBedwars plugin) {
        this.plugin = plugin;
        this.registry = registry;
        ReflectionAnnotations.INSTANCE.load(BukkitRegistryHandler.class, this);
        commandMap = new FieldResolver(Bukkit.getPluginManager().getClass()).resolveAccessor("commandMap").get(Bukkit.getPluginManager());
    }

    public void registerCommand(String name, String[] aliases, AbstractCommandNode commandNode) {
        PluginCommand command = PLUGIN_COMMAND_CONSTRUCTOR.newInstance(name, this.plugin);
        command.setAliases(Arrays.asList(aliases));
        Pair<String, String[]> pair = this.registry.getPermissions().get(commandNode);
        if (pair == null) command.setExecutor(Generators.executor(this.registry, commandNode));
        else command.setExecutor(Generators.executor(this.registry, commandNode, pair));
        command.setTabCompleter(Generators.completer(this.registry, commandNode));
        commandMap.register(name, command);
    }

}
