package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.messaging.MessageParser;
import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.messaging.member.ConsoleMember;
import com.pepedevs.dbedwars.messaging.member.MessagingMember;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;
import com.pepedevs.dbedwars.messaging.parser.LegacyParser;
import com.pepedevs.dbedwars.messaging.parser.MiniMessageParser;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import org.apache.commons.lang.Validate;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MessagingServer extends Messaging {

    private static MessagingServer server;
    private final DBedwars plugin;
    private BukkitAudiences adventure;
    private Set<MessagingMember> registeredMembers;
    private Set<MessagingChannel> registeredChannels;
    private MessagingMember consoleMessagingMember;
    private MessagingChannel consoleLogger;
    private MiniMessage miniMessage = MiniMessage.builder()
            .removeDefaultTransformations()
            .transformations(
                    TransformationType.COLOR,
                    TransformationType.DECORATION,
                    TransformationType.HOVER_EVENT,
                    TransformationType.CLICK_EVENT,
                    TransformationType.KEYBIND,
                    TransformationType.TRANSLATABLE,
                    TransformationType.INSERTION,
                    TransformationType.FONT,
                    TransformationType.GRADIENT,
                    TransformationType.RAINBOW,
                    TransformationType.RESET,
                    TransformationType.PRE)
            .markdownFlavor(DiscordFlavor.get())
            .build();

    // TODO DO IN FILES
    private MessageParser parser =
            new MiniMessageParser(
                    MiniMessage.builder()
                            .removeDefaultTransformations()
                            .transformations(
                                    TransformationType.COLOR,
                                    TransformationType.DECORATION,
                                    TransformationType.HOVER_EVENT,
                                    TransformationType.CLICK_EVENT,
                                    TransformationType.KEYBIND,
                                    TransformationType.TRANSLATABLE,
                                    TransformationType.INSERTION,
                                    TransformationType.FONT,
                                    TransformationType.GRADIENT,
                                    TransformationType.RAINBOW,
                                    TransformationType.RESET,
                                    TransformationType.PRE)
                            .markdownFlavor(DiscordFlavor.get())
                            .build());

    private MiniMessageParser miniParser = new MiniMessageParser(miniMessage);

    private LegacyParser classicParser = new LegacyParser();

    public MessagingServer(DBedwars plugin) {
        server = this;

        if (plugin == null) throw new IllegalArgumentException("plugin cannot be null");

        this.plugin = plugin;
    }

    public static MessagingServer connect() {
        return server;
    }

    public void start(ConsoleCommandSender console) {
        this.adventure = BukkitAudiences.builder(plugin).build();
        this.registeredChannels = new HashSet<>();
        this.registeredMembers = new HashSet<>();

        this.consoleMessagingMember = new ConsoleMember(console);
        this.consoleLogger = new MessagingChannel(EnumChannel.CONSOLE);
        this.consoleLogger.addConsole();
    }

    public void sendMessage(
            Message message, MessagingMember sender, MessagingChannel channel) {

        Validate.notNull(message, "message cannot be null");
        Validate.notNull(sender, "sender cannot be null");
        Validate.notNull(channel, "channel cannot be null");

        if (!registryCheck(channel))
            throw new IllegalStateException("cannot send message in an unregistered channel");

        Set<MessagingMember> receivers = channel.getChannelMembers();

        for (MessagingMember receiver : receivers) {
            receiver.getAudienceMember().sendMessage(message.asComponent());
        }
    }

    public void sendToExcept(
            Message message,
            MessagingMember sender,
            MessagingChannel channel,
            MessagingMember... hiddenUsers) {

        Validate.notNull(message, "message cannot be null");
        Validate.notNull(sender, "sender cannot be null");
        Validate.notNull(channel, "channel cannot be null");

        if (!registryCheck(channel))
            throw new IllegalStateException("cannot send message in an unregistered channel");

        Set<MessagingMember> receivers = channel.getChannelMembers();
        Arrays.asList(hiddenUsers).forEach(receivers::remove);

        for (MessagingMember receiver : receivers) {
            receiver.getAudienceMember().sendMessage(message.asComponent());
        }

    }

    public void sendToConsole(Message message) {
        Validate.notNull(message, "message cannot be null");
        this.consoleMessagingMember.getAudienceMember().sendMessage(message.asComponent());
    }

    public void registerChannels(MessagingChannel... channels) {
        this.registeredChannels.addAll(Arrays.asList(channels));
    }

    public void unRegisterChannels(MessagingChannel... channels) {
        for (MessagingChannel channel : channels) {
            this.registeredChannels.remove(channel);
        }
    }

    public Set<MessagingChannel> getRegisteredChannels() {
        return registeredChannels;
    }

    public MessagingMember getMessagingMember(Player player) {
        for (MessagingMember member : this.registeredMembers) {
            if (!member.isPlayerMember()) continue;
            if (((PlayerMember) member).getPlayer().getUniqueId().equals(player.getUniqueId())) return member;
        }

        MessagingMember member = new PlayerMember(player);
        this.registeredMembers.add(member);
        return member;
    }

    public MessagingMember getConsole() {
        return this.consoleMessagingMember;
    }

    public boolean registryCheck(MessagingChannel channel) {
        return this.getRegisteredChannels().contains(channel);
    }

    public BukkitAudiences getAdventure() {
        return this.adventure;
    }

    public MessageParser getParser() {
        return parser;
    }

    public MessageParser getMiniParser() {
        return miniParser;
    }

    public LegacyParser getClassicParser() {
        return classicParser;
    }

    @Override
    public String serialize(Component component) {
        return this.miniMessage.serialize(component);
    }

    @Override
    public Component deserialize(String message) {
        return this.miniMessage.parse(message);
    }

}
