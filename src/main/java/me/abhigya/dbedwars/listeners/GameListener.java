package me.abhigya.dbedwars.listeners;

import me.Abhigya.core.handler.PluginHandler;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.events.game.PlayerBaseEnterEvent;
import me.abhigya.dbedwars.api.events.game.PlayerBaseExitEvent;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.DeathCause;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.spawner.Spawner;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.api.util.TrapEnum;
import me.abhigya.dbedwars.item.CustomItems;
import me.abhigya.dbedwars.item.TNTItem;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;
import java.util.Optional;

public class GameListener extends PluginHandler {

    private final DBedwars plugin;
    private final Arena arena;

    public GameListener( DBedwars plugin, Arena arena ) {
        super( plugin );
        this.plugin = plugin;
        this.arena = arena;
    }

    @EventHandler
    public void handleItemMerge( ItemMergeEvent event ) {
        if ( !event.getEntity( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        if ( Utils.isUnMergeable( event.getEntity( ).getItemStack( ) ) )
            event.setCancelled( true );
    }

    @EventHandler
    public void handleBlockBreak( BlockBreakEvent event ) {
        if ( !event.getBlock( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        ArenaPlayer player = this.arena.getAsArenaPlayer( event.getPlayer( ) ).orElse( null );

        if ( player == null ) {
            event.setCancelled( true );
            return;
        }

        if ( player.isSpectator( ) || player.getTeam( ) == null ) {
            event.setCancelled( true );
            return;
        }

        if ( event.getBlock( ).hasMetadata( "placed" ) ) {
            // This should not happen but anyways
            if ( !player.getArena( ).getSettings( ).getName( ).equals( ( (FixedMetadataValue) event.getBlock( ).getMetadata( "placed" ).get( 0 ) ).value( ) ) )
                event.setCancelled( true );

        } else if ( Utils.isBed( event.getBlock( ) ) ) {
            event.setCancelled( true );

            Optional< Team > oTeam = this.arena.getTeams( ).stream( ).filter( t -> {
                Block block = t.getBedLocation( ).getBlock( this.arena.getWorld( ) );
                return ( block.equals( event.getBlock( ) ) || block.equals( event.getBlock( ).getRelative( BlockFace.EAST ) ) ||
                        block.equals( event.getBlock( ).getRelative( BlockFace.WEST ) ) || block.equals( event.getBlock( ).getRelative( BlockFace.NORTH ) ) ||
                        block.equals( event.getBlock( ).getRelative( BlockFace.SOUTH ) ) );
            } ).findFirst( );

            if ( !oTeam.isPresent( ) )
                return;

            Team team = oTeam.get( );

            if ( player.getTeam( ).equals( team ) ) {
                // TODO: change message
                player.sendMessage( StringUtils.translateAlternateColorCodes( "&cYou cannot destroy your own bed!" ) );
                return;
            }

            this.arena.destroyBed( player, team );
        } else {
            //TODO: change message
            player.sendMessage( StringUtils.translateAlternateColorCodes( "&cYou can only destroy blocks placed by players." ) );
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void handleBlockPlace( BlockPlaceEvent event ) {
        if ( !event.getBlock( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        ArenaPlayer player = this.arena.getAsArenaPlayer( event.getPlayer( ) ).orElse( null );

        if ( player == null ) {
            event.setCancelled( true );
            return;
        }

        Block block = event.getBlock( );
        block.setMetadata( "placed", new FixedMetadataValue( this.plugin, this.arena.getSettings( ).getName( ) ) );

        if ( block.getType( ) == Material.TNT ) {
            ( (TNTItem) CustomItems.TNT.getItem( ) ).onTNTPlace( event );
        }
    }

    @EventHandler
    public void handlePlayerKill( EntityDamageEvent event ) {
        if ( !( event.getEntity( ) instanceof Player ) )
            return;

        if ( !event.getEntity( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        ArenaPlayer player = this.arena.getAsArenaPlayer( (Player) event.getEntity( ) ).orElse( null );
        if ( player == null )
            return;

        if ( ( (Player) event.getEntity( ) ).getHealth( ) - event.getFinalDamage( ) <= 0 ) {
            event.setCancelled( true );

            player.kill( DeathCause.ATTACK );
        }
    }

    @EventHandler
    public void handlePlayerDamageTag( EntityDamageByEntityEvent event ) {
        if ( !( event.getEntity( ) instanceof Player ) || !( event.getDamager( ) instanceof Player ) )
            return;

        if ( !event.getEntity( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        ArenaPlayer player = this.arena.getAsArenaPlayer( (Player) event.getEntity( ) ).orElse( null );
        if ( player == null )
            return;

        this.arena.getAsArenaPlayer( (Player) event.getDamager( ) ).ifPresent( player::setLastHitTag );
    }

    // TODO: revamp this
    @EventHandler
    public void handlePlayerHunger( FoodLevelChangeEvent event ) {
        if ( !event.getEntity( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        if ( this.arena.getSettings( ).isDisableHunger( ) )
            event.setCancelled( true );
    }

    @EventHandler
    public void handleCrafting( CraftItemEvent event ) {
        if ( !event.getView( ).getPlayer( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        event.getRecipe( ).getResult( ).setType( XMaterial.AIR.parseMaterial( ) );
        event.setCancelled( true );
    }

    @EventHandler
    public void handlePickup( PlayerPickupItemEvent event ) {
        if ( !event.getPlayer( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        if ( !this.arena.isArenaPlayer( event.getPlayer( ) ) ) {
            event.setCancelled( true );
            return;
        }

        if ( event.getItem( ).hasMetadata( "split" ) ) {
            Optional< Spawner > spawner = this.arena.getSpawner( LocationXYZ.valueOf( event.getItem( ).getLocation( ) ), 1 );
            spawner.ifPresent( s -> {
                Collection< Entity > entities = this.arena.getWorld( ).getNearbyEntities( s.getLocation( ),
                        s.getDropType( ).getSpawnRadius( ), s.getDropType( ).getSpawnRadius( ), s.getDropType( ).getSpawnRadius( ) );
                entities.remove( event.getPlayer( ) );
                for ( Entity entity : entities ) {
                    if ( entity instanceof Player && this.arena.isArenaPlayer( (Player) entity ) ) {
                        ( (Player) entity ).getInventory( ).addItem( event.getItem( ).getItemStack( ) );
                    }
                }
            } );
        }
    }

    @EventHandler
    public void handleInventoryAdd( InventoryPickupItemEvent event ) {
        if ( !event.getItem( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        if ( Utils.hasNBTData( event.getItem( ).getItemStack( ), "split" ) ) {
            ItemStack item = Utils.removeNBTData( event.getItem( ).getItemStack( ), "split" );
            event.setCancelled( true );
            event.getItem( ).remove( );
            event.getInventory( ).addItem( item );
        }
    }

    @EventHandler( priority = EventPriority.LOW )
    public void handlePlayerFall( PlayerMoveEvent event ) {
        if ( !event.getPlayer( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        if ( event.getPlayer( ).getLocation( ).getY( ) <= this.arena.getSettings( ).getMinYAxis( ) ) {
            this.arena.getAsArenaPlayer( event.getPlayer( ) ).ifPresent( p -> {
                p.kill( DeathCause.VOID );
            } );

        }
    }

    @EventHandler( priority = EventPriority.HIGH, ignoreCancelled = true )
    public void handlePlayerMove( PlayerMoveEvent event ) {
        if ( !event.getPlayer( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        if ( event.getFrom( ).getBlockX( ) == event.getTo( ).getBlockX( ) && event.getFrom( ).getBlockZ( ) == event.getTo( ).getBlockZ( ) )
            return;

        this.arena.getAsArenaPlayer( event.getPlayer( ) ).ifPresent( p -> {
            for ( Team team : this.arena.getTeams( ) ) {
                if ( team.getIslandArea( ).contains( event.getTo( ).toVector( ) ) && !team.getIslandArea( ).contains( event.getFrom( ).toVector( ) ) ) {
                    PlayerBaseEnterEvent e = new PlayerBaseEnterEvent( p, this.arena, team );
                    e.call( );

                    if ( !e.getTeam( ).getTrapQueue( ).isEmpty( ) ) {
                        if ( e.getPlayer( ).getTeam( ).equals( e.getTeam( ) ) )
                            e.getTeam( ).triggerTrap( TrapEnum.TriggerType.TEAMMATE_BASE_ENTER_EVENT, e.getPlayer( ) );
                        else
                            e.getTeam( ).triggerTrap( TrapEnum.TriggerType.ENEMY_BASE_ENTER_EVENT, e.getPlayer( ) );
                    }

                    break;
                } else if ( !team.getIslandArea( ).contains( event.getTo( ).toVector( ) ) && team.getIslandArea( ).contains( event.getFrom( ).toVector( ) ) ) {
                    PlayerBaseExitEvent e = new PlayerBaseExitEvent( p, this.arena, team );
                    e.call( );

                    if ( !e.getTeam( ).getTrapQueue( ).isEmpty( ) ) {
                        if ( e.getPlayer( ).getTeam( ).equals( e.getTeam( ) ) )
                            e.getTeam( ).triggerTrap( TrapEnum.TriggerType.TEAMMATE_BASE_EXIT_EVENT, e.getPlayer( ) );
                        else
                            e.getTeam( ).triggerTrap( TrapEnum.TriggerType.ENEMY_BASE_EXIT_EVENT, e.getPlayer( ) );
                    }

                    break;
                }
            }
        } );
    }

    @EventHandler
    public void handleEntitySpawn( EntitySpawnEvent event ) {
        if ( !event.getLocation( ).getWorld( ).equals( this.arena.getWorld( ) ) )
            return;

        if ( event.getEntity( ) instanceof Player || event.getEntity( ) instanceof Item || event.getEntity( ).hasMetadata( "spawnable" ) )
            return;

        event.setCancelled( true );
    }

    @Override
    protected boolean isAllowMultipleInstances( ) {
        return true;
    }

    @Override
    public void register( ) {
        super.register( );
    }

    @Override
    public void unregister( ) {
        super.unregister( );
    }

}
