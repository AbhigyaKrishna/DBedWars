package org.zibble.dbedwars.api.messaging.member;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.util.Keyed;

import java.util.UUID;

public interface PlayerMember extends MessagingMember, Keyed<UUID> {

    Player getPlayer();

}
