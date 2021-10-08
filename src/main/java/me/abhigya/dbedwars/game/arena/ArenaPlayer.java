package me.abhigya.dbedwars.game.arena;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.entity.UUIDPlayer;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.events.game.ArenaEndEvent;
import me.abhigya.dbedwars.api.events.game.PlayerFinalKillEvent;
import me.abhigya.dbedwars.api.events.game.PlayerKillEvent;
import me.abhigya.dbedwars.api.events.game.TeamEliminateEvent;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.DeathCause;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.view.ShopView;
import me.abhigya.dbedwars.task.RespawnTask;
import me.abhigya.dbedwars.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.Optional;

public class ArenaPlayer implements me.abhigya.dbedwars.api.game.ArenaPlayer {

    private final UUIDPlayer player;
    private Arena arena;
    private Team team;

    private boolean spectator;
    private int kill;
    private int finalKill;
    private int death;
    private boolean respawning;
    private boolean finalKilled;
    private me.abhigya.dbedwars.api.game.ArenaPlayer lastHitTag;
    private Instant lastHitTime;
    private ShopView shopView;
    private ItemStack[] previousInv;
    private ItemStack[] previousArmor;

    public ArenaPlayer( Player player, Arena arena ) {
        this.player = new UUIDPlayer( player );
        this.arena = arena;
        this.shopView = new me.abhigya.dbedwars.game.arena.view.shop.ShopView( this );
    }

    @Override
    public Arena getArena( ) {
        return this.arena;
    }

    @Override
    public void setArena( Arena arena ) {
        this.arena = arena;
    }

    @Override
    public Team getTeam( ) {
        return this.team;
    }

    @Override
    public void setTeam( Team team ) {
        this.team = team;
    }

    @Override
    public boolean isSpectator( ) {
        return this.spectator;
    }

    @Override
    public void setSpectator( boolean spectator ) {
        if ( spectator ) {
            this.spectator = true;
            this.getPlayer( ).setGameMode( GameMode.SPECTATOR );
        } else {
            this.spectator = false;
            this.getPlayer( ).setGameMode( GameMode.SURVIVAL );
        }
    }

    @Override
    public void addKill( ) {
        this.kill++;
    }

    @Override
    public void setKill( int count ) {
        this.kill = count;
    }

    @Override
    public int getKills( ) {
        return this.kill;
    }

    @Override
    public void addFinalKills( ) {
        this.finalKill++;
    }

    @Override
    public int getFinalKills( ) {
        return this.finalKill;
    }

    @Override
    public void setFinalKills( int count ) {
        this.finalKill = count;
    }

    @Override
    public void addDeath( ) {
        this.death++;
    }

    @Override
    public int getDeath( ) {
        return this.death;
    }

    @Override
    public void setDeath( int count ) {
        this.death = count;
    }

