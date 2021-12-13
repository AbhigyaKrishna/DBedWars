package com.pepedevs.dbedwars.item;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.utils.Utils;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

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
                StringUtils.translateAlternateColorCodes(
                        plugin.getConfigHandler().getCustomItems().getWaterBucket().getLore()
                                        == null
                                ? new ArrayList<>()
                                : plugin.getConfigHandler()
                                        .getCustomItems()
                                        .getWaterBucket()
                                        .getLore()),
                XMaterial.WATER_BUCKET.parseMaterial());
        this.removeOnUse =
                plugin.getConfigHandler().getCustomItems().getWaterBucket().shouldRemoveOnUse();
    }

    public void onWaterBucketUse(PlayerBucketEmptyEvent event) {
        if (removeOnUse) Utils.useItem(event.getPlayer());
    }
}
