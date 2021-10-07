package me.abhigya.dbedwars.guis.setup;

import me.Abhigya.core.menu.inventory.Item;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.Abhigya.core.menu.inventory.custom.book.BookItemMenu;
import me.Abhigya.core.menu.inventory.item.action.ActionItem;
import me.Abhigya.core.menu.inventory.item.action.ItemAction;
import me.Abhigya.core.menu.inventory.item.action.ItemActionPriority;
import me.Abhigya.core.menu.inventory.item.voidaction.VoidActionItem;
import me.Abhigya.core.menu.inventory.size.ItemMenuSize;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.util.gui.IMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SetupStartGui extends IMenu< BookItemMenu > {

    Item voidBar = new VoidActionItem( "", XMaterial.WHITE_STAINED_GLASS_PANE.parseItem( ) );
    private ActionItem newArena = new ActionItem( StringUtils.translateAlternateColorCodes( "&5Create new arena!" ),
            XMaterial.EMERALD.parseItem( ) );

    public SetupStartGui( DBedwars plugin ) {
        super( plugin, "START_SETUP", new BookItemMenu( StringUtils.translateAlternateColorCodes( "Select arena to configure" ),
                ItemMenuSize.SIX_LINE, ItemMenuSize.ONE_LINE, null ) );
        newArena.addAction( new ItemAction( ) {
            @Override
            public ItemActionPriority getPriority( ) {
                return ItemActionPriority.NORMAL;
            }

            @Override
            public void onClick( ItemClickAction itemClickAction ) {
                SetupStartGui.this.getPlugin( ).getGuiHandler( ).getAnvilGuis( ).get( "ARENA_NAME_SETUP" ).open( itemClickAction.getPlayer( ), itemClickAction, null );
                itemClickAction.setClose( true );
            }
        } );
    }

    @Override
    protected void setUpMenu( Player player, @Nullable ItemClickAction action, @Nullable Map< String, Object > info ) {
        this.menu.clear( );
        if ( this.getPlugin( ).getGameManager( ).getArenas( ).isEmpty( ) ) {

            for ( byte b = 0; b < 45; b++ ) {
                if ( b == 22 ) {
                    this.menu.addItem( newArena );
                    continue;
                }

                this.menu.addItem( VOID_ITEM );
            }

            for ( byte b = 0; b < 9; b++ ) {
                this.menu.setBarButton( b, voidBar );
            }
            return;
        }

        for ( byte b = 0; b < 9; b++ ) {
            if ( b == 2 ) {
                this.menu.setBarButton( b, newArena );
            } else if ( b == 6 ) {
                this.menu.setBarButton( b, PREVIOUS_PAGE );
            } else if ( b == 7 ) {
                this.menu.setBarButton( b, NEXT_PAGE );
            } else {
                this.menu.setBarButton( b, voidBar );
            }
        }

        for ( Map.Entry< String, Arena > e : this.getPlugin( ).getGameManager( ).getArenas( ).entrySet( ) ) {
            ActionItem item;
            if ( e.getValue( ).isEnabled( ) ) {
                item = new ActionItem( StringUtils.translateAlternateColorCodes( "&a" + e.getKey( ) ), XMaterial.LIME_DYE.parseItem( ) );
            } else {
                if ( e.getValue( ).isConfigured( ) )
                    item = new ActionItem( StringUtils.translateAlternateColorCodes( "&7" + e.getKey( ) ), XMaterial.GRAY_DYE.parseItem( ) );
                else
                    item = new ActionItem( StringUtils.translateAlternateColorCodes( "&c" + e.getKey( ) ), XMaterial.RED_DYE.parseItem( ) );
            }
            item.addAction( new ItemAction( ) {
                @Override
                public ItemActionPriority getPriority( ) {
                    return ItemActionPriority.NORMAL;
                }

                @Override
                public void onClick( ItemClickAction itemClickAction ) {
                    Map< String, Object > info = new HashMap<>( );
                    info.put( "arena", e.getKey( ) );
                    SetupStartGui.this.getPlugin( ).getGuiHandler( ).getGuis( ).get( "TYPE_SETUP" ).open( itemClickAction, info, itemClickAction.getPlayer( ) );
                }
            } );

            this.menu.addItem( item );
        }
    }

}
