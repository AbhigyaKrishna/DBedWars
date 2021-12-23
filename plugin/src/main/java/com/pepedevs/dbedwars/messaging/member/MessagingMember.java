package com.pepedevs.dbedwars.messaging.member;

import com.pepedevs.dbedwars.messaging.AbstractMessaging;
import com.pepedevs.dbedwars.messaging.Messaging;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

public abstract class MessagingMember extends AbstractMessaging implements com.pepedevs.dbedwars.api.messaging.member.MessagingMember {

    private final CommandSender sender;
    private final Audience audienceMember;

    private Collection<com.pepedevs.dbedwars.api.messaging.member.MessagingMember> members = Collections.singleton(this);

    protected MessagingMember(CommandSender sender) {
        this.audienceMember = Messaging.getInstance().getAdventure().sender(sender);
        this.sender = sender;
    }

    public static MessagingMember ofPlayer(Player player) {
        return Messaging.getInstance().getMessagingMember(player);
    }

    public static MessagingMember ofConsole() {
        return Messaging.getInstance().getConsole();
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Audience getAudienceMember() {
        return audienceMember;
    }

    @Override
    public Collection<com.pepedevs.dbedwars.api.messaging.member.MessagingMember> getMembers() {
        return this.members;
    }

}
