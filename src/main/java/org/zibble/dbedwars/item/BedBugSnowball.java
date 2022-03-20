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
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.util.Acceptor;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.EventUtils;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.utils.Util;

import java.util.ArrayList;
import java.util.Optional;

public class BedBugSnowball extends BedWarsActionItem {

    public static final FixedMetadataValue BED_BUG_BALL_META = new FixedMetadataValue(DBedwars.getInstance(), true);
    private final DBedwars plugin;

    public BedBugSnowball(DBedwars plugin) {
        super(ConfigLang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getBedBug().getName()),
                ConfigLang.getTranslator().translate(
                        plugin.getConfigHandler().getCustomItems().getBedBug().getLore() == null ? new ArrayList<>()
                        : plugin.getConfigHandler().getCustomItems().getBedBug().getLore()),
                XMaterial.SNOWBALL.parseMaterial());
        this.plugin = plugin;
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
        if (!EventUtils.isRightClick(playerInteractEvent.getAction())) return;
        Arena arena = plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena == null) return;
        Optional<ArenaPlayer> optionalArenaPlayer = arena.getAsArenaPlayer(player);
        if (!optionalArenaPlayer.isPresent()) return;
        ArenaPlayer arenaPlayer = optionalArenaPlayer.get();
        //TODO SPECTATOR CHECK
        playerInteractEvent.setCancelled(true);
        Snowball bedBugBall = player.launchProjectile(Snowball.class);
        bedBugBall.setMetadata("isDBedWarsBedBugBall", BED_BUG_BALL_META);
        bedBugBall.setMetadata("thrower", new FixedMetadataValue(plugin, arenaPlayer.getName()));
        Util.removeItem(player, this.toItemStack());
    }

    public void onLand(ProjectileHitEvent event) {
        Entity bedBugBall = event.getEntity();
        Silverfish bedBug = bedBugBall.getWorld().spawn(bedBugBall.getLocation().clone().add(0, 1, 0), Silverfish.class);
        ArenaPlayer thrower = null;
        String name = bedBugBall.getMetadata("thrower").get(0).asString();
        for (ArenaPlayer player : this.plugin.getGameManager().getArena(bedBugBall.getWorld().getName()).getPlayers()) {
            if (player.getName().equals(name)) {
                thrower = player;
                break;
            }
        }
        if (thrower == null) return;
        final ArenaPlayer finalThrower = thrower;
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.BED_BUG_CHASE_FEATURE, BedBugChaseFeature.class, new Acceptor<BedBugChaseFeature>() {
            @Override
            public boolean accept(BedBugChaseFeature feature) {
                feature.startChase(bedBug, finalThrower);
                return true;
            }
        });
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.BED_BUG_DISPLAY_NAME_UPDATE_FEATURE, BedBugDisplayNameUpdateFeature.class, new Acceptor<BedBugDisplayNameUpdateFeature>() {
            @Override
            public boolean accept(BedBugDisplayNameUpdateFeature feature) {
                feature.start(bedBug, finalThrower);
                return true;
            }
        });
    }

    @Override
    public Key<String> getKey() {
        return Key.of("BED_BUG");
    }

}
