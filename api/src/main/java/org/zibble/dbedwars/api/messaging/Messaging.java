package org.zibble.dbedwars.api.messaging;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.member.ConsoleMember;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;

public abstract class Messaging {

    private static Messaging instance;

    protected Messaging() {
        instance = this;
    }

    public static Messaging get() {
        return instance;
    }

    public abstract MessagingMember getMemberOf(CommandSender sender);

    public abstract ConsoleMember getConsole();

    public abstract PlayerMember getMessagingMember(Player player);

    public abstract String serializeMini(Component component);

    public abstract Component parseMini(String message);

    public abstract Component translateAlternateColorCodes(String message);

    public abstract Message asConfigMessage(String message);

    public abstract String setPlaceholders(final String message, Placeholder... entries);

    public abstract String setPlaceholders(final String message, final Player player, Placeholder... entries);

    public abstract String setRegisteredPlaceholders(final String message, Player player);

}
