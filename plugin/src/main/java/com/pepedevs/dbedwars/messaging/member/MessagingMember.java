package com.pepedevs.dbedwars.messaging.member;

import com.pepedevs.dbedwars.messaging.EnumChannel;
import com.pepedevs.dbedwars.messaging.Message;
import com.pepedevs.dbedwars.messaging.MessagingChannel;
import com.pepedevs.dbedwars.messaging.MessagingServer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MessagingMember
        implements com.pepedevs.dbedwars.api.messaging.member.MessagingMember {

    private final CommandSender sender;
    private final Audience audienceMember;

    private final MessagingChannel messagingChannel;

    protected MessagingMember(CommandSender sender) {
        this.audienceMember = MessagingServer.connect().getAdventure().sender(sender);
        this.sender = sender;
        // TODO TEMP
        this.messagingChannel = new MessagingChannel(EnumChannel.PERSONAL);
    }

    public static MessagingMember ofPlayer(Player player) {
        return MessagingServer.connect().getMessagingMember(player);
    }

    public static MessagingMember ofConsole() {
        return MessagingServer.connect().getConsole();
    }

    public void sendMessage(Message message, MessagingChannel channel) {
        MessagingServer.connect().sendMessage(message, this, channel);
    }

    public void sendMessage(Message message) {
        MessagingServer.connect()
                .sendMessage(message, MessagingMember.ofConsole(), this.messagingChannel);
    }

    public CommandSender getSender() {
        return sender;
    }

    public Audience getAudienceMember() {
        return audienceMember;
    }
}
