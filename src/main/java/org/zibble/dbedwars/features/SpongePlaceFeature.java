package org.zibble.dbedwars.features;

import org.bukkit.block.Block;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.zibble.dbedwars.task.implementations.SpongeAnimationTask;

public class SpongePlaceFeature extends org.zibble.dbedwars.api.feature.custom.SpongePlaceFeature {

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
                    cfgSponge.getSoundOnBoxIncrease(),
                    cfgSponge.getSoundOnEnd())
            );
        }
    }

    @Override
    public boolean isInitialized() {
        return false;
    }
}
