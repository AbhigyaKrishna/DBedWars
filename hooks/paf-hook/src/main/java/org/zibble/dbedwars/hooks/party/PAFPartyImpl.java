package org.zibble.dbedwars.hooks.party;

import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.party.Party;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PAFPartyImpl implements Party {

    private final PAFHook hook;
    private final PlayerParty handle;

    public PAFPartyImpl(PAFHook hook, PlayerParty handle) {
        this.hook = hook;
        this.handle = handle;
    }

    @Override
    public UUID getLeader() {
        return handle.getLeader().getUniqueId();
    }

    @Override
    public void setLeader(Player player) {
        this.handle.setLeader(this.hook.getPlayerManager().getPlayer(player));
    }

    @Override
    public boolean isMember(Player uuid) {
        return this.handle.isInParty(this.hook.getPlayerManager().getPlayer(uuid));
    }

    @Override
    public List<Player> getMembers() {
        return this.handle.getPlayers().stream().map(OnlinePAFPlayer::getPlayer).collect(Collectors.toList());
    }

    @Override
    public void addMember(Player player) {
        this.handle.addPlayer(this.hook.getPlayerManager().getPlayer(player));
    }

    @Override
    public void removeMember(Player player) {
        this.handle.kickPlayer(this.hook.getPlayerManager().getPlayer(player));
    }

}
