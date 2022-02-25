package org.zibble.dbedwars.game.arena;

import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.messaging.message.Message;

public class ArenaCategoryImpl {

    private final String name;
    private final Message displayName;

    public ArenaCategoryImpl(String name, Message displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public Message getDisplayName() {
        return displayName;
    }

    public Arena[] getArenas() {
        // TODO: Implement
        return new Arena[0];
    }

}
