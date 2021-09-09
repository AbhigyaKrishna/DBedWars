package me.abhigya.dbedwars.nms;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSAdaptor {

    void clearRegionFileCache(World world);

    void clearChunkCache(World world);

    void refreshPlayerChunk(Player player);

    void setMetadata(ItemStack item, String metadata, Object value);

    boolean hasMetadata(ItemStack item, String metadata);

    Object getMetadata(ItemStack item, String metadata);

    default void setPluginMetaValue(ItemStack item) {
        this.setMetadata(item, "plugin", "dbedwars");
    }

    default boolean hasPluginMetaValue(ItemStack item) {
        return this.hasMetadata(item, "plugin") && this.getMetadata(item, "plugin").equals("dbedwars");
    }

}
