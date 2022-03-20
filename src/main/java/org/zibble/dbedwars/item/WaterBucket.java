package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.utils.Util;

public class WaterBucket extends BedWarsActionItem {

    private final boolean removeOnUse;

    public WaterBucket(DBedwars plugin) {
        super(ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getWaterBucket().getName()),
                plugin.getConfigHandler().getCustomItems().getWaterBucket().getLore() == null ? null
                        : ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getWaterBucket().getLore()),
                XMaterial.WATER_BUCKET);
        this.removeOnUse =
                plugin.getConfigHandler().getCustomItems().getWaterBucket().shouldRemoveOnUse();
    }

    public void onWaterBucketUse(PlayerBucketEmptyEvent event) {
        if (removeOnUse) Util.useItem(event.getPlayer());
    }

    @Override
    public Key<String> getKey() {
        return Key.of("WATER_BUCKET");
    }

}
