package org.zibble.dbedwars.messaging.member;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.messaging.AbstractMessaging;
import org.zibble.dbedwars.messaging.Messaging;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public abstract class MessagingMember extends AbstractMessaging implements org.zibble.dbedwars.api.messaging.member.MessagingMember {

    private final CommandSender sender;
    private final Audience audienceMember;

    private Collection<org.zibble.dbedwars.api.messaging.member.MessagingMember> members = Collections.singleton(this);

    protected MessagingMember(CommandSender sender, Function<BukkitAudiences, Audience> fn) {
        this.audienceMember = fn.apply(Messaging.getInstance().getAdventure());
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
