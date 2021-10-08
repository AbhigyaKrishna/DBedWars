package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class WaterBucket extends PluginActionItem{
    public WaterBucket(DBedwars plugin) {
        super(plugin, StringUtils.translateAlternateColorCodes(plugin.getConfigHandler().getCustomItems().getWaterBucket().getDisplayName()),
                ((DBedwars.getInstance().getConfigHandler().getCustomItems().getWaterBucket().getLore() == null ? new ArrayList<>() : DBedwars.getInstance().getConfigHandler().getCustomItems().getWaterBucket().getLore())),
                XMaterial.WATER_BUCKET.parseMaterial());
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
    }

    public void onWaterBucketUse(PlayerBucketEmptyEvent event){
        if (!event.getItemStack().isSimilar(this.toItemStack()))
            return;
        event.getPlayer().getInventory().setItem(event.getPlayer().getInventory().getHeldItemSlot(),new ItemStack(Material.AIR));
    }

}
