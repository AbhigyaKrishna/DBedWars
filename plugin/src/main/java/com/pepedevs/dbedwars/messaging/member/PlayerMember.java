package com.pepedevs.dbedwars.messaging.member;

import org.bukkit.entity.Player;

public class PlayerMember extends MessagingMember implements com.pepedevs.dbedwars.api.messaging.member.PlayerMember {

    public PlayerMember(Player player) {
        super(player);
    }

    @Override
    public boolean isConsoleMember() {
        return false;
    }

    @Override
    public boolean isPlayerMember() {
        return true;
    }

    @Override
    public Player getPlayer() {
        return (Player) this.getSender();
    }

}
