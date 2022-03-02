package org.zibble.dbedwars.item;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.zibble.dbedwars.configuration.language.ConfigLang;

import java.util.ArrayList;
import java.util.List;

public class TNTItem extends BedWarsActionItem {

    public static final FixedMetadataValue TNT_PRIMED_META = new FixedMetadataValue(DBedwars.getInstance(), true);
    private final ConfigurableCustomItems.ConfigurableTNT cfgTNT;
    private final ConfigurableCustomItems.ConfigurableKnockBack cfgKB;

    public TNTItem(DBedwars plugin) {
        super(
                ConfigLang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getTNT().getName()),
                ConfigLang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getTNT().getLore() == null ? new ArrayList<>()
                        : plugin.getConfigHandler().getCustomItems().getTNT().getLore()),
                Material.TNT);
        this.cfgTNT = plugin.getConfigHandler().getCustomItems().getTNT();
        this.cfgKB = cfgTNT.getKnockBack();
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {}

    public void onTNTPlace(BlockPlaceEvent event) {

    }

    public void onTNTExplode(EntityExplodeEvent event) {
        if (cfgTNT.isFixRandomExplosionEnabled()) {
            event.getEntity().teleport(event.getEntity().getLocation().getBlock().getLocation().add(0.5, 0, 0.5));
        }
        double radius = cfgTNT.getKnockBack().getRadiusEntities();
        List<Entity> nearbyEntities = (List<Entity>) event.getLocation().getWorld().getNearbyEntities(event.getLocation(), radius, radius, radius);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) giveKB((LivingEntity) entity, event.getLocation(), event);
        }
    }

    private void giveKB(LivingEntity player, Location fbl, EntityExplodeEvent e) {
        if (!cfgKB.isEnabled()) return;
        Location loc = player.getLocation();
        double distance = (e.getYield() * cfgTNT.getKnockBack().getDistanceModifier());
        distance *= 1.0D;
        double hf = cfgTNT.getKnockBack().getHeightForce() / 2.0D;
        double rf = cfgTNT.getKnockBack().getHorizontalForce() / 2.0D;
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
    public Key<String> getKey() {
        return Key.of("TNT");
    }

}
