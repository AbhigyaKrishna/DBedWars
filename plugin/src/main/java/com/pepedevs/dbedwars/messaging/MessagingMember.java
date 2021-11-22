package com.pepedevs.dbedwars.messaging;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public abstract class MessagingMember {

    private CommandSender sender;
    private Audience audienceMember;

    protected MessagingHistory messagingHistory;

    protected MessagingMember() {

    }

    public SentMessage sendMessage(Message message, MessagingChannel channel){
        return MessagingServer.connect().sendMessage(message, this, channel);
    }

    @Nullable
    public Player getAsPlayer() {
        if (isPlayer())
            return (Player) getSender();
        return null;
    }

    @Nullable
    public ConsoleCommandSender getAsConsole() {
        if (isConsole())
            return (ConsoleCommandSender) getSender();
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
