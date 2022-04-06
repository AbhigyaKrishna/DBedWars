package org.zibble.dbedwars.game.setup;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.game.ArenaDataHolderImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MissingArenaData {

    private final SetupSession session;

    public MissingArenaData(SetupSession session) {
        this.session = session;
    }

    public boolean hasWorld() {
        return this.session.getArenaDataHolder().getWorldFileName() != null;
    }

    public boolean hasWaitingLocation() {
        return this.session.getArenaDataHolder().getWaitingLocation() != null;
    }

    public boolean hasSpectatorLocation() {
        return this.session.getArenaDataHolder().getSpectatorLocation() != null;
    }

    public boolean hasCustomName() {
        return this.session.getArenaDataHolder().getCustomName() != null;
    }

    public boolean isEnabled() {
        return this.session.getArenaDataHolder().isEnabled();
    }

    public boolean hasLobbyArea() {
        return this.session.getArenaDataHolder().getLobbyArea() != null;
    }

    public boolean hasLobbyCorner1() {
        return DBedwars.getInstance().getHookManager().getAreaSelectionHook().getSecondLocation(this.session.getPlayer()) != null;
    }

    public boolean hasLobbyCorner2() {
        return DBedwars.getInstance().getHookManager().getAreaSelectionHook().getSecondLocation(this.session.getPlayer()) != null;
    }

    public boolean hasTeams() {
        return !this.session.getArenaDataHolder().getTeamData().isEmpty();
    }

    public boolean isTeamPresent(Color color) {
        return this.session.getArenaDataHolder().getTeamData().containsKey(color);
    }

    public boolean hasBed(Color color) {
        return this.session.getArenaDataHolder().getTeamData().get(color).getBed() != null;
    }

    public Set<Color> getTeamsWithMissingBed() {
        Set<Color> missingBed = new HashSet<>();
        for (Map.Entry<Color, ArenaDataHolderImpl.TeamDataHolderImpl> team : this.session.getArenaDataHolder().getTeamData().entrySet()) {
            if (team.getValue().getBed() != null) {
                missingBed.add(team.getKey());
            }
        }
        return missingBed;
    }

    public boolean hasSpawn(Color color) {
        return this.session.getArenaDataHolder().getTeamData().get(color).getSpawnLocation() != null;
    }

    public Set<Color> getTeamsWithMissingSpawn() {
        Set<Color> missingSpawn = new HashSet<>();
        for (Map.Entry<Color, ArenaDataHolderImpl.TeamDataHolderImpl> team : this.session.getArenaDataHolder().getTeamData().entrySet()) {
            if (team.getValue().getSpawnLocation() != null) {
                missingSpawn.add(team.getKey());
            }
        }
        return missingSpawn;
    }

    public boolean hasSpawners(Color color) {
        return !this.session.getArenaDataHolder().getTeamData().get(color).getSpawners().isEmpty();
    }

    public SetupSession getSession() {
        return session;
    }

}
