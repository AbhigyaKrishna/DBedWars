package org.zibble.dbedwars.item;

import com.pepedevs.radium.utils.xseries.XMaterial;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.configuration.Lang;
import org.zibble.dbedwars.utils.Utils;

import java.util.ArrayList;

public class WaterBucket extends BedWarsActionItem {

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

    @Override
    public Key<String> getKey() {
        return Key.of("WATER_BUCKET");
    }

}