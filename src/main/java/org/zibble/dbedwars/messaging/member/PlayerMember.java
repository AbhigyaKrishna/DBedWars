package org.zibble.dbedwars.messaging.member;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.util.Key;

import java.util.UUID;

public class PlayerMember extends MessagingMember implements org.zibble.dbedwars.api.messaging.member.PlayerMember {

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

    @Override
    public Key<UUID> getKey() {
        return Key.of(this.getPlayer().getUniqueId());
    }

}
