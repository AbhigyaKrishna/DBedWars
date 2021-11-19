package com.pepedevs.dbedwars.listeners;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.events.PlayerBaseEnterEvent;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.DeathCause;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.spawner.Spawner;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.api.util.NBTUtils;
import com.pepedevs.dbedwars.item.*;
import com.pepedevs.dbedwars.utils.Utils;
import me.Abhigya.core.handler.PluginHandler;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;
import java.util.Optional;

public class GameListener extends PluginHandler {

    private final DBedwars plugin;
    private final Arena arena;

    public GameListener(DBedwars plugin, Arena arena) {
        super(plugin);
        this.plugin = plugin;
        this.arena = arena;
    }

    @EventHandler
    public void handleItemMerge(ItemMergeEvent event) {
        if (!event.getEntity().getWorld().equals(this.arena.getWorld())) return;

        if (Utils.isUnMergeable(event.getEntity().getItemStack())) event.setCancelled(true);
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        if (!event.getBlock().getWorld().equals(this.arena.getWorld())) return;

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
            if (!player.getArena()
                    .getSettings()
                    .getName()
                    .equals(event.getBlock().getMetadata("placed").get(0).value())) {
                event.setCancelled(true);
                return;
            }
            if (event.getBlock().getType() == XMaterial.SPONGE.parseMaterial())
                ((Sponge) ((DBedwars) this.getPlugin()).getCustomItemHandler().getItem("SPONGE"))
                        .onSpongeBreak(event);
        } else if (Utils.isBed(event.getBlock())) {
            event.setCancelled(true);

            Optional<Team> oTeam =
                    this.arena.getTeams().stream()
                            .filter(
                                    t -> {
                                        Block block =
                                                t.getBedLocation().getBlock(this.arena.getWorld());
                                        return (block.equals(event.getBlock())
                                                || block.equals(
                                                        event.getBlock()
                                                                .getRelative(BlockFace.EAST))
                                                || block.equals(
                                                        event.getBlock()
                                                                .getRelative(BlockFace.WEST))
                                                || block.equals(
                                                        event.getBlock()
                                                                .getRelative(BlockFace.NORTH))
                                                || block.equals(
                                                        event.getBlock()
                                                                .getRelative(BlockFace.SOUTH)));
                                    })
                            .findFirst();

            if (!oTeam.isPresent()) return;

            Team team = oTeam.get();

            if (player.getTeam().equals(team)) {
                // TODO: change message
                player.sendMessage(
                        StringUtils.translateAlternateColorCodes(
                                "&cYou cannot destroy your own bed!"));
                return;
            }

            this.arena.destroyBed(player, team);
        } else {
            // TODO: change message
            player.sendMessage(
                    StringUtils.translateAlternateColorCodes(
                            "&cYou can only destroy blocks placed by players."));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        if (!event.getBlock().getWorld().equals(this.arena.getWorld())) return;

        ArenaPlayer player = this.arena.getAsArenaPlayer(event.getPlayer()).orElse(null);

        if (player == null) {
            event.setCancelled(true);
            return;
        }

        Block block = event.getBlock();
        block.setMetadata(
                "placed", new FixedMetadataValue(this.plugin, this.arena.getSettings().getName()));
        // TODO ADDITIONAL CHECK FOR TNT
        if (block.getType() == XMaterial.TNT.parseMaterial()
                && ((DBedwars) this.getPlugin())
                        .getCustomItemHandler()
                        .getItem("TNT")
                        .isThis(player.getPlayer().getItemInHand())) {
            ((TNTItem) ((DBedwars) this.getPlugin()).getCustomItemHandler().getItem("TNT"))
                    .onTNTPlace(event);
        }
        if (block.getType() == XMaterial.SPONGE.parseMaterial()
                && ((DBedwars) this.getPlugin())
                        .getCustomItemHandler()
                        .getItem("SPONGE")
                        .isThis(player.getPlayer().getItemInHand())) {
            ((Sponge) ((DBedwars) this.getPlugin()).getCustomItemHandler().getItem("SPONGE"))
                    .onSpongePlace(event);
        }
        if (block.getType() == XMaterial.TRAPPED_CHEST.parseMaterial()
                && ((DBedwars) this.getPlugin())
                        .getCustomItemHandler()
                        .getItem("POPUP_TOWER")
                        .isThis(player.getPlayer().getItemInHand())) {
            ((PopupTowerChestItem)
                            ((DBedwars) this.getPlugin())
                                    .getCustomItemHandler()
                                    .getItem("POPUP_TOWER"))
                    .onChestPlace(event, player);
        }
        // TODO REFERENCE
        if (NBTUtils.hasNBTData(event.getItemInHand(), "")) {
            ((BlastProofGlass)
                            ((DBedwars) this.getPlugin())
                                    .getCustomItemHandler()
                                    .getItem("BLAST_PROOF_GLASS"))
                    .onPlace(event);
        }
    }

