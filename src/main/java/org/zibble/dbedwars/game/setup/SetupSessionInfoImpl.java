package org.zibble.dbedwars.game.setup;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.zibble.dbedwars.api.game.setup.SetupSessionInfo;
import org.zibble.dbedwars.api.util.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SetupSessionInfoImpl implements SetupSessionInfo {

    private World.Environment dimension;

    private String arenaCustomName;

    private Location waitingLocation;
    private Location spectatorLocation;

    private Block waitingLocationCorner1;
    private Block waitingLocationCorner2;

    private int maxPlayersPerTeam;
    private int minPlayersToStart;

    private final Map<Color, TeamInfoImpl> teamDataMap = new EnumMap<>(Color.class);

    @Override
    public String getArenaCustomName() {
        return arenaCustomName;
    }

    protected void setArenaCustomName(String arenaCustomName) {
        this.arenaCustomName = arenaCustomName;
    }

    @Override
    public Location getWaitingLocation() {
        return waitingLocation;
    }

    protected void setWaitingLocation(Location waitingLocation) {
        this.waitingLocation = waitingLocation;
    }

    protected void setTeamSpawn(Color color, Location location) {
        TeamInfoImpl holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamInfoImpl();
            this.teamDataMap.put(color, holder);
        }
        holder.spawn = location;
    }

    protected void setShopNPC(Color color, Location location) {
        TeamInfoImpl holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamInfoImpl();
            this.teamDataMap.put(color, holder);
        }
        holder.shopNPC = location;
    }

    protected void setTeamBed(Color color, Location location) {
        TeamInfoImpl holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamInfoImpl();
            this.teamDataMap.put(color, holder);
        }
        holder.bedLocation = location;
    }

    protected void setUpgradesNPC(Color color, Location location) {
        TeamInfoImpl holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamInfoImpl();
            this.teamDataMap.put(color, holder);
        }
        holder.upgradesNPC = location;
    }

    @Override
    public Map<Color, ? extends TeamInfo> getTeamData() {
        return this.teamDataMap;
    }

    protected void addGenLocation(Color color, Location location) {
        TeamInfoImpl holder = this.teamDataMap.get(color);
        if (holder == null) {
            holder = new TeamInfoImpl();
            this.teamDataMap.put(color, holder);
        }
        holder.genLocations.add(location);
    }

    protected void setWaitingLocationCorner1(Block location) {
        this.waitingLocationCorner1 = location;
    }

    protected void setWaitingLocationCorner2(Block location) {
        this.waitingLocationCorner2 = location;
    }

    @Override
    public Block getWaitingLocationCorner1() {
        return waitingLocationCorner1;
    }

    @Override
    public Block getWaitingLocationCorner2() {
        return waitingLocationCorner2;
    }

    @Override
    public World.Environment getDimension() {
        return dimension;
    }

    protected void setDimension(World.Environment dimension) {
        this.dimension = dimension;
    }

    @Override
    public Location getSpectatorLocation() {
        return spectatorLocation;
    }

    protected void setSpectatorLocation(Location spectatorLocation) {
        this.spectatorLocation = spectatorLocation;
    }

    @Override
    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    @Override
    public int getMinPlayersToStart() {
        return minPlayersToStart;
    }

    protected void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    protected void setMinPlayersToStart(int minPlayersToStart) {
        this.minPlayersToStart = minPlayersToStart;
    }

    protected void addTeamInfo(Color color, TeamInfoImpl teamInfo) {
        this.teamDataMap.put(color, teamInfo);
    }

    protected static class TeamInfoImpl implements TeamInfo {
        protected Location spawn;
        protected Location shopNPC;
        protected Location upgradesNPC;
        protected Location bedLocation;
        protected final List<Location> genLocations = new ArrayList<>();

        @Override
        public Location getSpawn() {
            return this.spawn;
        }

        @Override
        public Location getBed() {
            return this.bedLocation;
        }

        @Override
        public Location getShop() {
            return this.shopNPC;
        }

        @Override
        public Location getUpgrades() {
            return this.upgradesNPC;
        }

        @Override
        public List<Location> getGenLocations() {
            return this.genLocations;
        }
    }
}
