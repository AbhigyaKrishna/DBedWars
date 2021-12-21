package com.pepedevs.dbedwars.messaging;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MessagingMember {

    private final CommandSender sender;
    private final Audience audienceMember;

    private final MessagingHistory messagingHistory;

    protected MessagingMember(Audience audience, Player player) {
        this(audience, (CommandSender) player);
    }

    protected MessagingMember(Audience audience, ConsoleCommandSender console) {
        this(audience, (CommandSender) console);
    }

    private MessagingMember(Audience audience, CommandSender sender) {
        this.audienceMember = audience;
        this.sender = sender;
        //TODO TEMP
        this.messagingHistory = new MessagingHistory(1000, 100);
    }

    public static MessagingMember ofPlayer(Player player) {
        return MessagingServer.connect().getMessagingMember(player);
    }

    public static MessagingMember ofConsole() {
        return MessagingServer.connect().getConsole();
    }

    public SentMessage sendMessage(Message message, MessagingChannel channel) {
        return MessagingServer.connect().sendMessage(message, this, channel);
    }

    public Player getAsPlayer() {
        if (isPlayer()) return (Player) getSender();
        return null;
    }

    public ConsoleCommandSender getAsConsole() {
        if (isConsole()) return (ConsoleCommandSender) getSender();
        return null;
    }

    public CommandSender getSender() {
        return sender;
    }

    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Audience getAudienceMember() {
        return audienceMember;
    }

    public MessagingHistory getMessagingHistory() {
        return messagingHistory;
    }
}
