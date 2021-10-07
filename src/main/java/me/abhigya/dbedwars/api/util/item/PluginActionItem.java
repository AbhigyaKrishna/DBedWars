package me.abhigya.dbedwars.api.util.item;

import me.Abhigya.core.item.ActionItemBase;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public abstract class PluginActionItem extends ActionItemBase {

    private final DBedwars plugin;

    public PluginActionItem( DBedwars plugin, String display_name, Collection< String > lore, Material material ) {
        super( display_name, lore, material );
        this.plugin = plugin;
    }

    @Override
    public ItemStack toItemStack( ) {
        return this.toBwItemStack( ).toItemStack( );
    }

    public BwItemStack toBwItemStack( ) {
        return new BwItemStack( super.toItemStack( ) );
    }

    @Override
    public boolean isThis( ItemStack item ) {
        return super.isThis( item ) && Utils.hasPluginData( item );
    }

}
