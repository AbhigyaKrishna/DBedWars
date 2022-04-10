package org.zibble.dbedwars.messaging;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.Validate;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.placeholders.PlayerPlaceholderEntry;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.messaging.member.ConsoleMember;
import org.zibble.dbedwars.messaging.member.PlayerMember;

public class Messaging extends org.zibble.dbedwars.api.messaging.Messaging {

    private static Messaging server;
    private final DBedwars plugin;
    private BukkitAudiences adventure;
    private ConsoleMember consoleMessagingMember;
    private LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.builder().character('&').build();

    public Messaging(DBedwars plugin) {
        Validate.notNull(plugin, "plugin cannot be null");
        this.plugin = plugin;
        server = this;
    }

    public static Messaging getInstance() {
        return server;
    }

    public void init(ConsoleCommandSender console) {
        this.adventure = BukkitAudiences.builder(plugin).build();
        this.consoleMessagingMember = new ConsoleMember(console);
    }

    public void sendToConsole(Message message) {
        Validate.notNull(message, "message cannot be null");
        for (Component component : message.asComponent()) {
            this.consoleMessagingMember.getAudienceMember().sendMessage(component);
        }
    }

    @Override
    public PlayerMember getMessagingMember(Player player) {
        return new PlayerMember(player);
    }

    public BukkitAudiences getAdventure() {
        return this.adventure;
    }

    @Override
    public ConsoleMember getConsole() {
        return this.consoleMessagingMember;
    }

    @Override
    public String serializeMini(Component component) {
        return MiniMessageWrapper.getFullInstance().serialize(component);
    }

    @Override
    public Component parseMini(String message) {
        return MiniMessageWrapper.getFullInstance().deserialize(message);
    }

    @Override
    public Component translateAlternateColorCodes(String message) {
        return legacySerializer.deserialize(message);
    }

    public LegacyComponentSerializer getLegacySerializer() {
        return legacySerializer;
    }

    @Override
    public Message asConfigMessage(String message) {
        return ConfigMessage.from(message);
    }

    @Override
    public String setPlaceholders(final String message, Placeholder... entries) {
        String replaced = message;
        for (Placeholder entry : entries) {
            if (entry instanceof PlaceholderEntry) {
                replaced = ((PlaceholderEntry) entry).apply(replaced);
            }
        }
        return replaced;
    }

    @Override
    public String setPlaceholders(final String message, final Player player, Placeholder... entries) {
        String replaced = message;
        for (Placeholder entry : entries) {
            if (entry instanceof PlaceholderEntry) {
                replaced = ((PlaceholderEntry) entry).apply(replaced);
            } else if (entry instanceof PlayerPlaceholderEntry) {
                replaced = ((PlayerPlaceholderEntry) entry).apply(replaced, player);
            }
        }
        return replaced;
    }

    @Override
    public String setRegisteredPlaceholders(final String message, Player player) {
        return this.plugin.getHookManager().getPlaceholderHook().setPlaceholders(player, message);
    }

}
