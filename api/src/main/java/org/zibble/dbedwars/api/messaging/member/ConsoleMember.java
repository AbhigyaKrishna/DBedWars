package org.zibble.dbedwars.api.messaging.member;

import org.bukkit.command.ConsoleCommandSender;

public interface ConsoleMember extends MessagingMember {

    ConsoleCommandSender getConsole();

}