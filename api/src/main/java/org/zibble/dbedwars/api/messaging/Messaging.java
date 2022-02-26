package org.zibble.dbedwars.api.messaging;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.member.ConsoleMember;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;

public abstract class Messaging {

    private static Messaging instance;

    protected Messaging() {
        instance = this;
    }

    public static Messaging get() {
        return instance;
    }

    public abstract ConsoleMember getConsole();

    public abstract PlayerMember getMessagingMember(Player player);

    public abstract String serializeMini(Component component);

    public abstract Component parseMini(String message);

    public abstract Component translateAlternateColorCodes(String message);

    public abstract String setPlaceholders(final String message, Placeholder... entries);

    public abstract String setPlaceholders(final String message, final Player player, Placeholder... entries);

    public abstract String setRegisteredPlaceholders(final String message, Player player);

}
