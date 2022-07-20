package org.zibble.dbedwars.hooks.party;

import de.simonsator.partyandfriends.api.pafplayers.PAFPlayer;
import org.zibble.dbedwars.api.hooks.friend.Friend;

import java.util.UUID;

public class PAFFriendImpl implements Friend {

    private final PAFPlayer player;

    public PAFFriendImpl(PAFPlayer player) {
        this.player = player;
    }

    @Override
    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public boolean isOnline() {
        return this.player.isOnline();
    }

}
