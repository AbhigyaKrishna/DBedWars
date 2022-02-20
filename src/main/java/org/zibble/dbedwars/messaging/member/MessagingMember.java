package org.zibble.dbedwars.messaging.member;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.messaging.AbstractMessaging;
import org.zibble.dbedwars.messaging.Messaging;

import java.util.Collection;
import java.util.Collections;

public abstract class MessagingMember extends AbstractMessaging implements org.zibble.dbedwars.api.messaging.member.MessagingMember {

    private final CommandSender sender;
    private final Audience audienceMember;

    private Collection<org.zibble.dbedwars.api.messaging.member.MessagingMember> members = Collections.singleton(this);

    protected MessagingMember(CommandSender sender) {
        this.audienceMember = Messaging.getInstance().getAdventure().sender(sender);
        this.sender = sender;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Audience getAudienceMember() {
        return audienceMember;
    }

    @Override
    public Collection<org.zibble.dbedwars.api.messaging.member.MessagingMember> getMembers() {
        return this.members;
    }

}
