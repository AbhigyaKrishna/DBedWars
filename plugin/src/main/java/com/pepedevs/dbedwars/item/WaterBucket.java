package com.pepedevs.dbedwars.item;

import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.utils.Utils;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.ArrayList;

public class WaterBucket extends PluginActionItem {

    private final boolean removeOnUse;

    public WaterBucket(DBedwars plugin) {
        super(Lang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getWaterBucket().getDisplayName()),
                Lang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getWaterBucket().getLore()
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
