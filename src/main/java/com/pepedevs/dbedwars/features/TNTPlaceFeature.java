package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.item.TNTItem;
import com.pepedevs.radium.utils.math.VectorUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import java.util.Random;

public class TNTPlaceFeature extends com.pepedevs.dbedwars.api.feature.custom.TNTPlaceFeature {

    private final DBedwars plugin;

    public TNTPlaceFeature(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    @Override
    public void onPlace(Block block, ArenaPlayer placer) {
        ConfigurableCustomItems.ConfigurableTNT cfgTNT = this.plugin.getConfigHandler().getCustomItems().getTNT();
        if (!cfgTNT.isAutoIgniteTNTEnabled()) return;
        block.setType(Material.AIR);
        TNTPrimed tntPrimed = placer.getPlayer().getWorld().spawn(block.getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
        tntPrimed.setFuseTicks(cfgTNT.getFuseTicks());
        if (!cfgTNT.isBetterTNTAnimationEnabled()) return;
        tntPrimed.setVelocity(VectorUtils.rotateAroundAxisY(new Vector(0.01, 0.40, 0.01), new Random().nextInt(360)));
        tntPrimed.setMetadata("isDBedwarsTNT", TNTItem.TNT_PRIMED_META);
    }

    @Override
    public boolean isInitialized() {
        return false;
    }
}
