package com.pepedevs.dbedwars.messaging;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class MessagingMember {

    private final CommandSender sender;
    private final Audience audienceMember;

    protected MessagingHistory messagingHistory;

    public static MessagingMember ofPlayer(Player player) {
        return MessagingServer.connect().getMessagingMember(player);
    }

    public static MessagingMember ofConsole() {
        return MessagingServer.connect().getConsole();
    }

    protected MessagingMember(Audience audience, Player player) {
        this.audienceMember = audience;
        this.sender = player;
    }

    protected MessagingMember(Audience audience, ConsoleCommandSender console) {
        this.audienceMember = audience;
        this.sender = console;
    }

    public SentMessage sendMessage(Message message, MessagingChannel channel) {
        return MessagingServer.connect().sendMessage(message, this, channel);
    }

    @Nullable
    public Player getAsPlayer() {
        if (isPlayer()) return (Player) getSender();
        return null;
    }

    @Nullable
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
