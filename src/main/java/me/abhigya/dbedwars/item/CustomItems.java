package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum CustomItems {

    //TODO BLACKLIST THESE 2 ITEMS

    SIMPLE_SETUP_ITEM( new PluginActionItem( DBedwars.getInstance( ), StringUtils.translateAlternateColorCodes( "&bSimple Arena Setup" ),
            StringUtils.translateAlternateColorCodes( new ArrayList<>( Collections.singletonList( "&7Right click to view arena settings panel." ) ) ),
            XMaterial.NETHER_STAR.parseMaterial( ) ) {
        @Override
        public void onActionPerform( Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent ) {
            Map< String, Object > info = new HashMap<>( );
            info.put( "arena", DBedwars.getInstance( ).getGameManager( ).getArena( Utils.getValue( playerInteractEvent.getItem( ),
                    "arena", String.class ) ) );
            info.put( "page", 1 );
            if ( enumAction == EnumAction.RIGHT_CLICK || enumAction == EnumAction.RIGHT_CLICK_SNEAKING || enumAction == EnumAction.RIGHT_CLICK_SPRINTING )
                DBedwars.getInstance( ).getGuiHandler( ).getGuis( ).get( "SIMPLE_SETUP" ).open( null, info, player );
        }
    } ),

    ADVANCED_SETUP_ITEM( new PluginActionItem( DBedwars.getInstance( ), StringUtils.translateAlternateColorCodes( "&bAdvanced Arena Setup" ),
            StringUtils.translateAlternateColorCodes( new ArrayList<>( Collections.singletonList( "&7Right click to view arena settings panel." ) ) ),
            XMaterial.NETHER_STAR.parseMaterial( ) ) {
        @Override
        public void onActionPerform( Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent ) {
            Map< String, Object > info = new HashMap<>( );
            info.put( "arena", DBedwars.getInstance( ).getGameManager( ).getArena( Utils.getValue( playerInteractEvent.getItem( ),
                    "arena", String.class ) ) );
            info.put( "page", 1 );
            if ( enumAction == EnumAction.RIGHT_CLICK || enumAction == EnumAction.RIGHT_CLICK_SNEAKING || enumAction == EnumAction.RIGHT_CLICK_SPRINTING )
                DBedwars.getInstance( ).getGuiHandler( ).getGuis( ).get( "ADVANCED_SETUP" ).open( null, info, player );
        }
    } ),

    FIREBALL( new FireballItem( DBedwars.getInstance( ) ) ),

    TNT( new TNTItem( DBedwars.getInstance( ) ) ),
    BRIDGE_EGG( new BridgeEgg( DBedwars.getInstance( ) ) )
    /*POPUP_TOWER(new PopupTowerChest(DBedwars.getInstance()))*/;

    private final PluginActionItem item;

    CustomItems( PluginActionItem item ) {
        this.item = item;
    }

    public PluginActionItem getItem( ) {
        return item;
    }
}
