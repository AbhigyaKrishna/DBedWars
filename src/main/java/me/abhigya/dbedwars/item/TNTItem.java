package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.math.VectorUtils;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TNTItem extends PluginActionItem {

    ConfigurableCustomItems.ConfigurableTNT tnt;

    public TNTItem( DBedwars plugin ) {
        super( plugin, StringUtils.translateAlternateColorCodes( plugin.getConfigHandler( ).getCustomItems( ).getTNT( ).getName( ) ), StringUtils.translateAlternateColorCodes( plugin.getConfigHandler( ).getCustomItems( ).getTNT( ).getLore( ) == null ? new ArrayList<>( ) : plugin.getConfigHandler( ).getCustomItems( ).getTNT( ).getLore( ) ), Material.TNT );
        this.tnt = plugin.getConfigHandler( ).getCustomItems( ).getTNT( );
    }

    @Override
    public void onActionPerform( Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent ) {

    }

    public void onTNTPlace( BlockPlaceEvent event ) {

        //TODO PLACE A CHECK TO MAKE SURE THE TNT IS OUT TNT BOUGHT FROM SHOP OR GOTTEN BY COMMAND

        if ( !tnt.isAutoIgniteTNTEnabled( ) ) {
            return;
        }
        event.getBlockPlaced( ).setType( Material.AIR );
        TNTPrimed tntPrimed = event.getPlayer( ).getWorld( ).spawn( event.getBlockPlaced( ).getLocation( ).add( 0.5, 0, 0.5 ), TNTPrimed.class );
        tntPrimed.setFuseTicks( tnt.getFuseTicks( ) );
        if ( !tnt.isBetterTNTAnimationEnabled( ) ) {
            return;
        }
        tntPrimed.setVelocity( VectorUtils.rotateAroundAxisY( new Vector( 0.01, 0.20, 0.01 ), new Random( ).nextInt( 360 ) ) );

    }

    public void onTNTExplode( EntityExplodeEvent event ) {
        if ( event.getEntityType( ) != EntityType.PRIMED_TNT ) {
            return;
        }
        if ( tnt.isFixRandomExplosionEnabled( ) ) {
            event.getEntity( ).teleport( event.getEntity( ).getLocation( ).clone( ).getBlock( ).getLocation( ).add( 0.5, 0, 0.5 ) );
        }
        double radius = tnt.getKnockBack( ).getRadiusEntities( );
        List< Entity > nearbyEntities = (List< Entity >) event.getLocation( ).getWorld( ).getNearbyEntities( event.getLocation( ), radius, radius, radius );
        for ( Entity entity : nearbyEntities ) {
            if ( entity instanceof LivingEntity )
                pushAway( (LivingEntity) entity, event.getLocation( ), event );
        }
    }

    void pushAway( LivingEntity player, Location fbl, EntityExplodeEvent e ) {
        Location loc = player.getLocation( );

        double distance = ( e.getYield( ) * tnt.getKnockBack( ).getDistanceModifier( ) );
        distance *= 1.0D;
        double hf = tnt.getKnockBack( ).getHeightForce( ) / 2.0D;
        double rf = tnt.getKnockBack( ).getHorizontalForce( ) / 2.0D;
        player.setVelocity( fbl.toVector( ).subtract( loc.toVector( ) ).normalize( ).multiply( -1.0D * rf ).setY( hf ) );
        if ( player instanceof org.bukkit.entity.Player ) {
            EntityDamageEvent DamageEvent = new EntityDamageEvent( player, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, distance - loc.distance( player.getLocation( ) ) );
            Bukkit.getPluginManager( ).callEvent( DamageEvent );
            if ( !DamageEvent.isCancelled( ) )
                player.damage( DamageEvent.getFinalDamage( ) );
        }
    }

}
