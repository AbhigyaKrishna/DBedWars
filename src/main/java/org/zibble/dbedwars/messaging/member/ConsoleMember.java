package org.zibble.dbedwars.messaging.member;

import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleMember extends MessagingMember implements org.zibble.dbedwars.api.messaging.member.ConsoleMember {

    public ConsoleMember(ConsoleCommandSender sender) {
        super(sender, AudienceProvider::console);
    }

    @Override
    public boolean isConsoleMember() {
        return true;
    }

    @Override
    public boolean isPlayerMember() {
        return false;
    }

    @Override
    public ConsoleCommandSender getConsole() {
        return (ConsoleCommandSender) this.getSender();
    }

}
