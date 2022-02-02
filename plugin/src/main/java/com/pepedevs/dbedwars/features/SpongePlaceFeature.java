package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.task.SpongeAnimationTask;
import org.bukkit.block.Block;

public class SpongePlaceFeature extends com.pepedevs.dbedwars.api.feature.custom.SpongePlaceFeature {

    private final DBedwars plugin;

    public SpongePlaceFeature(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    @Override
    public void onPlace(Block sponge, ArenaPlayer placer) {
        ConfigurableCustomItems.ConfigurableSponge cfgSponge = this.plugin.getConfigHandler().getCustomItems().getSponge();
        if (cfgSponge.isAnimationEnabled()) {
            plugin.getThreadHandler().submitAsync(new SpongeAnimationTask(
                    plugin,
                    sponge,
                    cfgSponge.getRadiusForParticles(),
                    cfgSponge.shouldRemoveSpongeOnAnimationEnd(),
                    cfgSponge.getSoundBoxIncrease(),
                    cfgSponge.getSoundOnAnimationEnd())
            );
        }
    }

    @Override
    public boolean isInitialized() {
        return false;
    }
}
