package com.pepedevs.dbedwars.item;

import com.pepedevs.corelib.events.EventUtils;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.PotionEffectAT;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.utils.FireBallUtil;
import com.pepedevs.dbedwars.utils.Utils;
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

public class FireballItem extends PluginActionItem {

    public static final FixedMetadataValue fireballMeta =
            new FixedMetadataValue(DBedwars.getInstance(), DBedwars.getInstance().getName());
    private final ConfigurableCustomItems.ConfigurableFireball cfgFireball;
    private final ConfigurableCustomItems.KnockBack cfgKB;

    // TODO DAMAGE SETTINGS TO BE ADDED

    public FireballItem(DBedwars plugin) {
        super(
                StringUtils.translateAlternateColorCodes(
                        plugin.getConfigHandler().getCustomItems().getFireball().getDisplayName()),
                StringUtils.translateAlternateColorCodes(
                        plugin.getConfigHandler().getCustomItems().getFireball().getLore() == null
                                ? new ArrayList<>()
                                : plugin.getConfigHandler()
                                        .getCustomItems()
                                        .getFireball()
                                        .getLore()),
                XMaterial.FIRE_CHARGE.parseMaterial());
        this.cfgFireball = plugin.getConfigHandler().getCustomItems().getFireball();
        this.cfgKB = cfgFireball.getKnockBack();
    }

    @Override
    public void onActionPerform(
            Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
        if (EventUtils.isRightClick(playerInteractEvent.getAction())
                || (cfgFireball.isLeftClickThrowEnabled()
                        && EventUtils.isClickingBlock(playerInteractEvent.getAction()))) {
            playerInteractEvent.setCancelled(true);
            Utils.useItem(player);
            Fireball fireball = player.launchProjectile(Fireball.class);
            fireball.setMetadata("isDBedwarsFireball", fireballMeta);
            fireball.setVelocity(fireball.getVelocity().multiply(cfgFireball.getSpeedMultiplier()));
            if (this.cfgFireball.isFixDirectionEnabled())
                FireBallUtil.setDirection(fireball, player.getEyeLocation().getDirection());
            fireball.setYield(cfgFireball.getExplosionYield());
            cfgFireball
                    .getPotionEffects()
                    .forEach(
                            s -> {
                                PotionEffectAT effect = PotionEffectAT.valueOf(s);
                                if (effect != null) effect.applyTo(player);
                            });
            fireball.setIsIncendiary(cfgFireball.isExplosionFireEnabled());
        }
    }

    public void onFireBallExplode(EntityExplodeEvent e) {
        Location l = e.getLocation();
        double radius = cfgKB.getRadiusEntities();
        List<Entity> nearbyEntities =
                (List<Entity>) l.getWorld().getNearbyEntities(l, radius, radius, radius);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) giveKB((LivingEntity) entity, l, e);
        }
    }

    private void giveKB(LivingEntity player, Location fbl, EntityExplodeEvent e) {
        if (!cfgKB.isEnabled()) {
            return;
        }
        Location loc = player.getLocation();
        double distance = (e.getYield() * cfgKB.getDistanceModifier());
        distance *= 1.0D;
        double hf = cfgKB.getHeightForce() / 0.9;
        double rf = cfgKB.getHorizontalForce() / 2.7;
        player.setVelocity(
                fbl.toVector().subtract(loc.toVector()).normalize().multiply(-1.0D * rf).setY(hf));
        if (player instanceof Player) {
            EntityDamageEvent DamageEvent =
                    new EntityDamageEvent(
                            player,
                            EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                            distance - loc.distance(player.getLocation()));
            Bukkit.getPluginManager().callEvent(DamageEvent);
            if (!DamageEvent.isCancelled()) player.damage(DamageEvent.getFinalDamage());
        }
    }
}
