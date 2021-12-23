package com.pepedevs.dbedwars.api.messaging.member;

import com.pepedevs.dbedwars.api.messaging.AbstractMessaging;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

public interface MessagingMember extends AbstractMessaging {

    CommandSender getSender();

    boolean isConsoleMember();

    boolean isPlayerMember();

    Audience getAudienceMember();

}
