package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
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
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.FireballLaunchFeature;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.util.Acceptor;
import org.zibble.dbedwars.api.util.EventUtils;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FireballItem extends BedWarsActionItem {

    public static final FixedMetadataValue FIREBALL_META =
            new FixedMetadataValue(DBedwars.getInstance(), DBedwars.getInstance().getName());
    private final DBedwars plugin;

    // TODO DAMAGE SETTINGS TO BE ADDED

    public FireballItem(DBedwars plugin) {
        super(ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getFireball().getName()),
                plugin.getConfigHandler().getCustomItems().getFireball().getLore() == null ? null
                        : ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getFireball().getLore()),
                XMaterial.FIRE_CHARGE);
        this.plugin = plugin;
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
        ConfigurableCustomItems.ConfigurableFireball cfgFireball = this.plugin.getConfigHandler().getCustomItems().getFireball();;
        Arena arena = this.plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena == null) return;
        Optional<ArenaPlayer> optionalArenaPlayer = arena.getAsArenaPlayer(player);
        if (!optionalArenaPlayer.isPresent()) return;
        ArenaPlayer arenaPlayer = optionalArenaPlayer.get();
        if (EventUtils.isRightClick(playerInteractEvent.getAction()) || (cfgFireball.isLeftClickThrowEnabled() && EventUtils.isClickingBlock(playerInteractEvent.getAction()))) {
            playerInteractEvent.setCancelled(true);
            Util.useItem(player);
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
        ConfigurableCustomItems.ConfigurableKnockBack cfgKB = this.plugin.getConfigHandler().getCustomItems().getFireball().getKnockback();
        Location l = e.getLocation();
        double radius = cfgKB.getRadiusEntities();
        List<Entity> nearbyEntities = (List<Entity>) l.getWorld().getNearbyEntities(l, radius, radius, radius);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) giveKB((LivingEntity) entity, l, e);
        }
    }

    private void giveKB(LivingEntity entity, Location fbl, EntityExplodeEvent e) {
        ConfigurableCustomItems.ConfigurableKnockBack cfgKB = this.plugin.getConfigHandler().getCustomItems().getFireball().getKnockback();
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

    @Override
    public Key<String> getKey() {
        return Key.of("FIREBALL");
    }

}
