package me.abhigya.dbedwars.api.util.gui;

import me.Abhigya.core.menu.anvil.AnvilMenu;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.abhigya.dbedwars.DBedwars;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class IAnvilMenu {

    private final DBedwars plugin;
    private final String identifier;
    protected AnvilMenu menu;

    protected IAnvilMenu( DBedwars plugin, String identifier, AnvilMenu menu ) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.menu = menu;
        this.menu.registerListener( plugin );
    }

    protected abstract void setUpMenu( Player player, @Nullable ItemClickAction action, @Nullable Map< String, Object > info );

    public void open( Player player, @Nullable ItemClickAction action, @Nullable Map< String, Object > info ) {
        this.setUpMenu( player, action, info );
        this.menu.open( player );
    }

    public DBedwars getPlugin( ) {
        return plugin;
    }

    public String getIdentifier( ) {
        return identifier;
    }

    public AnvilMenu getMenu( ) {
        return menu;
    }

}
