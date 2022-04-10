package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.BridgeEggBuildFeature;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.ArenaStatus;
import org.zibble.dbedwars.api.util.EventUtils;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.utils.Util;

import java.util.Optional;

public class BridgeEgg extends BedWarsActionItem {

    public static final FixedMetadataValue BRIDGE_EGG_META =
            new FixedMetadataValue(DBedwars.getInstance(), true);
    private final DBedwars plugin;

    public BridgeEgg(DBedwars plugin) {
        super(ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getBridgeEgg().getName()),
                plugin.getConfigHandler().getCustomItems().getBridgeEgg().getLore() == null ? null
                        : ConfigMessage.from((plugin.getConfigHandler().getCustomItems().getBridgeEgg().getLore())),
                XMaterial.EGG);
        this.plugin = plugin;
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent event) {
        if (!EventUtils.isRightClick(event.getAction())) {
            return;
        }
        Optional<? extends ArenaPlayer> optionalArenaPlayer = this.plugin.getGameManager().getArenaPlayer(player);
        if (!optionalArenaPlayer.isPresent()) return;
        ArenaPlayer arenaPlayer = optionalArenaPlayer.get();
        if (arenaPlayer.getArena().getStatus() != ArenaStatus.RUNNING) return;
        event.setCancelled(true);
        Util.removeItem(player, this.asItemStack());
        Egg egg = player.launchProjectile(Egg.class);
        egg.setMetadata("isDBedwarsEgg", BRIDGE_EGG_META);
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.BRIDGE_EGG_BUILD_FEATURE, BridgeEggBuildFeature.class, bridgeEggBuildFeature -> {
            bridgeEggBuildFeature.startBuilding(egg, arenaPlayer);
            return true;
        });
    }

    @Override
    public Key getKey() {
        return Key.of("BRIDGE_EGG");
    }

    // TODO IDK HOW TO BLOCK CHICKEN SPAWNS HERE (UPDATE MEIN KARLEGE)

}
