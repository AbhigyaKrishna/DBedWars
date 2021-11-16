package com.pepedevs.dbedwars.item;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.math.VectorUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TNTItem extends PluginActionItem {

    private final ConfigurableCustomItems.ConfigurableTNT cfgTNT;
    private final ConfigurableCustomItems.KnockBack cfgKB;
    public static final FixedMetadataValue tntPrimedMeta =
            new FixedMetadataValue(DBedwars.getInstance(), true);

    public TNTItem(DBedwars plugin) {
        super(
                StringUtils.translateAlternateColorCodes(
                        plugin.getConfigHandler().getCustomItems().getTNT().getName()),
                StringUtils.translateAlternateColorCodes(
                        plugin.getConfigHandler().getCustomItems().getTNT().getLore() == null
                                ? new ArrayList<>()
                                : plugin.getConfigHandler().getCustomItems().getTNT().getLore()),
                Material.TNT);
        this.cfgTNT = plugin.getConfigHandler().getCustomItems().getTNT();
        this.cfgKB = cfgTNT.getKnockBack();
    }

    @Override
    public void onActionPerform(
            Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {}

    public void onTNTPlace(BlockPlaceEvent event) {
        if (!cfgTNT.isAutoIgniteTNTEnabled()) {
            return;
        }
        event.getBlockPlaced().setType(Material.AIR);
        TNTPrimed tntPrimed =
                event.getPlayer()
                        .getWorld()
                        .spawn(
                                event.getBlockPlaced().getLocation().add(0.5, 0, 0.5),
                                TNTPrimed.class);
        tntPrimed.setFuseTicks(cfgTNT.getFuseTicks());
        if (!cfgTNT.isBetterTNTAnimationEnabled()) {
            return;
        }
        tntPrimed.setVelocity(
                VectorUtils.rotateAroundAxisY(
                        new Vector(0.01, 0.40, 0.01), new Random().nextInt(360)));
        tntPrimed.setMetadata("isDBedwarsTNT", tntPrimedMeta);
    }

    public void onTNTExplode(EntityExplodeEvent event) {
        if (cfgTNT.isFixRandomExplosionEnabled()) {
            event.getEntity()
                    .teleport(
                            event.getEntity()
                                    .getLocation()
                                    .clone()
                                    .getBlock()
                                    .getLocation()
                                    .add(0.5, 0, 0.5));
        }
        double radius = cfgTNT.getKnockBack().getRadiusEntities();
        List<Entity> nearbyEntities =
                (List<Entity>)
                        event.getLocation()
                                .getWorld()
                                .getNearbyEntities(event.getLocation(), radius, radius, radius);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity)
                giveKB((LivingEntity) entity, event.getLocation(), event);
        }
    }

    private void giveKB(LivingEntity player, Location fbl, EntityExplodeEvent e) {
        if (!cfgKB.isEnabled()) {
            return;
        }
        Location loc = player.getLocation();
        double distance = (e.getYield() * cfgTNT.getKnockBack().getDistanceModifier());
        distance *= 1.0D;
        double hf = cfgTNT.getKnockBack().getHeightForce() / 2.0D;
        double rf = cfgTNT.getKnockBack().getHorizontalForce() / 2.0D;
        player.setVelocity(
                fbl.toVector().subtract(loc.toVector()).normalize().multiply(-1.0D * rf).setY(hf));
        if (player instanceof org.bukkit.entity.Player) {
            EntityDamageEvent DamageEvent =
                    new EntityDamageEvent(
                            player,
                            EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                            distance - loc.distance(player.getLocation()));
            Bukkit.getPluginManager().callEvent(DamageEvent);
            if (!DamageEvent.isCancelled()) player.damage(DamageEvent.getFinalDamage());
        }
    }
}