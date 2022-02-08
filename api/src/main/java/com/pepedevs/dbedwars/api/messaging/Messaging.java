package com.pepedevs.dbedwars.api.messaging;

import com.pepedevs.dbedwars.api.messaging.member.ConsoleMember;
import com.pepedevs.dbedwars.api.messaging.member.PlayerMember;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

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

    public abstract String setPlaceholders(String message, PlaceholderEntry... entries);

    public abstract String setPlaceholders(String message, Player player);

}
