package com.pepedevs.dbedwars.listeners;

import com.pepedevs.radium.utils.PluginHandler;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ArenaListener extends PluginHandler {

    private final Arena arena;

    public ArenaListener(DBedwars plugin, Arena arena) {
        super(plugin);
        this.arena = arena;
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        if (this.arena.getWorld().equals(event.getBlock().getWorld())) event.setCancelled(true);
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;

        if (this.arena.getWorld().equals(event.getBlock().getWorld())) event.setCancelled(true);
    }

    @EventHandler
    public void handleEntitySpawn(EntitySpawnEvent event) {
        if (event.isCancelled()) return;

        if (this.arena.getWorld().equals(event.getEntity().getWorld())) {
            if (event.getEntity() instanceof Player) return;
            else if (event.getEntity() instanceof Item) return;
            else if (event.getEntity() instanceof ArmorStand) return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;

        if (this.arena.getWorld().equals(event.getEntity().getWorld())) event.setCancelled(true);
    }

    @EventHandler
    public void handleItemDrop(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;

        if (this.arena.getWorld().equals(event.getPlayer().getWorld())) event.setCancelled(true);
    }

    @Override
    protected boolean isAllowMultipleInstances() {
        return true;
    }

    @Override
    protected boolean isSingleInstanceForAllPlugin() {
        return true;
    }

    @Override
    public void register() {
        super.register();
    }

    @Override
    public void unregister() {
        super.unregister();
    }
}
