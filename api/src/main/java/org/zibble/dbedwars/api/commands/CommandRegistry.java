package org.zibble.dbedwars.api.commands;

import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;

public interface CommandRegistry {

    void registerPackage(String packageName);

    void registerClass(Class<?> clazz);

    void register(AbstractCommandNode node);

}
