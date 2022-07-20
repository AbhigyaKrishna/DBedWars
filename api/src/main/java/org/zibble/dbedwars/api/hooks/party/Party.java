package org.zibble.dbedwars.api.hooks.party;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.Message;

import java.util.List;
import java.util.UUID;

public interface Party {

    UUID getLeader();

    void setLeader(Player player);

    boolean isMember(Player uuid);

    List<Player> getMembers();

    void addMember(Player player);

    void removeMember(Player player);

    default void sendMessage(Message message) {
        for (Player member : this.getMembers()) {
            Messaging.get().getMessagingMember(member).sendMessage(message);
        }
    }

}
