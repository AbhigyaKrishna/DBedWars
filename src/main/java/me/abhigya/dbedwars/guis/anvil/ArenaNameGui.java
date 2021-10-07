package me.abhigya.dbedwars.guis.anvil;

import me.Abhigya.core.menu.anvil.AnvilItem;
import me.Abhigya.core.menu.anvil.AnvilMenu;
import me.Abhigya.core.menu.anvil.action.AnvilItemClickAction;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.util.gui.IAnvilMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ArenaNameGui extends IAnvilMenu {

    public ArenaNameGui( DBedwars plugin ) {
        super( plugin, "ARENA_NAME_SETUP", new AnvilMenu( new AnvilItem( StringUtils.translateAlternateColorCodes( "Enter name" ),
                XMaterial.PAPER.parseItem( ) ) {
            @Override
            public void onClick( AnvilItemClickAction anvilItemClickAction ) {
            }
        }, null ) );

        this.menu.setOutputAction( action -> {
            if ( action.getClickedItem( ).getItemMeta( ) != null && action.getClickedItem( ).getItemMeta( ).hasDisplayName( ) ) {
                String name = action.getClickedItem( ).getItemMeta( ).getDisplayName( );

                if ( ArenaNameGui.this.getPlugin( ).getGameManager( ).containsArena( name ) ) {
                    action.getPlayer( ).sendMessage( StringUtils.translateAlternateColorCodes( "&cArena with this name already exist!" ) );
                } else {
                    Arena arena = ArenaNameGui.this.getPlugin( ).getGameManager( ).createArena( name );
                    arena.saveData( false );

                    Map< String, Object > info = new HashMap<>( );
                    info.put( "arena", arena );
                    info.put( "type", "initial" );

                    ArenaNameGui.this.menu.close( action.getPlayer( ) );
                    ArenaNameGui.this.getPlugin( ).getGuiHandler( ).getGuis( ).get( "MAP_SETUP" ).open( null, info, action.getPlayer( ) );
                }
            } else {
                action.getPlayer( ).sendMessage( StringUtils.translateAlternateColorCodes( "&cAn error occurred while getting name!" ) );
            }
        } );
    }

    @Override
    public void setUpMenu( Player player, ItemClickAction action, @Nullable Map< String, Object > info ) {
    }

}
