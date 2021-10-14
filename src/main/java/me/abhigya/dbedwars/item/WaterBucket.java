package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class WaterBucket extends PluginActionItem{

    private final boolean removeOnUse;

    public WaterBucket(DBedwars plugin) {
        super(plugin, StringUtils.translateAlternateColorCodes(plugin.getConfigHandler().getCustomItems().getWaterBucket().getDisplayName()),
                ((DBedwars.getInstance().getConfigHandler().getCustomItems().getWaterBucket().getLore() == null ? new ArrayList<>() : DBedwars.getInstance().getConfigHandler().getCustomItems().getWaterBucket().getLore())),
                XMaterial.WATER_BUCKET.parseMaterial());
        this.removeOnUse = plugin.getConfigHandler().getCustomItems().getWaterBucket().shouldRemoveOnUse();
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
    }

    public void onWaterBucketUse(PlayerBucketEmptyEvent event){
        if (removeOnUse)
            Utils.useItem( event.getPlayer( ) );
    }

}
