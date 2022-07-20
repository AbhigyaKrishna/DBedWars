package org.zibble.dbedwars.hooks.party;

import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.hooks.friend.FriendHook;
import org.zibble.dbedwars.api.hooks.party.Party;
import org.zibble.dbedwars.api.hooks.party.PartyHook;
import org.zibble.dbedwars.api.plugin.PluginDependence;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PAFHook extends PluginDependence implements PartyHook, FriendHook {

    private PAFPlayerManager playerManager;
    private PartyManager partyManager;

    public PAFHook() {
        super("PartyAndFriends");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            this.playerManager = PAFPlayerManager.getInstance();
            this.partyManager = PartyManager.getInstance();
        }
        return true;
    }

    @Override
    public boolean isInParty(Player player) {
        return this.playerManager.getPlayer(player).getParty() != null;
    }

    @Override
    public Party getParty(Player player) {
        return new PAFPartyImpl(this, this.playerManager.getPlayer(player).getParty());
    }

    @Override
    public List<PAFFriendImpl> getFriends(UUID player) {
        return this.playerManager.getPlayer(player).getFriends().stream().map(PAFFriendImpl::new).collect(Collectors.toList());
    }

    @Override
    public boolean areFriends(UUID player1, UUID player2) {
        return this.playerManager.getPlayer(player1).isAFriendOf(this.playerManager.getPlayer(player2));
    }

    public PAFPlayerManager getPlayerManager() {
        return playerManager;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

}
