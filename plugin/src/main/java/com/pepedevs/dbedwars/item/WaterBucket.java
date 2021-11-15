package com.pepedevs.dbedwars.item;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class WaterBucket extends PluginActionItem {

    private final boolean removeOnUse;

    public WaterBucket(DBedwars plugin) {
        super(
                StringUtils.translateAlternateColorCodes(
                        plugin.getConfigHandler()
                                .getCustomItems()
                                .getWaterBucket()
                                .getDisplayName()),
                ((DBedwars.getInstance()
                                        .getConfigHandler()
                                        .getCustomItems()
                                        .getWaterBucket()
                                        .getLore()
                                == null
                        ? new ArrayList<>()
                        : DBedwars.getInstance()
                                .getConfigHandler()
                                .getCustomItems()
                                .getWaterBucket()
                                .getLore())),
                XMaterial.WATER_BUCKET.parseMaterial());
        this.removeOnUse =
                plugin.getConfigHandler().getCustomItems().getWaterBucket().shouldRemoveOnUse();
    }

    @Override
    public void onActionPerform(
            Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {}

    public void onWaterBucketUse(PlayerBucketEmptyEvent event) {
        if (removeOnUse) Utils.useItem(event.getPlayer());
    }
}
