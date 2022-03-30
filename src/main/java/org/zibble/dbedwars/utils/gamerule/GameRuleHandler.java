package org.zibble.dbedwars.utils.gamerule;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.plugin.PluginHandler;

public class GameRuleHandler extends PluginHandler {

    public GameRuleHandler(DBedwars plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isPositivePresent(GameRuleType.DISABLE_BLOCK_PLACING, event.getBlock().getWorld())
                && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (isPositivePresent(GameRuleType.DISABLE_BLOCK_BREAKING, event.getBlock().getWorld())
                && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (isPositivePresent(GameRuleType.INVULNERABLE_ENTITIES, event.getEntity().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPvP(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if (isPositivePresent(GameRuleType.DISALLOW_PVP, event.getEntity().getWorld())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHunger(FoodLevelChangeEvent event) {
        if (isPositivePresent(GameRuleType.DISABLE_HUNGER, event.getEntity().getWorld())) {
            event.setCancelled(true);
        }
    }

    protected boolean isPositivePresent(GameRuleType type, World world) {
        return type.getGameRuleMetadata(world) != null
                && type.getGameRuleMetadata(world).asBoolean();
    }

    @Override
    protected boolean isAllowMultipleInstances() {
        return false;
    }

}
