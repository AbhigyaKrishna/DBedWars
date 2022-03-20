package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.BridgeEggBuildFeature;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.ArenaStatus;
import org.zibble.dbedwars.api.util.Acceptor;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.EventUtils;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.utils.Util;

import java.util.ArrayList;
import java.util.Optional;

public class BridgeEgg extends BedWarsActionItem {

    public static final FixedMetadataValue BRIDGE_EGG_META =
            new FixedMetadataValue(DBedwars.getInstance(), true);
    private final DBedwars plugin;

    public BridgeEgg(DBedwars plugin) {
        super(ConfigLang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getBridgeEgg().getName()),
                ConfigLang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getBridgeEgg().getLore() == null ? new ArrayList<>()
                        : plugin.getConfigHandler().getCustomItems().getBridgeEgg().getLore()),
                XMaterial.EGG.parseMaterial());
        this.plugin = plugin;
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent event) {
        if (!EventUtils.isRightClick(event.getAction())) {
            return;
        }
        if (!plugin.getGameManager().containsArena(player.getWorld().getName())) return;
        Arena arena = plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena.getStatus() != ArenaStatus.RUNNING) return;
        Optional<ArenaPlayer> optionalArenaPlayer = arena.getAsArenaPlayer(player);
        if (!optionalArenaPlayer.isPresent()) return;
        ArenaPlayer arenaPlayer = optionalArenaPlayer.get();
        //TODO USELESS SPECTATOR CHECK :P
        event.setCancelled(true);
        Util.removeItem(player, this.toItemStack());
        Egg egg = player.launchProjectile(Egg.class);
        egg.setMetadata("isDBedwarsEgg", BRIDGE_EGG_META);
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.BRIDGE_EGG_BUILD_FEATURE, BridgeEggBuildFeature.class, new Acceptor<BridgeEggBuildFeature>() {
            @Override
            public boolean accept(BridgeEggBuildFeature bridgeEggBuildFeature) {
                bridgeEggBuildFeature.startBuilding(egg, arenaPlayer);
                return true;
            }
        });
    }

    @Override
    public Key<String> getKey() {
        return Key.of("BRIDGE_EGG");
    }

    // TODO IDK HOW TO BLOCK CHICKEN SPAWNS HERE (UPDATE MEIN KARLEGE)

}
