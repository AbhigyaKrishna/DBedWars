package me.abhigya.dbedwars.api.game;

import com.google.common.collect.Multimap;
import me.Abhigya.core.util.math.collision.BoundingBox;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.Color;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.api.util.LocationXYZYP;
import me.abhigya.dbedwars.api.util.TrapEnum;

import java.util.List;
import java.util.Set;

public interface Team {

    Color getColor( );

    String getName( );

    LocationXYZ getBedLocation( );

    void setBedLocation( LocationXYZ location );

    LocationXYZYP getSpawn( );

    void setSpawn( LocationXYZYP location );

    void addSpawner( DropType dropType, LocationXYZ location );

    Multimap< DropType, LocationXYZ > getSpawners( );

    LocationXYZYP getShopNpc( );

    void setShopNpc( LocationXYZYP location );

    LocationXYZYP getUpgradesNpc( );

    void setUpgradesNpc( LocationXYZYP location );

    void reloadData( );

    boolean isConfigured( );

    void init( Arena arena );

    Arena getArena( );

    void addPlayer( ArenaPlayer player );

    void removePlayer( ArenaPlayer player );

    Set< ArenaPlayer > getPlayers( );

    void sendMessage( String message );

    boolean isBedBroken( );

    void setBedBroken( boolean flag );

    boolean isEliminated( );

    void setEliminated( boolean flag );

    BoundingBox getIslandArea( );

    List< Trap > getTrapQueue( );

    void triggerTrap( TrapEnum.TriggerType trigger, ArenaPlayer target );

    void spawnShopNpc( LocationXYZYP location );

    void spawnUpgradesNpc( LocationXYZYP location );

}
