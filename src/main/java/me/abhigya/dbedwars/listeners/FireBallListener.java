package me.abhigya.dbedwars.listeners;

import me.Abhigya.core.handler.PluginHandler;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class FireBallListener extends PluginHandler {

    private final double radiusEntities;
    private final double distanceModifier;
    private final double heightForce;
    private final double horizontalForce;

    public FireBallListener(Plugin plugin) {
        super(plugin);
        ConfigurableCustomItems.KnockBack fbkb = DBedwars.getInstance().getConfigHandler().getCustomItems().getFireball().getKnockBack();
        this.radiusEntities = fbkb.getRadiusEntities();
        this.distanceModifier = fbkb.getDistanceModifier();
        this.heightForce = fbkb.getHeightForce();
        this.horizontalForce = fbkb.getHorizontalForce();
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntityType() != EntityType.FIREBALL)
            return;
        if (!e.getEntity().hasMetadata("plugin"))
            return;
        if (!e.getEntity().getMetadata("plugin").contains(new FixedMetadataValue(DBedwars.getInstance(),DBedwars.getInstance().getName())))
            return;
        Location l = e.getLocation();
        double radius = radiusEntities;
        List<Entity> nearbyEntities = (List<Entity>)l.getWorld().getNearbyEntities(l, radius, radius, radius);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity)
                pushAway((LivingEntity)entity, l, e);
        }
    }

    private void pushAway(LivingEntity player, Location fbl, EntityExplodeEvent e) {
        Location loc = player.getLocation();
        double distance = (e.getYield() * distanceModifier);
        distance *= 1.0D;
        double hf = heightForce /1.5;
        double rf = horizontalForce /1.5;
        player.setVelocity(fbl.toVector().subtract(loc.toVector()).normalize().multiply(-1.0D * rf).setY(hf));
        if (player instanceof Player) {
            EntityDamageEvent DamageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, distance - loc.distance(player.getLocation()));
            Bukkit.getPluginManager().callEvent(DamageEvent);
            if (!DamageEvent.isCancelled())
                player.damage(DamageEvent.getFinalDamage());
        }
    }

    @Override
    public void register() {
        super.register();
    }

    @Override
    public void unregister() {
        super.unregister();
    }

    @Override
    protected boolean isAllowMultipleInstances() {
        return false;
    }
}
