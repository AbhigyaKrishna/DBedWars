package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.messaging.MessageParser;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableMessaging;
import com.pepedevs.dbedwars.messaging.parser.ClassicParser;
import com.pepedevs.dbedwars.messaging.parser.MiniMessageParser;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import org.apache.commons.lang.Validate;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MessagingServer {

    private static MessagingServer server;
    private final DBedwars plugin;
    private BukkitAudiences adventure;
    private Set<MessagingMember> registeredMembers;
    private Set<MessagingChannel> registeredChannels;
    private MessagingMember consoleMessagingMember;
    private MessagingChannel consoleLogger;

    //TODO DO IN FILES
    private ConfigurableMessaging.ConfigurableHistory history;
    private MessageParser parser = new MiniMessageParser(
            MiniMessage.builder()
                    .removeDefaultTransformations()
                    .transformations(TransformationType.COLOR,
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
                    .build()
    );

    private MiniMessageParser miniParser = new MiniMessageParser(MiniMessage.builder()
            .removeDefaultTransformations()
            .transformations(TransformationType.COLOR,
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

    private ClassicParser classicParser = new ClassicParser();

    public MessagingServer(DBedwars plugin) {
        server = this;

        if (plugin == null) throw new IllegalArgumentException("plugin cannot be null");

        this.plugin = plugin;
    }

    protected static MessagingServer connect() {
        return server;
    }

    public void start(ConsoleCommandSender console) {
        this.adventure = BukkitAudiences.builder(plugin).build();
        this.registeredChannels = new HashSet<>();
        this.registeredMembers = new HashSet<>();

        this.consoleMessagingMember = new MessagingMember(adventure.sender(console), console);
        this.consoleLogger = new MessagingChannel();
        this.consoleLogger.addConsole();
    }

    protected SentMessage sendMessage(
            Message message, MessagingMember sender, MessagingChannel channel) {

        Validate.notNull(message, "message cannot be null");
        Validate.notNull(sender, "sender cannot be null");
        Validate.notNull(channel, "channel cannot be null");

        if (!registryCheck(channel))
            throw new IllegalStateException("cannot send message in an unregistered channel");

        Set<MessagingMember> receivers = channel.getChannelMemebers();

        SentMessage sentMessage = new SentMessage(message, channel, sender);
        sender.getMessagingHistory().addSentMessage(sentMessage);
        channel.getMessagingHistory().addSentMessage(sentMessage);

        for (MessagingMember receiver : receivers) {
            receiver.getAudienceMember().sendMessage(message.asComponent());
            receiver.getMessagingHistory().addSentMessage(sentMessage);
        }

        return sentMessage;
    }

    protected SentMessage sendToExcept(
            Message message,
            MessagingMember sender,
            MessagingChannel channel,
            MessagingMember... hiddenUsers) {

        Validate.notNull(message, "message cannot be null");
        Validate.notNull(sender, "sender cannot be null");
        Validate.notNull(channel, "channel cannot be null");

        if (!registryCheck(channel))
            throw new IllegalStateException("cannot send message in an unregistered channel");

        Set<MessagingMember> receivers = channel.getChannelMemebers();
        Arrays.asList(hiddenUsers).forEach(receivers::remove);

        SentMessage sentMessage =
                new SentMessage(message, channel, sender, System.currentTimeMillis(), receivers);
        sender.getMessagingHistory().addSentMessage(sentMessage);
        channel.getMessagingHistory().addSentMessage(sentMessage);

        for (MessagingMember receiver : receivers) {
            receiver.getAudienceMember().sendMessage(message.asComponent());
            receiver.getMessagingHistory().addSentMessage(sentMessage);
        }

        return sentMessage;
    }

    protected SentMessage sendToConsole(Message message) {

        Validate.notNull(message, "message cannot be null");

        SentMessage sentMessage = new SentMessage(message, consoleLogger, consoleMessagingMember);

        this.consoleMessagingMember.getAudienceMember().sendMessage(message.asComponent());
        this.consoleMessagingMember.getMessagingHistory().addSentMessage(sentMessage);
        this.consoleLogger.getMessagingHistory().addSentMessage(sentMessage);

        return sentMessage;
    }

    protected void registerChannels(MessagingChannel... channels) {
        this.registeredChannels.addAll(Arrays.asList(channels));
    }

    protected void unRegisterChannels(MessagingChannel... channels) {
        for (MessagingChannel channel : channels) {
            this.registeredChannels.remove(channel);
        }
    }

    protected Set<MessagingChannel> getRegisteredChannels() {
        return registeredChannels;
    }

    protected MessagingMember getMessagingMember(Player player) {
        for (MessagingMember member : this.registeredMembers) {
            if (!member.isPlayer()) continue;
            if (member.getAsPlayer().equals(player)) return member;
        }

        MessagingMember member = new MessagingMember(adventure.player(player), player);
        this.registeredMembers.add(member);
        return member;
    }

    protected MessagingMember getConsole() {
        return this.consoleMessagingMember;
    }

    protected boolean registryCheck(MessagingChannel channel) {
        return this.getRegisteredChannels().contains(channel);
    }

    protected ConfigurableMessaging.ConfigurableHistory getHistory() {
        return this.history;
    }

    protected MessageParser getParser() {
        return parser;
    }

    public MessageParser getMiniParser() {
        return miniParser;
    }

    public ClassicParser getClassicParser() {
        return classicParser;
    }
}
