package com.pepedevs.dbedwars.messaging;

import com.pepedevs.corelib.placeholders.PlaceholderUtil;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.messaging.member.ConsoleMember;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
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
        this.consoleMessagingMember.getAudienceMember().sendMessage(message.asComponent());
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
        return MiniMessageWrapper.getFullInstance().parse(message);
    }

    @Override
    public Component translateAlternateColorCodes(String message) {
        return legacySerializer.deserialize(message);
    }

    @Override
    public String setPlaceholders(final String message, PlaceholderEntry... entries) {
        String replaced = message;
        for (PlaceholderEntry entry : entries) {
            replaced = message.replace(entry.getPlaceholder(), entry.getReplacement().get());
        }
        return replaced;
    }

    @Override
    public String setPlaceholders(String message, Player player) {
        return PlaceholderUtil.placeholder(player, message);
    }

}
