package org.zibble.dbedwars.api.commands;

import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.util.Pair;

public interface CommandRegistry {
    
    void addSubCommandNode(AbstractCommandNode node, Pair<String, AbstractCommandNode> pair);

}
