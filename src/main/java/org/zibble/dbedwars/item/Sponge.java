package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.SpongePlaceFeature;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.util.function.Acceptor;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.zibble.dbedwars.messaging.Messaging;

import java.util.Optional;

public class Sponge extends BedWarsActionItem {

    public static final Key KEY = Key.of("SPONGE");

    private final DBedwars plugin;
    private final ConfigurableCustomItems.ConfigurableSponge cfgSponge;
    private final FixedMetadataValue SPONGE_META =
            new FixedMetadataValue(DBedwars.getInstance(), true);

    public Sponge(DBedwars plugin) {
        super(ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getSponge().getName()),
                plugin.getConfigHandler().getCustomItems().getSponge().getLore() == null ? null
                        : ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getSponge().getLore()),
                XMaterial.SPONGE);
        this.plugin = plugin;
        this.cfgSponge = plugin.getConfigHandler().getCustomItems().getSponge();
    }

    @Override
    public void onActionPerform(
            Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
    }

    public void onSpongePlace(BlockPlaceEvent event) {
        event.getBlock().setMetadata("isBedwarsSponge", SPONGE_META);
        Player player = event.getPlayer();
        Optional<? extends ArenaPlayer> optionalArenaPlayer = this.plugin.getGameManager().getArenaPlayer(player);
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
                Messaging.getInstance().getMessagingMember(event.getPlayer()).sendMessage(ConfigMessage.from(cfgSponge.getBreakTryMessage()));
        }
    }

    @Override
    public Key getKey() {
        return KEY;
    }

}
