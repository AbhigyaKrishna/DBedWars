package me.abhigya.dbedwars.game.arena.settings;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.RegenerationType;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.Color;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.api.util.LocationXYZYP;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableArena;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ArenaSettings implements me.abhigya.dbedwars.api.game.settings.ArenaSettings {

    private final DBedwars plugin;
    private final Arena arena;

    private String name;
    private String customName;
    private World.Environment worldEnv;
    private RegenerationType regenerationType;
    private LocationXYZYP lobby;
    private LocationXYZYP spectatorLocation;
    private LocationXYZ lobbyPosMax;
    private LocationXYZ lobbyPosMin;
    private BwItemStack icon;
    private int teamPlayers;
    private int minPlayers;

    /* Overridable settings */
    private int startTimer;
    private int respawnTime;
    private int islandRadius;
    private int minYAxis;
    private int playerHitTagLength;
    private int gameEndDelay;
    private boolean disableHunger;
    // Points
    private int bedDestroyPoint;
    private int killPoint;
    private int finalKillPoint;
    private int deathPoint;

    private Multimap< DropType, LocationXYZ > drops;
    private Set< Team > availableTeams;

    public ArenaSettings( DBedwars plugin, Arena arena ) {
        this.plugin = plugin;
        this.arena = arena;
        this.teamPlayers = 1;
        this.worldEnv = World.Environment.NORMAL;
        this.regenerationType = RegenerationType.MULTI_THREADED_SYNC;
        this.drops = ArrayListMultimap.create( );
        this.availableTeams = new HashSet<>( );

        this.startTimer = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getStartTimer( );
        this.respawnTime = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getRespawnTime( );
        this.islandRadius = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getIslandRadius( );
        this.minYAxis = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getMinYAxis( );
        this.playerHitTagLength = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getPlayerHitTagLength( );
        this.gameEndDelay = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getGameEndDelay( );
        this.disableHunger = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).isDisableHunger( );
        this.bedDestroyPoint = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getBedDestroyPoint( );
        this.killPoint = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getKillPoint( );
        this.finalKillPoint = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getFinalKillPoint( );
        this.deathPoint = this.plugin.getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getDeathPoint( );
    }

    public ArenaSettings( DBedwars plugin, Arena arena, ConfigurableArena cfgArena ) {
        this( plugin, arena );
        this.update( cfgArena );
    }

    @Override
    public Arena getArena( ) {
        return this.arena;
    }

    @Override
    public String getName( ) {
        return this.name;
    }

    @Override
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public String getCustomName( ) {
        return this.customName;
    }

    @Override
    public void setCustomName( String customName ) {
        this.customName = customName;
    }

    @Override
    public boolean hasCustomName( ) {
        return this.customName != null;
    }

    @Override
    public World.Environment getWorldEnv( ) {
        return this.worldEnv;
    }

    @Override
    public void setWorldEnv( World.Environment worldEnv ) {
        this.worldEnv = worldEnv;
    }

    @Override
    public RegenerationType getRegenerationType( ) {
        return this.regenerationType;
    }

    @Override
    public void setRegenerationType( RegenerationType regenerationType ) {
        this.regenerationType = regenerationType;
    }

    @Override
    public LocationXYZYP getLobby( ) {
        return this.lobby;
    }

    @Override
    public void setLobby( LocationXYZYP lobby ) {
        this.lobby = lobby;
    }

    @Override
    public boolean hasLobby( ) {
        return this.lobby != null;
    }

    @Override
    public LocationXYZYP getSpectatorLocation( ) {
        return this.spectatorLocation;
    }

    @Override
    public void setSpectatorLocation( LocationXYZYP spectatorLocation ) {
        this.spectatorLocation = spectatorLocation;
    }

    @Override
    public LocationXYZ getLobbyPosMax( ) {
        return this.lobbyPosMax;
    }

    @Override
    public void setLobbyPosMax( LocationXYZ location ) {
        this.lobbyPosMax = location;
    }

    @Override
    public LocationXYZ getLobbyPosMin( ) {
        return this.lobbyPosMin;
    }

    @Override
    public void setLobbyPosMin( LocationXYZ location ) {
        this.lobbyPosMin = location;
    }

    @Override
    public BwItemStack getIcon( ) {
        return this.icon;
    }

    @Override
    public void setIcon( BwItemStack icon ) {
        this.icon = icon;
    }

    @Override
    public int getTeamPlayers( ) {
        return this.teamPlayers;
    }

    @Override
    public void setTeamPlayers( int teamPlayers ) {
        this.teamPlayers = teamPlayers;
    }

    @Override
    public int getMinPlayers( ) {
        return this.minPlayers;
    }

    @Override
    public void setMinPlayers( int minPlayers ) {
        this.minPlayers = minPlayers;
    }

    @Override
    public int getMaxPlayer( ) {
        return this.teamPlayers * this.availableTeams.size( );
    }

    @Override
    public Multimap< DropType, LocationXYZ > getDrops( ) {
        return this.drops;
    }

    @Override
    public void addDrop( DropType dropType, LocationXYZ location ) {
        this.drops.put( dropType.clone( ), location );
    }

    @Override
    public void removeDrop( LocationXYZ location ) {
        for ( Map.Entry< DropType, LocationXYZ > d : this.drops.entries( ) ) {
            if ( d.getValue( ).equals( location ) ) {
                this.drops.remove( d.getKey( ), d.getValue( ) );
            }
        }
    }

    @Override
    public void removeDrop( DropType dropType, LocationXYZ location ) {
        this.drops.remove( dropType, location );
    }

    @Override
    public Collection< LocationXYZ > getDropLocation( DropType dropType ) {
        return this.drops.get( dropType );
    }

    @Override
    public Set< Team > getAvailableTeams( ) {
        return Collections.unmodifiableSet( this.availableTeams );
    }

    @Override
    public void enableTeam( Color teamColor ) {
        this.availableTeams.add( new me.abhigya.dbedwars.game.arena.Team( teamColor ) );
    }

    @Override
    public void enableTeam( Team team ) {
        this.availableTeams.add( team );
    }

    @Override
    public boolean isEnabled( Color teamColor ) {
        return this.availableTeams.stream( ).anyMatch( t -> t.getColor( ).equals( teamColor ) );
    }

    @Nullable
    @Override
    public Team getTeam( Color teamColor ) {
        for ( Team t : this.availableTeams ) {
            if ( t.getColor( ).equals( teamColor ) )
                return t;
        }

        return null;
    }

    @Override
    public void disableTeam( Color teamColor ) {
        this.availableTeams.removeIf( team -> team.getColor( ).equals( teamColor ) );
    }

    @Override
    public void disableTeam( Team team ) {
        this.availableTeams.remove( team );
    }

    @Override
    public int getStartTimer( ) {
        return startTimer;
    }

    @Override
    public void setStartTimer( int startTimer ) {
        this.startTimer = startTimer;
    }

    @Override
    public int getRespawnTime( ) {
        return respawnTime;
    }

    @Override
    public void setRespawnTime( int respawnTime ) {
        this.respawnTime = respawnTime;
    }

    @Override
    public int getIslandRadius( ) {
        return islandRadius;
    }

    @Override
    public void setIslandRadius( int islandRadius ) {
        this.islandRadius = islandRadius;
    }

    @Override
    public int getMinYAxis( ) {
        return minYAxis;
    }

    @Override
    public void setMinYAxis( int minYAxis ) {
        this.minYAxis = minYAxis;
    }

    @Override
    public int getPlayerHitTagLength( ) {
        return playerHitTagLength;
    }

    @Override
    public void setPlayerHitTagLength( int playerHitTagLength ) {
        this.playerHitTagLength = playerHitTagLength;
    }

    @Override
    public int getGameEndDelay( ) {
        return gameEndDelay;
    }

    @Override
    public void setGameEndDelay( int gameEndDelay ) {
        this.gameEndDelay = gameEndDelay;
    }

    @Override
    public boolean isDisableHunger( ) {
        return disableHunger;
    }

    @Override
    public void setDisableHunger( boolean flag ) {
        this.disableHunger = flag;
    }

    @Override
    public int getBedDestroyPoint( ) {
        return bedDestroyPoint;
    }

    @Override
    public void setBedDestroyPoint( int bedDestroyPoint ) {
        this.bedDestroyPoint = bedDestroyPoint;
    }

    @Override
    public int getKillPoint( ) {
        return killPoint;
    }

    @Override
    public void setKillPoint( int killPoint ) {
        this.killPoint = killPoint;
    }

    @Override
    public int getFinalKillPoint( ) {
        return finalKillPoint;
    }

    @Override
    public void setFinalKillPoint( int finalKillPoint ) {
        this.finalKillPoint = finalKillPoint;
    }

    @Override
    public int getDeathPoint( ) {
        return deathPoint;
    }

    @Override
    public void setDeathPoint( int deathPoint ) {
        this.deathPoint = deathPoint;
    }

    public void update( ConfigurableArena cfgArena ) {
        this.name = cfgArena.getIdentifier( );
        this.customName = cfgArena.getCustomName( );
        this.worldEnv = cfgArena.getWorldEnv( );
        this.icon = cfgArena.getIcon( );
        this.lobby = cfgArena.getLobbyLoc( );
        this.lobbyPosMax = cfgArena.getLobbyPosMax( );
        this.lobbyPosMin = cfgArena.getLobbyPosMin( );
        this.spectatorLocation = cfgArena.getSpectatorLocation( );
        this.teamPlayers = cfgArena.getPlayerInTeam( );
        this.minPlayers = cfgArena.getMinPlayers( );
        this.regenerationType = cfgArena.getRegenerationType( );
        this.availableTeams = new HashSet<>( cfgArena.getTeams( ) );
        this.drops = cfgArena.getSpawners( );
    }

}
