package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.api.messaging.Placeholder;
import com.pepedevs.dbedwars.api.messaging.PlayerPlaceholderEntry;
import com.pepedevs.radium.placeholders.PlaceholderUtil;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.messaging.member.ConsoleMember;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.Validate;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Messaging extends com.pepedevs.dbedwars.api.messaging.Messaging {

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
    public String setPapiPlaceholders(final String message, Player player) {
        return PlaceholderUtil.placeholder(player, message);
    }

}
