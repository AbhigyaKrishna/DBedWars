package org.zibble.dbedwars.commands.framework;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.CommandRegistry;
import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandRegistryImpl implements CommandRegistry {

    private final BukkitRegistryHandler bukkitRegistryHandler;

    private final Map<Pair<String, String[]>, AbstractCommandNode> baseCommandNodes;
    private final Multimap<AbstractCommandNode, Pair<String, AbstractCommandNode>> subCommandNodes;
    private final Map<AbstractCommandNode, Pair<String, String[]>> permissions;

    public CommandRegistryImpl(DBedwars plugin) {

        this.baseCommandNodes = new ConcurrentHashMap<>();
        this.subCommandNodes = Multimaps.newMultimap(new HashMap<>(), HashSet::new);
        this.permissions = new ConcurrentHashMap<>();

        AutoRegistryHandler autoRegistryHandler = new AutoRegistryHandler(plugin);
        this.bukkitRegistryHandler = new BukkitRegistryHandler(this, plugin);

        this.baseCommandNodes.putAll(autoRegistryHandler.baseNodes());
        autoRegistryHandler.subNodes(this);
        this.permissions.putAll(autoRegistryHandler.permissions(this));
    }

    public void registerInBukkit() {
        this.baseCommandNodes.forEach((namePair, commandNode) -> this.bukkitRegistryHandler.registerCommand(namePair.getKey(), namePair.getValue(), commandNode));
    }

    public AbstractCommandNode getBaseNode(String node) {
        for (Map.Entry<Pair<String, String[]>, AbstractCommandNode> entry : this.baseCommandNodes.entrySet()) {
            if (entry.getKey().getKey().equalsIgnoreCase(node)) return entry.getValue();
            for (String s : entry.getKey().getValue()) {
                if (s.equalsIgnoreCase(node)) return entry.getValue();
            }
        }
        return null;
    }

    public AbstractCommandNode getChildNode(AbstractCommandNode parent, String node) {
        for (Map.Entry<AbstractCommandNode, Pair<String, AbstractCommandNode>> entry : this.subCommandNodes.entries()) {
            if (entry.getKey().equals(parent) && entry.getValue().getKey().equalsIgnoreCase(node)) return entry.getValue().getValue();
        }
        return null;
    }

    public Map<Pair<String, String[]>, AbstractCommandNode> getBaseCommandNodes() {
        return baseCommandNodes;
    }

    public Multimap<AbstractCommandNode, Pair<String, AbstractCommandNode>> getSubCommandNodes() {
        return subCommandNodes;
    }

    public Map<AbstractCommandNode, Pair<String, String[]>> getPermissions() {
        return permissions;
    }

    @Override
    public void addSubCommandNode(AbstractCommandNode node, Pair<String, AbstractCommandNode> pair) {
        this.subCommandNodes.put(node, pair);
    }

}
