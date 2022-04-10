package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;

import java.util.List;

public class TNTItem extends BedWarsActionItem {

    public static final Key KEY = Key.of("TNT");

    public static final FixedMetadataValue TNT_PRIMED_META = new FixedMetadataValue(DBedwars.getInstance(), true);
    private final ConfigurableCustomItems.ConfigurableTNT cfgTNT;
    private final ConfigurableCustomItems.ConfigurableKnockBack cfgKB;

    public TNTItem(DBedwars plugin) {
        super(ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getTnt().getName()),
                plugin.getConfigHandler().getCustomItems().getTnt().getLore() == null ? null
                        : ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getTnt().getLore()),
                XMaterial.TNT);
        this.cfgTNT = plugin.getConfigHandler().getCustomItems().getTnt();
        this.cfgKB = cfgTNT.getKnockback();
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
    }

    public void onTNTPlace(BlockPlaceEvent event) {

    }

    public void onTNTExplode(EntityExplodeEvent event) {
        if (cfgTNT.isFixRandomExplosionEnabled()) {
            event.getEntity().teleport(event.getEntity().getLocation().getBlock().getLocation().add(0.5, 0, 0.5));
        }
        double radius = cfgTNT.getKnockback().getRadiusEntities();
        List<Entity> nearbyEntities = (List<Entity>) event.getLocation().getWorld().getNearbyEntities(event.getLocation(), radius, radius, radius);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) giveKB((LivingEntity) entity, event.getLocation(), event);
        }
    }

    private void giveKB(LivingEntity player, Location fbl, EntityExplodeEvent e) {
        if (!cfgKB.isEnabled()) return;
        Location loc = player.getLocation();
        double distance = (e.getYield() * cfgTNT.getKnockback().getDistanceModifier());
        distance *= 1.0D;
        double hf = cfgTNT.getKnockback().getHeightForce() / 2.0D;
        double rf = cfgTNT.getKnockback().getHorizontalForce() / 2.0D;
        player.setVelocity(fbl.toVector().subtract(loc.toVector()).normalize().multiply(-1.0D * rf).setY(hf));
        if (player instanceof Player) {
            EntityDamageEvent DamageEvent =
                    new EntityDamageEvent(
                            player,
                            EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                            distance - loc.distance(player.getLocation()));
            Bukkit.getPluginManager().callEvent(DamageEvent);
            if (!DamageEvent.isCancelled()) player.damage(DamageEvent.getFinalDamage());
        }
    }

    @Override
    public Key getKey() {
        return KEY;
    }

}
