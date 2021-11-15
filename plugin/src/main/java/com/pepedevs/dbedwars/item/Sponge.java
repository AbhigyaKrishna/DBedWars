package com.pepedevs.dbedwars.item;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.task.SpongeAnimationTask;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class Sponge extends PluginActionItem {

    private final DBedwars plugin;
    private final ConfigurableCustomItems.ConfigurableSponge cfgSponge;
    private final FixedMetadataValue spongeMeta =
            new FixedMetadataValue(DBedwars.getInstance(), true);

    public Sponge(DBedwars plugin) {
        super(
                StringUtils.translateAlternateColorCodes(
                        plugin.getConfigHandler().getCustomItems().getSponge().getDisplayName()),
                (plugin.getConfigHandler().getCustomItems().getSponge().getLore() == null
                        ? new ArrayList<>()
                        : plugin.getConfigHandler().getCustomItems().getSponge().getLore()),
                XMaterial.SPONGE.parseMaterial());
        this.plugin = plugin;
        this.cfgSponge = plugin.getConfigHandler().getCustomItems().getSponge();
    }

    @Override
    public void onActionPerform(
            Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {}

    public void onSpongePlace(BlockPlaceEvent event) {
        event.getBlock().setMetadata("isBedwarsSponge", spongeMeta);
        if (cfgSponge.isAnimationEnabled())
            plugin.getThreadHandler()
                    .addAsyncWork(
                            new SpongeAnimationTask(
                                    plugin,
                                    event.getBlock(),
                                    cfgSponge.getRadiusForParticles(),
                                    cfgSponge.shouldRemoveSpongeOnAnimationEnd(),
                                    cfgSponge.getSoundBoxIncrease(),
                                    cfgSponge.getSoundOnAnimationEnd()));
    }

    public void onSpongeBreak(BlockBreakEvent event) {
        if (!cfgSponge.isBreakingAllowed()
                && event.getBlock().hasMetadata("isBedwarsSponge")
                && event.getBlock().getMetadata("isBedwarsSponge").contains(spongeMeta)) {
            event.setCancelled(true);
            if (cfgSponge.getBreakTryMessage() != null
                    && !cfgSponge.getBreakTryMessage().trim().equals(""))
                event.getPlayer()
                        .sendMessage(
                                StringUtils.translateAlternateColorCodes(
                                        cfgSponge.getBreakTryMessage()));
        }
    }
}