    @Override
    public void kill( DeathCause reason ) {
        // TODO: revamp this
        if ( this.team.isBedBroken( ) ) {
            PlayerFinalKillEvent event = new PlayerFinalKillEvent( this, this.getLastHitTagged( ), this.arena, reason,
                    StringUtils.translateAlternateColorCodes( "&" + this.getTeam( ).getColor( ).getChatSymbol( ) + this.getPlayer( ).getName( ) + " &7died. &bFINAL KILL" ) );
            event.call( );

            if ( event.isCancelled( ) )
                return;

            event.getVictim( ).addDeath( );
            event.getVictim( ).setSpectator( true );
            this.previousInv = event.getVictim( ).getPlayer( ).getInventory( ).getContents( );
            this.previousArmor = event.getVictim( ).getPlayer( ).getInventory( ).getArmorContents( );
            event.getVictim( ).getPlayer( ).getInventory( ).clear( );
            if ( reason == DeathCause.VOID )
                event.getVictim( ).getPlayer( ).teleport( this.arena.getSettings( ).getSpectatorLocation( ).toBukkit( this.arena.getWorld( ) ) );
            event.getVictim( ).setFinalKilled( true );
            event.getVictim( ).getArena( ).broadcast( event.getKillMessage( ) );
            if ( event.getVictim( ).getTeam( ).getPlayers( ).stream( ).allMatch( me.abhigya.dbedwars.api.game.ArenaPlayer::isFinalKilled ) ) {
                TeamEliminateEvent e = new TeamEliminateEvent( this.arena, event.getVictim( ).getTeam( ) );
                e.call( );

                e.getTeam( ).setEliminated( true );

                Optional< Team > oTeam = this.arena.getTeams( ).stream( ).filter( t -> !t.isEliminated( ) ).findFirst( );
                if ( oTeam.isPresent( ) && this.arena.getTeams( ).stream( ).filter( t -> !t.isEliminated( ) ).count( ) == 1 ) {
                    ArenaEndEvent ev = new ArenaEndEvent( this.arena, oTeam.get( ).getPlayers( ) );
                    ev.call( );
                    if ( ev.isCancelled( ) )
                        return;

                    this.arena.end( );
                }
            }
        } else {
            PlayerKillEvent event = new PlayerKillEvent( this, this.getLastHitTagged( ), this.arena, reason, StringUtils.translateAlternateColorCodes(
                    "&" + this.getTeam( ).getColor( ).getChatSymbol( ) + this.getPlayer( ).getName( ) + " &7died." ) );
            event.call( );

            if ( event.isCancelled( ) )
                return;

            this.addDeath( );
            this.setSpectator( true );
            event.getVictim( ).getPlayer( ).getInventory( ).clear( );
            this.getPlayer( ).teleport( this.arena.getSettings( ).getSpectatorLocation( ).toBukkit( this.arena.getWorld( ) ) );
            this.arena.broadcast( event.getKillMessage( ) );
            this.setRespawning( true );
            DBedwars.getInstance( ).getThreadHandler( ).addAsyncWork( new RespawnTask( DBedwars.getInstance( ), event.getVictim( ) ) );
        }
    }

    @Override
    public boolean isFinalKilled( ) {
        return this.finalKilled;
    }

    @Override
    public void setFinalKilled( boolean flag ) {
        this.finalKilled = flag;
    }

    @Override
    public UUIDPlayer getUUIDPlayer( ) {
        return this.player;
    }

    @Override
    public Player getPlayer( ) {
        return this.player.get( );
    }

    @Override
    public void spawn( Location location ) {
        Utils.setSpawnInventory( this.getPlayer( ), this.team, this.previousInv, this.previousArmor );
        this.getPlayer( ).teleport( location );
        this.getPlayer( ).setHealth( 20 );
    }

    @Override
    public void sendMessage( String msg ) {
        this.getPlayer( ).sendMessage( msg );
    }

    @Override
    public void sendMessage( BaseComponent[] component ) {
        this.getPlayer( ).spigot( ).sendMessage( component );
    }

    @Override
    public me.abhigya.dbedwars.api.game.ArenaPlayer getLastHitTagged( ) {
        if ( this.lastHitTime == null )
            return null;

        return ( System.currentTimeMillis( ) - this.lastHitTime.toEpochMilli( ) ) / 1000 > DBedwars.getInstance( ).getConfigHandler( ).getMainConfiguration( ).getArenaSection( ).getPlayerHitTagLength( )
                ? null : this.lastHitTag;
    }

    @Override
    public void setLastHitTag( me.abhigya.dbedwars.api.game.ArenaPlayer player ) {
        this.lastHitTag = player;
        this.setLastHitTime( Instant.now( ) );
    }

    @Override
    public Instant getLastHitTime( ) {
        return this.lastHitTime;
    }

    @Override
    public void setLastHitTime( Instant instant ) {
        this.lastHitTime = instant;
    }

    @Override
    public boolean isRespawning( ) {
        return this.respawning;
    }

    public void setRespawning( boolean flag ) {
        this.respawning = flag;
    }

    @Override
    public ShopView getShopView( ) {
        return this.shopView;
    }

    @Override
    public void queueRespawn( ) {
        this.respawning = true;
        DBedwars.getInstance( ).getThreadHandler( ).addAsyncWork( new RespawnTask( DBedwars.getInstance( ), this ) );
    }

}
