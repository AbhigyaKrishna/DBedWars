package com.pepedevs.dbedwars.api.messaging.member;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

public interface MessagingMember {

    CommandSender getSender();

    boolean isConsoleMember();

    boolean isPlayerMember();

    Audience getAudienceMember();
}
