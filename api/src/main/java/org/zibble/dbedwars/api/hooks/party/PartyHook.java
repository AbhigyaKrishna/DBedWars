package org.zibble.dbedwars.api.hooks.party;

import org.bukkit.entity.Player;

public interface PartyHook {

    boolean isInParty(Player player);

    Party getParty(Player player);

}
