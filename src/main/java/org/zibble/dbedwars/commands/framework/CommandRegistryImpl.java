package org.zibble.dbedwars.commands.framework;

import com.google.common.collect.ImmutableMap;
import org.reflections.Reflections;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.commands.CommandRegistry;
import org.zibble.dbedwars.api.commands.annotations.ParentCommandNode;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.commands.framework.command.CommandHolder;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ConstructorWrapper;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CommandRegistryImpl implements CommandRegistry {

    private final BukkitRegistryHandler bukkitRegistryHandler;
    private final Map<Class<?>, Object> availableParameters;
    private final Set<CommandHolder> baseCommands;

    public CommandRegistryImpl(DBedwars plugin) {
        this.baseCommands = new HashSet<>();
        this.bukkitRegistryHandler = new BukkitRegistryHandler(plugin);
        this.availableParameters = ImmutableMap.<Class<?>, Object>builder()
                .put(DBedwars.class, plugin)
                .put(DBedWarsAPI.class, DBedWarsAPI.getApi())
                .put(Messaging.class, Messaging.get())
                .build();
    }

    public void registerBaseNode(String name, String[] aliases, Permission permission, PlayerOnly playerOnly, AbstractCommandNode node) {
        CommandHolder commandHolder = new CommandHolder(name, aliases, permission, playerOnly, node);
        this.baseCommands.add(commandHolder);
        this.bukkitRegistryHandler.registerCommand(commandHolder);
    }

    public CommandHolder getBaseHolder(String name) {
        for (CommandHolder holder : this.baseCommands) {
            if (holder.matches(name))
                return holder;
        }
        return null;
    }

    public CommandHolder getNode(String path) {
        String[] node = path.split("\\.");
        CommandHolder holder = this.getBaseHolder(node[0]);
        for (int i = 1; i < node.length; i++) {
            if (holder == null)
                return null;
            holder = holder.subCommand(node[i]);
        }
        return holder;
    }

    @Override
    public void registerPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> baseNodes = new HashSet<>(reflections.getTypesAnnotatedWith(ParentCommandNode.class));
        for (Class<?> baseNode : baseNodes) {
            this.registerClass(baseNode);
        }

        TreeSet<Class<?>> subNodes = new TreeSet<>((a, b) -> {
            SubCommandNode o1 = a.getAnnotation(SubCommandNode.class);
            SubCommandNode o2 = b.getAnnotation(SubCommandNode.class);
            if (o1 == null || o2 == null) throw new IllegalArgumentException("Classes must be annotated with @SubCommandNode");
            String[] aRoute = o1.parent().split("\\.");
            String[] bRoute = o2.parent().split("\\.");
            return Integer.compare(aRoute.length, bRoute.length);
        });

        subNodes.addAll(reflections.getTypesAnnotatedWith(SubCommandNode.class));

        for (Class<?> subNode : subNodes) {
            this.registerClass(subNode);
        }
    }

    @Override
    public void registerClass(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ParentCommandNode.class)) {
            AbstractCommandNode node = this.createObject(clazz);
            if (node == null)
                throw new IllegalArgumentException("No constructor found for class " + clazz.getName());
            this.registerBaseNodes(node);
        } else if (clazz.isAnnotationPresent(SubCommandNode.class)) {
            AbstractCommandNode node = this.createObject(clazz);
            if (node == null)
                throw new IllegalArgumentException("No constructor found for class " + clazz.getName());
            this.registerSubNode(node);
        } else {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not a valid command node");
        }
    }

    @Override
    public void register(AbstractCommandNode node) {
        if (node.getClass().isAnnotationPresent(ParentCommandNode.class)) {
            this.registerBaseNodes(node);
        } else if (node.getClass().isAnnotationPresent(SubCommandNode.class)) {
            this.registerSubNode(node);
        } else {
            throw new IllegalArgumentException("Class " + node.getClass().getName() + " is not a valid command node");
        }
    }

    private void registerBaseNodes(AbstractCommandNode node) {
        Class<?> clazz = node.getClass();
        ParentCommandNode parent = clazz.getAnnotation(ParentCommandNode.class);
        Permission permission = clazz.getAnnotation(Permission.class);
        PlayerOnly playerOnly = clazz.getAnnotation(PlayerOnly.class);
        this.registerBaseNode(parent.value(), parent.aliases(), permission, playerOnly, node);
    }

    private void registerSubNode(AbstractCommandNode node) {
        Class<?> clazz = node.getClass();
        SubCommandNode subCommandNode = clazz.getAnnotation(SubCommandNode.class);
        CommandHolder currentParent = this.getNode(subCommandNode.parent());
        if (currentParent == null)
            throw new IllegalArgumentException("Invalid path at for node: " + subCommandNode.parent());
        Permission permission = clazz.getAnnotation(Permission.class);
        PlayerOnly playerOnly = clazz.getAnnotation(PlayerOnly.class);
        CommandHolder sub = new CommandHolder(subCommandNode.value(), subCommandNode.aliases(), permission, playerOnly, node);
        currentParent.addSubCommand(sub);
    }

    private AbstractCommandNode createObject(Class<?> clazz) {
        constructor: for (Constructor<?> constructor : clazz.getConstructors()) {
            ConstructorWrapper<?> wrapper = new ConstructorWrapper<>(constructor);
            Class<?>[] parameterTypes = wrapper.getParameterTypes();
            for (Class<?> type : parameterTypes) {
                if (!this.availableParameters.containsKey(type)) continue constructor;
            }
            Object[] parameters = new Object[parameterTypes.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = this.availableParameters.get(parameterTypes[i]);
            }
            return  (AbstractCommandNode) wrapper.newInstance(parameters);
        }

        return null;
    }

}
