package org.zibble.dbedwars.game.setup;

import org.bukkit.World;
import org.zibble.dbedwars.api.game.setup.SetupSessionInfo;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ArenaDataHolder {

    private String name;
    private int maxPlayersPerTeam;
    private int minPlayersToStart;
    private boolean enabled;
    private World.Environment environment;

    private LocationXYZYP waitingLocation;
    private LocationXYZYP spectatorLocation;
    private LocationXYZ lobbyCorner1;
    private LocationXYZ lobbyCorner2;

    private final Map<Color, TeamDataHolder> teams;
    //TODO SPAWNER TYPE

    private ArenaDataHolder() {
        this.teams = new EnumMap<>(Color.class);
    }

    public static ArenaDataHolder fromSetupSession(SetupSessionInfo data) {
        ArenaDataHolder holder = new ArenaDataHolder();
        holder.name = data.getArenaCustomName();
        holder.maxPlayersPerTeam = data.getMaxPlayersPerTeam();
        holder.minPlayersToStart = data.getMinPlayersToStart();
        holder.enabled = false;
        holder.environment = data.getDimension();
        holder.waitingLocation = LocationXYZYP.valueOf(data.getWaitingLocation());
        holder.spectatorLocation = LocationXYZYP.valueOf(data.getSpectatorLocation());
        holder.lobbyCorner1 = LocationXYZ.valueOf(data.getWaitingLocationCorner1().getLocation());
        holder.lobbyCorner2 = LocationXYZ.valueOf(data.getWaitingLocationCorner2().getLocation());
        data.getTeamData().forEach((color, teamInfo) -> holder.teams.put(color, TeamDataHolder.fromTeamInfo(teamInfo)));
        return holder;
    }

    public void loadInSession(SetupSessionInfoImpl session, World world) {
        session.setArenaCustomName(this.name);
        session.setMaxPlayersPerTeam(this.maxPlayersPerTeam);
        session.setMinPlayersToStart(this.minPlayersToStart);
        session.setDimension(this.environment);
        session.setWaitingLocation(this.waitingLocation.toBukkit(world));
        session.setSpectatorLocation(this.spectatorLocation.toBukkit(world));
        session.setWaitingLocationCorner1(this.lobbyCorner1.getBlock(world));
        session.setWaitingLocationCorner2(this.lobbyCorner2.getBlock(world));
        this.teams.forEach((color, teamData) -> session.addTeamInfo(color, teamData.toTeamInfo(world)));
    }

    public static ArenaDataHolder fromConfig() {

    }

    public String getName() {
        return name;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public int getMinPlayersToStart() {
        return minPlayersToStart;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public World.Environment getEnvironment() {
        return environment;
    }

    public LocationXYZYP getWaitingLocation() {
        return waitingLocation;
    }

    public LocationXYZYP getSpectatorLocation() {
        return spectatorLocation;
    }

    public LocationXYZ getLobbyCorner1() {
        return lobbyCorner1;
    }

    public LocationXYZ getLobbyCorner2() {
        return lobbyCorner2;
    }

    public Map<Color, TeamDataHolder> getTeamData() {
        return this.teams;
    }

    public static class TeamDataHolder {
        private LocationXYZYP spawnLocation;
        private LocationXYZYP shopNPC;
        private LocationXYZYP upgradesNPC;

        private LocationXYZ bed;

        private final List<LocationXYZ> spawners = new ArrayList<>();

        private static TeamDataHolder fromTeamInfo(SetupSessionInfo.TeamInfo teamInfo) {
            TeamDataHolder teamDataHolder = new TeamDataHolder();
            teamDataHolder.spawnLocation = LocationXYZYP.valueOf(teamInfo.getSpawn());
            teamDataHolder.shopNPC = LocationXYZYP.valueOf(teamInfo.getShop());
            teamDataHolder.upgradesNPC = LocationXYZYP.valueOf(teamInfo.getUpgrades());
            teamDataHolder.bed = LocationXYZ.valueOf(teamInfo.getBed());
            teamInfo.getGenLocations().forEach(location -> teamDataHolder.spawners.add(LocationXYZ.valueOf(location)));
            return teamDataHolder;
        }

        public LocationXYZYP getSpawnLocation() {
            return spawnLocation;
        }

        public LocationXYZYP getShopNPC() {
            return shopNPC;
        }

        public LocationXYZYP getUpgradesNPC() {
            return upgradesNPC;
        }

        public LocationXYZ getBed() {
            return bed;
        }

        public List<LocationXYZ> getSpawners() {
            return spawners;
        }

        public SetupSessionInfoImpl.TeamInfoImpl toTeamInfo(World world) {
            SetupSessionInfoImpl.TeamInfoImpl teamInfo = new SetupSessionInfoImpl.TeamInfoImpl();
            teamInfo.spawn = spawnLocation.toBukkit(world);
            teamInfo.shopNPC = shopNPC.toBukkit(world);
            teamInfo.upgradesNPC = upgradesNPC.toBukkit(world);
            teamInfo.bedLocation = bed.toBukkit(world);
            this.spawners.forEach(location -> teamInfo.genLocations.add(location.toBukkit(world)));
            return teamInfo;
        }
    }
}
