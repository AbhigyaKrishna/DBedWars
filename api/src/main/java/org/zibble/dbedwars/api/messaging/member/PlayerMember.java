package org.zibble.dbedwars.api.messaging.member;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.util.key.Keyed;

public interface PlayerMember extends MessagingMember, Keyed {

    Player getPlayer();

}
