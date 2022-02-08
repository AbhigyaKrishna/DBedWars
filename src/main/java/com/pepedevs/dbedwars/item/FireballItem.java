package com.pepedevs.dbedwars.item;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.feature.custom.FireballLaunchFeature;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.utils.Utils;
import com.pepedevs.radium.events.EventUtils;
import com.pepedevs.radium.item.ActionItem;
import com.pepedevs.radium.utils.Acceptor;
import com.pepedevs.radium.utils.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FireballItem extends PluginActionItem {

    public static final FixedMetadataValue FIREBALL_META =
            new FixedMetadataValue(DBedwars.getInstance(), DBedwars.getInstance().getName());
    private final DBedwars plugin;

    // TODO DAMAGE SETTINGS TO BE ADDED

    public FireballItem(DBedwars plugin) {
        super(
                Lang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getFireball().getDisplayName()),
                Lang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getFireball().getLore() == null ? new ArrayList<>()
                        : plugin.getConfigHandler().getCustomItems().getFireball().getLore()),
                XMaterial.FIRE_CHARGE.parseMaterial());
        this.plugin = plugin;
    }

    @Override
    public void onActionPerform(Player player, ActionItem.EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
        ConfigurableCustomItems.ConfigurableFireball cfgFireball = this.plugin.getConfigHandler().getCustomItems().getFireball();;
        Arena arena = this.plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena == null) return;
        Optional<ArenaPlayer> optionalArenaPlayer = arena.getAsArenaPlayer(player);
        if (!optionalArenaPlayer.isPresent()) return;
        ArenaPlayer arenaPlayer = optionalArenaPlayer.get();
        if (EventUtils.isRightClick(playerInteractEvent.getAction()) || (cfgFireball.isLeftClickThrowEnabled() && EventUtils.isClickingBlock(playerInteractEvent.getAction()))) {
            playerInteractEvent.setCancelled(true);
            Utils.useItem(player);
            Fireball fireball = player.launchProjectile(Fireball.class);
            fireball.setMetadata("isDBedwarsFireball", FIREBALL_META);
            this.plugin.getFeatureManager().runFeature(BedWarsFeatures.FIREBALL_LAUNCH_FEATURE, FireballLaunchFeature.class, new Acceptor<FireballLaunchFeature>() {
                @Override
                public boolean accept(FireballLaunchFeature feature) {
                    feature.launch(fireball, arenaPlayer);
                    return true;
                }
            });
        }
    }

    public void onFireBallExplode(EntityExplodeEvent e) {
        ConfigurableCustomItems.ConfigurableKnockBack cfgKB = this.plugin.getConfigHandler().getCustomItems().getFireball().getKnockBack();
        Location l = e.getLocation();
        double radius = cfgKB.getRadiusEntities();
        List<Entity> nearbyEntities = (List<Entity>) l.getWorld().getNearbyEntities(l, radius, radius, radius);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) giveKB((LivingEntity) entity, l, e);
        }
    }

    private void giveKB(LivingEntity entity, Location fbl, EntityExplodeEvent e) {
        ConfigurableCustomItems.ConfigurableKnockBack cfgKB = this.plugin.getConfigHandler().getCustomItems().getFireball().getKnockBack();
        if (!cfgKB.isEnabled()) return;
        Location loc = entity.getLocation();
        double distance = (e.getYield() * cfgKB.getDistanceModifier());
        distance *= 1.0D;
        double hf = cfgKB.getHeightForce() / 0.9;
        double rf = cfgKB.getHorizontalForce() / 2.7;
        entity.setVelocity(fbl.toVector().subtract(loc.toVector()).normalize().multiply(-1.0D * rf).setY(hf));
        if (entity instanceof Player) {
            EntityDamageEvent DamageEvent = new EntityDamageEvent(
                    entity,
                    EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                    distance - loc.distance(entity.getLocation())
            );
            Bukkit.getPluginManager().callEvent(DamageEvent);
            if (!DamageEvent.isCancelled()) entity.damage(DamageEvent.getFinalDamage());
        }
    }
}
