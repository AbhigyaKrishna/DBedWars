package me.abhigya.dbedwars.game.arena;

import me.Abhigya.core.particle.particlelib.ParticleBuilder;
import me.Abhigya.core.particle.particlelib.ParticleEffect;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.hologram.Hologram;
import me.Abhigya.core.util.math.collision.BoundingBox;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.events.game.SpawnerDropItemEvent;
import me.abhigya.dbedwars.api.events.game.SpawnerUpgradeEvent;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.SoundVP;
import me.abhigya.dbedwars.utils.ConfigurationUtils;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Spawner implements me.abhigya.dbedwars.api.game.spawner.Spawner {

    private final DBedwars plugin;
    private final DropType drop;
    private final Arena arena;
    private final Team team;
    private Location location;
    private BoundingBox box;

    private boolean registered;
    private int level;
    private DropType.Tier tier;
    private Instant lastUpgrade;
    private ParticleBuilder particle;
    private Instant start;
    private Hologram hologram;

    private Map< DropType.Drop, Long > items;

    public Spawner( DBedwars plugin, DropType type, Location location, Arena arena, @Nullable Team team ) {
        this.plugin = plugin;
        this.drop = type;
        this.location = location;
        this.box = new BoundingBox( this.location.getX( ) - this.drop.getSpawnRadius( ), this.location.getY( ) - 2, this.location.getZ( ) - this.drop.getSpawnRadius( ),
                this.location.getX( ) + this.drop.getSpawnRadius( ), this.location.getY( ) + 2, this.location.getZ( ) + this.drop.getSpawnRadius( ) );
        this.arena = arena;
        this.team = team;
        this.items = new ConcurrentHashMap<>( );
    }

    @Override
    public void init( ) {
        this.registered = true;
        this.level = 1;
        this.tier = this.drop.getTier( this.level );
        this.particle = new ParticleBuilder( this.drop.getSpawnEffect( ), this.location.clone( ).add( 0, 1, 0 ) ).setSpeed( 0 ).setAmount( 5 );
        if ( this.drop.isHologramEnabled( ) ) {
            this.hologram = this.plugin.getHologramFactory( ).createHologram( this.drop.getId( ) + "@" + UUID.randomUUID( ).toString( ),
                    this.location.clone( ).add( 0, 2, 0 ) );
            this.hologram.add( this.drop.getHologramMaterial( ).toItemStack( ), false );
            for ( String line : this.drop.getHologramText( ) ) {
                this.hologram.add( StringUtils.translateAlternateColorCodes( line ) );
            }

            this.hologram.spawn( );
        }

        this.start = Instant.now( );
        this.lastUpgrade = start;
        for ( DropType.Drop i : this.tier.getDrops( ) ) {
            this.items.put( i, System.currentTimeMillis( ) );
        }
        this.arena.addSpawner( this );
    }

    @Override
    public void tick( ) {
        if ( !this.exists( ) )
            return;

        this.rotateHologram( );
        for ( Map.Entry< DropType.Drop, Long > entry : this.items.entrySet( ) ) {
            if ( ( (double) ( System.currentTimeMillis( ) - entry.getValue( ) ) / ( 50 * 20 ) ) < entry.getKey( ).getDelay( ) )
                continue;
            this.items.put( entry.getKey( ), System.currentTimeMillis( ) );
            if ( entry.getKey( ).getMaxSpawn( ) != -1 ) {
                int count = this.location.getWorld( ).getNearbyEntities( this.location,
                                this.drop.getSpawnRadius( ), this.drop.getSpawnRadius( ), this.drop.getSpawnRadius( ) ).stream( )
                        .filter( e -> e instanceof Item && ( (Item) e ).getItemStack( ).getType( ).equals( entry.getKey( ).getItem( ).getType( ) ) )
                        .filter( e -> Utils.hasPluginData( ( (Item) e ).getItemStack( ) ) )
                        .mapToInt( e -> ( (Item) e ).getItemStack( ).getAmount( ) ).sum( );
                if ( count >= entry.getKey( ).getMaxSpawn( ) )
                    return;
            }

            this.spawn( entry.getKey( ) );
        }

    }

    @Override
    public void spawn( DropType.Drop drop ) {
        SpawnerDropItemEvent event = new SpawnerDropItemEvent( this.arena, this.getDropType( ), drop, this, this.team, this.level, this.tier );
        event.call( );

        if ( event.isCancelled( ) )
            return;

        this.plugin.getThreadHandler( ).getLeastWorkSyncWorker( ).add( ( ) -> {
            BwItemStack stack = event.getDrop( ).getItem( );
            if ( !Spawner.this.getDropType( ).isMerging( ) )
                stack.setUnMergeable( );

            Item item = this.location.getWorld( ).dropItemNaturally( this.location, stack.toItemStack( ) );
            if ( Spawner.this.getDropType( ).isSplitable( ) )
                item.setMetadata( "split", new FixedMetadataValue( this.plugin, true ) );
            if ( this.drop.getSpawnSound( ) != null )
                this.drop.getSpawnSound( ).play( this.location );
            if ( this.drop.getSpawnEffect( ) != null )
                this.particle.display( this.arena.getPlayers( ).stream( ).map( ArenaPlayer::getPlayer ).collect( Collectors.toList( ) ) );
        } );
    }

    @Override
    public boolean upgrade( int level ) {
        DropType.Tier nextTier = this.drop.getTier( level );
        SpawnerUpgradeEvent event = new SpawnerUpgradeEvent( this.arena, this.drop, this, this.team, this.level, level, this.tier, nextTier );
        event.call( );

        if ( event.isCancelled( ) )
            return false;

        this.level = event.getNextLevel( );
        this.lastUpgrade = Instant.now( );
        Map< String, Long > time = new HashMap<>( );
        for ( Map.Entry< DropType.Drop, Long > d : this.items.entrySet( ) ) {
            time.put( d.getKey( ).getKey( ), d.getValue( ) );
        }
        this.items.clear( );
        for ( DropType.Drop i : event.getNextTier( ).getDrops( ) ) {
            if ( time.containsKey( i.getKey( ) ) ) {
                this.items.put( i, time.get( i.getKey( ) ) );
            } else {
                this.items.put( i, System.currentTimeMillis( ) );
            }
        }

        this.plugin.getThreadHandler( ).getLeastWorkSyncWorker( ).add( ( ) -> {
            if ( event.getNextTier( ).getUpgradeMessage( ) != null ) {
                String message = event.getNextTier( ).getUpgradeMessage( );
                String key = message.substring( 0, message.indexOf( " " ) );
                message = message.replaceFirst( key, "" ).trim( );
                this.broadcast( key, message, this.location );
            }
            SoundVP sound = event.getNextTier( ).getUpgradeSound( );
            if ( sound != null ) {
                sound.play( this.location );
            }
            ParticleEffect effect = event.getNextTier( ).getUpgradeEffect( );
            if ( effect != null ) {
                ParticleBuilder particle = new ParticleBuilder( effect, this.location.clone( ).add( 0, 2, 0 ) );
                particle.setSpeed( 0 )
                        .setAmount( 20 )
                        .display( this.arena.getPlayers( ).stream( ).map( ArenaPlayer::getPlayer ).collect( Collectors.toList( ) ) );
            }
        } );

        return true;
    }

    private void rotateHologram( ) {
        if ( this.hologram == null )
            return;

        Location location = this.hologram.getLocation( ).clone( );
        location.setYaw( location.getYaw( ) + 1F );
        this.hologram.teleport( location );
    }

    private void broadcast( String key, String message, @Nullable Location location ) {
        if ( key.equalsIgnoreCase( "-all" ) ) {
//            this.arena.broadcast(message);
            location.getWorld( ).getPlayers( ).forEach( p -> p.sendMessage( ConfigurationUtils.parseMessage( ConfigurationUtils.parsePlaceholder( message, p ) ) ) );
        } else if ( key.equalsIgnoreCase( "-team" ) ) {
            if ( this.team != null )
                this.team.sendMessage( message );
        } else if ( key.startsWith( "-region" ) ) {
            if ( location != null && location.getWorld( ) != null ) {
                double range = 50;
                try {
                    range = Double.parseDouble( key.split( ":" )[1] );
                } catch ( NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException ignored ) {
                }

                location.getWorld( ).getNearbyEntities( location, range, range, range ).stream( ).filter( e -> e instanceof Player )
                        .forEach( e -> ( (Player) e ).sendMessage( ConfigurationUtils.parseMessage( ConfigurationUtils.parsePlaceholder( message, ( (Player) e ) ) ) ) );
            }
        } else if ( key.startsWith( "-player" ) ) {
            Player player = null;
            try {
                player = Bukkit.getPlayer( key.split( ":" )[1] );
            } catch ( ArrayIndexOutOfBoundsException | NullPointerException ignored ) {
            }
            if ( player != null ) {
                player.sendMessage( ConfigurationUtils.parseMessage( ConfigurationUtils.parsePlaceholder( message, player ) ) );
            }
        }
    }

    @Override
    public DropType getDropType( ) {
        return this.drop;
    }

    @Override
    public Instant getStartTime( ) {
        return this.start;
    }

    @Override
    public Location getLocation( ) {
        return this.location;
    }

    @Override
    public void setLocation( Location location ) {
        this.location = location;
        this.box = new BoundingBox( this.location.getX( ) - this.drop.getSpawnRadius( ), this.location.getY( ) - 2, this.location.getZ( ) - this.drop.getSpawnRadius( ),
                this.location.getX( ) + this.drop.getSpawnRadius( ), this.location.getY( ) + 2, this.location.getZ( ) + this.drop.getSpawnRadius( ) );
    }

    @Override
    public Arena getArena( ) {
        return arena;
    }

    @Override
    public Team getTeam( ) {
        return this.team;
    }

    @Override
    public BoundingBox getBoundingBox( ) {
        return this.box;
    }

    @Override
    public int getLevel( ) {
        return this.level;
    }

    @Override
    public DropType.Tier getTier( ) {
        return this.tier;
    }

    @Override
    public Instant getLastUpgrade( ) {
        return this.lastUpgrade;
    }

    @Override
    public ParticleBuilder getParticle( ) {
        return this.particle;
    }

    @Override
    public boolean exists( ) {
        return this.registered && this.location != null;
    }

    @Override
    public boolean remove( ) {
        return false;
    }

}
