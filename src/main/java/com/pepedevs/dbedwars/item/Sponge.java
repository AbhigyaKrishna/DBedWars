package com.pepedevs.dbedwars.item;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.feature.custom.SpongePlaceFeature;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.item.BedWarsActionItem;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.radium.item.ActionItem;
import com.pepedevs.radium.utils.Acceptor;
import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.radium.utils.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Optional;

public class Sponge extends BedWarsActionItem {

    private final DBedwars plugin;
    private final ConfigurableCustomItems.ConfigurableSponge cfgSponge;
    private final FixedMetadataValue SPONGE_META =
            new FixedMetadataValue(DBedwars.getInstance(), true);

    public Sponge(DBedwars plugin) {
        super(Lang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getSponge().getDisplayName()),
                Lang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getSponge().getLore() == null ? new ArrayList<>()
                        : plugin.getConfigHandler().getCustomItems().getSponge().getLore()),
                XMaterial.SPONGE.parseMaterial());
        this.plugin = plugin;
        this.cfgSponge = plugin.getConfigHandler().getCustomItems().getSponge();
    }

    @Override
    public void onActionPerform(
            Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {}

    public void onSpongePlace(BlockPlaceEvent event) {
        event.getBlock().setMetadata("isBedwarsSponge", SPONGE_META);
        Player player = event.getPlayer();
        Arena arena = this.plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena == null) return;
        Optional<ArenaPlayer> optionalArenaPlayer = arena.getAsArenaPlayer(player);
        if (!optionalArenaPlayer.isPresent()) return;
        ArenaPlayer arenaPlayer = optionalArenaPlayer.get();
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.SPONGE_PLACE_FEATURE, SpongePlaceFeature.class, new Acceptor<SpongePlaceFeature>() {
            @Override
            public boolean accept(SpongePlaceFeature feature) {
                feature.onPlace(event.getBlock(), arenaPlayer);
                return true;
            }
        });
    }

    public void onSpongeBreak(BlockBreakEvent event) {
        if (!cfgSponge.isBreakingAllowed()
                && event.getBlock().hasMetadata("isBedwarsSponge")
                && event.getBlock().getMetadata("isBedwarsSponge").contains(SPONGE_META)) {
            event.setCancelled(true);
            if (cfgSponge.getBreakTryMessage() != null && !cfgSponge.getBreakTryMessage().trim().equals(""))
                event.getPlayer().sendMessage(StringUtils.translateAlternateColorCodes(cfgSponge.getBreakTryMessage()));
        }
    }

    @Override
    public Key<String> getKey() {
        return Key.of("SPONGE");
    }

}
