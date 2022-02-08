package com.pepedevs.dbedwars.messaging.member;

import org.bukkit.command.ConsoleCommandSender;

public class ConsoleMember extends MessagingMember
        implements com.pepedevs.dbedwars.api.messaging.member.ConsoleMember {

    public ConsoleMember(ConsoleCommandSender sender) {
        super(sender);
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