    @EventHandler
    public void handlePlayerKill(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (!event.getEntity().getWorld().equals(this.arena.getWorld())) return;

        ArenaPlayer player = this.arena.getAsArenaPlayer((Player) event.getEntity()).orElse(null);
        if (player == null) return;

        if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);

            player.kill(DeathCause.ATTACK);
        }
    }

    @EventHandler
    public void handleEntityDeath(EntityDeathEvent event) {

        // TODO ADD MORE CHECKS IF NECESSARY

        if (!event.getEntity().getWorld().equals(this.arena.getWorld())) {
            return;
        }

        Entity entity = event.getEntity();

        if (entity.getType() == EntityType.IRON_GOLEM
                && entity.hasMetadata("isDBedwarsGolem")
                && entity.getMetadata("isDBedwarsGolem")
                        .contains(DreamDefenderSpawnEgg.GOLEM_META_VALUE)) {
            ((DreamDefenderSpawnEgg)
                            ((DBedwars) this.getPlugin())
                                    .getCustomItemHandler()
                                    .getItem("DREAM_DEFENDER"))
                    .onDeath(event);
        }
    }

    @EventHandler
    public void handlePlayerDamageTag(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;

        if (!event.getEntity().getWorld().equals(this.arena.getWorld())) return;

        ArenaPlayer player = this.arena.getAsArenaPlayer((Player) event.getEntity()).orElse(null);
        if (player == null) return;

        this.arena.getAsArenaPlayer((Player) event.getDamager()).ifPresent(player::setLastHitTag);
    }

    @EventHandler
    public void handleEntityExplosions(EntityExplodeEvent event) {
        if (event.getEntity() == null
                || !event.getEntity().getWorld().equals(this.arena.getWorld())) return;

        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.FIREBALL) {
            ((BlastProofGlass) plugin.getCustomItemHandler().getItem("BLAST_PROOF_GLASS"))
                    .onFireballExplode(event);
            if (entity.hasMetadata("isDBedwarsFireball")
                    && entity.getMetadata("isDBedwarsFireball").contains(FireballItem.fireballMeta))
                ((FireballItem) plugin.getCustomItemHandler().getItem("FIREBALL"))
                        .onFireBallExplode(event);
        } else if (entity.getType() == EntityType.PRIMED_TNT) {
            ((BlastProofGlass) plugin.getCustomItemHandler().getItem("BLAST_PROOF_GLASS"))
                    .onTNTExplode(event);
            if (entity.hasMetadata("isDBedwarsTNT")
                    && entity.getMetadata("isDBedwarsTNT").contains(TNTItem.tntPrimedMeta))
                ((TNTItem) plugin.getCustomItemHandler().getItem("TNT")).onTNTExplode(event);
        }

        event.blockList().removeIf(block -> !block.hasMetadata("placed"));
    }

    // TODO: revamp this
    @EventHandler
    public void handlePlayerHunger(FoodLevelChangeEvent event) {
        if (!event.getEntity().getWorld().equals(this.arena.getWorld())) return;

        if (this.arena.getSettings().isDisableHunger()) event.setCancelled(true);
    }

    @EventHandler
    public void handleCrafting(CraftItemEvent event) {
        if (!event.getView().getPlayer().getWorld().equals(this.arena.getWorld())) return;

        event.getRecipe().getResult().setType(XMaterial.AIR.parseMaterial());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handleDurability(PlayerItemDamageEvent event) {
        if (!event.getPlayer().getWorld().equals(this.arena.getWorld())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void handlePickup(PlayerPickupItemEvent event) {
        if (!event.getPlayer().getWorld().equals(this.arena.getWorld())) return;

        if (!this.arena.isArenaPlayer(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        if (event.getItem().hasMetadata("split")) {
            Optional<Spawner> spawner =
                    this.arena.getSpawner(LocationXYZ.valueOf(event.getItem().getLocation()), 1);
            spawner.ifPresent(
                    s -> {
                        Collection<Entity> entities =
                                this.arena
                                        .getWorld()
                                        .getNearbyEntities(
                                                s.getLocation(),
                                                s.getDropType().getSpawnRadius(),
                                                s.getDropType().getSpawnRadius(),
                                                s.getDropType().getSpawnRadius());
                        entities.remove(event.getPlayer());
                        for (Entity entity : entities) {
                            if (entity instanceof Player
                                    && this.arena.isArenaPlayer((Player) entity)) {
                                ((Player) entity)
                                        .getInventory()
                                        .addItem(event.getItem().getItemStack());
                            }
                        }
                    });
        }
    }

    @EventHandler
    public void handleInventoryAdd(InventoryPickupItemEvent event) {
        if (!event.getItem().getWorld().equals(this.arena.getWorld())) return;

        if (NBTUtils.hasNBTData(event.getItem().getItemStack(), "split")) {
            ItemStack item = NBTUtils.removeNBTData(event.getItem().getItemStack(), "split");
            event.setCancelled(true);
            event.getItem().remove();
            event.getInventory().addItem(item);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void handlePlayerFall(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().equals(this.arena.getWorld())) return;

        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;

        if (event.getTo().getY() <= this.arena.getSettings().getMinYAxis()) {
            this.arena.getAsArenaPlayer(event.getPlayer()).ifPresent(p -> p.kill(DeathCause.VOID));
        }
    }

    @EventHandler
    public void handleWaterBucketPlace(PlayerBucketEmptyEvent event) {
        if (!event.getPlayer().getWorld().equals(this.arena.getWorld())) return;

        if (event.getItemStack()
                .isSimilar(
                        ((DBedwars) this.getPlugin())
                                .getCustomItemHandler()
                                .getItem("WATER_BUCKET")
                                .toItemStack())) {
            ((WaterBucket)
                            ((DBedwars) this.getPlugin())
                                    .getCustomItemHandler()
                                    .getItem("WATER_BUCKET"))
                    .onWaterBucketUse(event);
        }
    }

    @EventHandler
    public void handleProjectileLand(ProjectileHitEvent event) {
        if (!event.getEntity().getWorld().equals(this.arena.getWorld())) return;

        Entity entity = event.getEntity();

        if (event.getEntityType() == EntityType.SNOWBALL) {
            if (entity.hasMetadata("isDBedWarsBedBugBall")
                    && entity.getMetadata("isDBedWarsBedBugBall")
                            .contains(BedBugSnowball.bedBugBallMeta)) {
                ((BedBugSnowball)
                                ((DBedwars) this.getPlugin())
                                        .getCustomItemHandler()
                                        .getItem("BED_BUG"))
                        .onLand(event);
            }
        }
    }

    @EventHandler
    public void handleTrapTrigger(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().equals(this.arena.getWorld())) return;

        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;

        this.arena
                .getAsArenaPlayer(event.getPlayer())
                .ifPresent(
                        p -> {
                            for (Team team : this.arena.getTeams()) {
                                if (team.getIslandArea().contains(event.getTo().toVector())
                                        && !team.getIslandArea()
                                                .contains(event.getFrom().toVector())) {
                                    PlayerBaseEnterEvent e =
                                            new PlayerBaseEnterEvent(p, this.arena, team);
                                    e.call();
                                }
                            }
                        });
    }

    @Override
    protected boolean isAllowMultipleInstances() {
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
