package org.zibble.dbedwars.game.setup;

import org.bukkit.Location;
import org.zibble.dbedwars.api.util.Color;

import java.util.EnumMap;
import java.util.Map;

public class SetupSessionDataHolder {

    private String arenaCustomName;
    private Location waitingLocation;

    private final Map<Color, TeamDataHolder> teamDataMap = new EnumMap<>(Color.class);

    public String getArenaCustomName() {
        return arenaCustomName;
    }

    public void setArenaCustomName(String arenaCustomName) {
        this.arenaCustomName = arenaCustomName;
    }

    public Location getWaitingLocation() {
        return waitingLocation;
    }

    public void setWaitingLocation(Location waitingLocation) {
        this.waitingLocation = waitingLocation;
    }

    public Location getTeamSpawn(Color color) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) return null;
        return holder.spawn;
    }

    public void setTeamSpawn(Color color, Location location) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamDataHolder();
            this.teamDataMap.put(color, holder);
        }
        holder.spawn = location;
    }

    public Location getShopNPC(Color color) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) return null;
        return holder.shopNPC;
    }

    public void setShopNPC(Color color, Location location) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamDataHolder();
            this.teamDataMap.put(color, holder);
        }
        holder.shopNPC = location;
    }

    public Location getTeamBed(Color color) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) return null;
        return holder.bedLocation;
    }

    public void setTeamBed(Color color, Location location) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamDataHolder();
            this.teamDataMap.put(color, holder);
        }
        holder.bedLocation = location;
    }

    public Location getUpgradesNPC(Color color) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) return null;
        return holder.upgradesNPC;
    }

    public void setUpgradesNPC(Color color, Location location) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamDataHolder();
            this.teamDataMap.put(color, holder);
        }
        holder.upgradesNPC = location;
    }

    public Location getGenLocation(Color color) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) return null;
        return holder.genLocation;
    }

    public void setGenLocation(Color color, Location location) {
        TeamDataHolder holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamDataHolder();
            this.teamDataMap.put(color, holder);
        }
        holder.genLocation = location;
    }

    private static class TeamDataHolder {
        private Location spawn;
        private Location shopNPC;
        private Location upgradesNPC;
        private Location bedLocation;
        private Location genLocation;
    }
}
