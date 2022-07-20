package org.zibble.dbedwars.api.hooks.friend;

import java.util.List;
import java.util.UUID;

public interface FriendHook {

    List<? extends Friend> getFriends(UUID player);

    boolean areFriends(UUID player1, UUID player2);

}
