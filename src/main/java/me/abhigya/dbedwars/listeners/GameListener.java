package me.abhigya.dbedwars.listeners;

import me.Abhigya.core.handler.PluginHandler;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.DeathCause;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Optional;

public class GameListener extends PluginHandler {

    private final DBedwars plugin;
    private final Arena arena;

    public GameListener(DBedwars plugin, Arena arena) {
        super(plugin);
        this.plugin = plugin;
        this.arena = arena;
        this.register();
    }

    @EventHandler
    public void handleItemMerge(ItemMergeEvent event) {
        if (!event.getEntity().getWorld().equals(this.arena.getWorld()))
            return;

        if (Utils.isUnMergeable(event.getEntity().getItemStack()))
            event.setCancelled(true);
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        if (!event.getBlock().getWorld().equals(this.arena.getWorld()))
            return;

        ArenaPlayer player = this.arena.getAsArenaPlayer(event.getPlayer()).orElse(null);

        if (player == null) {
            event.setCancelled(true);
            return;
        }

        if (player.isSpectator() || player.getTeam() == null) {
            event.setCancelled(true);
            return;
        }

        if (event.getBlock().hasMetadata("placed")) {
            // This should not happen but anyways
            if (!player.getArena().equals(this.arena) || !player.getArena().getSettings().getName().equals(((FixedMetadataValue) event.getBlock().getMetadata("placed").get(0)).value()))
                event.setCancelled(true);

        } else if (Utils.isBed(event.getBlock())) {
            event.setCancelled(true);

            Optional<Team> oTeam = this.arena.getTeams().stream().filter(t -> {
                LocationXYZ loc = LocationXYZ.valueOf(event.getBlock().getLocation());
                return  (t.getBedLocation().equals(loc) || t.getBedLocation().equals(loc.clone().addX(1)) ||
                        t.getBedLocation().equals(loc.clone().addZ(1)) || t.getBedLocation().equals(loc.clone().subtractX(1)) ||
                        t.getBedLocation().equals(loc.clone().subtractZ(1)));
            }).findFirst();

            if (!oTeam.isPresent())
                return;

            Team team = oTeam.get();

            if (player.getTeam().equals(team)) {
                // TODO: change message
                player.sendMessage(StringUtils.translateAlternateColorCodes("&cYou cannot destroy your own bed!"));
                return;
            }

            this.arena.destroyBed(player, team);
        } else {
            //TODO: change message
            player.sendMessage(StringUtils.translateAlternateColorCodes("&cYou can only destroy blocks placed by players."));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        if (!event.getBlock().getWorld().equals(this.arena.getWorld()))
            return;

        Block block = event.getBlock();
        block.setMetadata("placed", new FixedMetadataValue(this.plugin, this.arena.getSettings().getName()));
    }

    @EventHandler
    public void handlePlayerKill(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!event.getEntity().getWorld().equals(this.arena.getWorld()))
            return;

        ArenaPlayer player = this.arena.getAsArenaPlayer((Player) event.getEntity()).orElse(null);
        if (player == null)
            return;

        if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);

            player.kill(DeathCause.ATTACK);
        }
    }

    @EventHandler
    public void handlePlayerDamageTag(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;

        if (!event.getEntity().getWorld().equals(this.arena.getWorld()))
            return;

        ArenaPlayer player = this.arena.getAsArenaPlayer((Player) event.getEntity()).orElse(null);
        if (player == null)
            return;

        this.arena.getAsArenaPlayer((Player) event.getDamager()).ifPresent(player::setLastHitTag);
    }

    // TODO: revamp this
    @EventHandler
    public void handlePlayerHunger(FoodLevelChangeEvent event) {
        if (!event.getEntity().getWorld().equals(this.arena.getWorld()))
            return;

        if (this.plugin.getMainConfiguration().getArenaSection().isDisableHunger())
            event.setCancelled(true);
    }

    @EventHandler
    public void handleCrafting(CraftItemEvent event) {
        if (!event.getView().getPlayer().getWorld().equals(this.arena.getWorld()))
            return;

        event.getRecipe().getResult().setType(XMaterial.AIR.parseMaterial());
        event.setCancelled(true);
    }

//    @EventHandler
//    public void handlePickup(EntityPickupItemEvent event) {
//        if (!event.getEntity().getWorld().equals(this.arena.getWorld()))
//            return;
//
//        if (!(event.getEntity() instanceof Player))
//            return;
//    }

    @EventHandler
    public void handlePlayerFall(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().equals(this.arena.getWorld()))
            return;

        if (event.getPlayer().getLocation().getY() <= this.plugin.getMainConfiguration().getArenaSection().getMinYAxis()) {
            ArenaPlayer player = this.arena.getAsArenaPlayer(event.getPlayer()).orElse(null);
            if (player == null)
                return;

            player.kill(DeathCause.VOID);
        }
    }

    @EventHandler
    public void handleEntitySpawn(EntitySpawnEvent event) {
        if (!event.getLocation().getWorld().equals(this.arena.getWorld()))
            return;

        if (event.getEntity() instanceof Player || event.getEntity() instanceof Item || event.getEntity().hasMetadata("spawnable"))
            return;

        event.setCancelled(true);
    }

    @Override
    protected boolean isAllowMultipleInstances() {
        return true;
    }

    @Override
    public void unregister() {
        super.unregister();
    }
}
