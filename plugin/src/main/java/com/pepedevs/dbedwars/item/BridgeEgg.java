package com.pepedevs.dbedwars.item;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.feature.custom.BridgeEggBuildFeature;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.ArenaStatus;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.radium.events.EventUtils;
import com.pepedevs.radium.utils.Acceptor;
import com.pepedevs.radium.utils.xseries.XMaterial;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Optional;

public class BridgeEgg extends PluginActionItem {

    public static final FixedMetadataValue BRIDGE_EGG_META =
            new FixedMetadataValue(DBedwars.getInstance(), true);
    private final DBedwars plugin;

    public BridgeEgg(DBedwars plugin) {
        super(Lang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getBridgeEgg().getName()),
                Lang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getBridgeEgg().getLore() == null ? new ArrayList<>()
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
        BwItemStack.removeItem(player, this.toItemStack());
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

    // TODO IDK HOW TO BLOCK CHICKEN SPAWNS HERE (UPDATE MEIN KARLEGE)

}
