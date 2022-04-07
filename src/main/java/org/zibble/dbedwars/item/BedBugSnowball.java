package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.BedBugChaseFeature;
import org.zibble.dbedwars.api.feature.custom.BedBugDisplayNameUpdateFeature;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.util.EventUtils;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.utils.Util;

import java.util.Optional;

public class BedBugSnowball extends BedWarsActionItem {

    public static final Key KEY = Key.of("BED_BUG");

    public static final FixedMetadataValue BED_BUG_BALL_META = new FixedMetadataValue(DBedwars.getInstance(), true);
    private final DBedwars plugin;

    public BedBugSnowball(DBedwars plugin) {
        super(ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getBedBug().getName()),
                plugin.getConfigHandler().getCustomItems().getBedBug().getLore() == null ? null :
                        ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getBedBug().getLore()),
                XMaterial.SNOWBALL);
        this.plugin = plugin;
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
        if (!EventUtils.isRightClick(playerInteractEvent.getAction())) return;
        Optional<? extends ArenaPlayer> optionalArenaPlayer = this.plugin.getGameManager().getArenaPlayer(player);
        if (!optionalArenaPlayer.isPresent()) return;
        ArenaPlayer arenaPlayer = optionalArenaPlayer.get();
        //TODO SPECTATOR CHECK
        playerInteractEvent.setCancelled(true);
        Snowball bedBugBall = player.launchProjectile(Snowball.class);
        bedBugBall.setMetadata("isDBedWarsBedBugBall", BED_BUG_BALL_META);
        bedBugBall.setMetadata("thrower", new FixedMetadataValue(plugin, arenaPlayer.getName()));
        Util.removeItem(player, this.asItemStack());
    }

    public void onLand(ProjectileHitEvent event) {
        Entity bedBugBall = event.getEntity();
        Silverfish bedBug = bedBugBall.getWorld().spawn(bedBugBall.getLocation().clone().add(0, 1, 0), Silverfish.class);
        ArenaPlayer thrower = null;
        String name = bedBugBall.getMetadata("thrower").get(0).asString();
        for (ArenaPlayer player : this.plugin.getGameManager().getArena(bedBugBall.getWorld()).getPlayers()) {
            if (player.getName().equals(name)) {
                thrower = player;
                break;
            }
        }
        if (thrower == null) return;
        final ArenaPlayer finalThrower = thrower;
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.BED_BUG_CHASE_FEATURE, BedBugChaseFeature.class, feature -> {
            feature.startChase(bedBug, finalThrower);
            return true;
        });
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.BED_BUG_DISPLAY_NAME_UPDATE_FEATURE, BedBugDisplayNameUpdateFeature.class, feature -> {
            feature.start(bedBug, finalThrower);
            return true;
        });
    }

    @Override
    public Key getKey() {
        return KEY;
    }

}
