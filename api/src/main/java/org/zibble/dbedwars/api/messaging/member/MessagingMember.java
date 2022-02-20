package org.zibble.dbedwars.api.messaging.member;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.messaging.AbstractMessaging;

public interface MessagingMember extends AbstractMessaging {

    CommandSender getSender();

    boolean isConsoleMember();

    boolean isPlayerMember();

    Audience getAudienceMember();

}
