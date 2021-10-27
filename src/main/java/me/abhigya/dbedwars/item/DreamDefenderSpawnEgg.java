package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.util.PotionEffectAT;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableCustomItems;
import me.abhigya.dbedwars.task.GolemDisplayNameUpdateTask;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class DreamDefenderSpawnEgg extends PluginActionItem {

    private final DBedwars plugin;
    public static final FixedMetadataValue GOLEM_META_VALUE =
            new FixedMetadataValue(DBedwars.getInstance(), true);
    private final ConfigurableCustomItems.ConfigurableDreamDefender cfgGolem;

    public DreamDefenderSpawnEgg(DBedwars plugin) {
        super(
                plugin,
                StringUtils.translateAlternateColorCodes(
                        plugin.getConfigHandler()
                                .getCustomItems()
                                .getDreamDefender()
                                .getItemName()),
                StringUtils.translateAlternateColorCodes(
                        (plugin.getConfigHandler().getCustomItems().getDreamDefender().getItemLore()
                                        == null
                                ? new ArrayList<>()
                                : plugin.getConfigHandler()
                                        .getCustomItems()
                                        .getDreamDefender()
                                        .getItemLore())),
                XMaterial.WOLF_SPAWN_EGG.parseMaterial());
        this.plugin = plugin;
        cfgGolem = plugin.getConfigHandler().getCustomItems().getDreamDefender();
    }

    @Override
    public void onActionPerform(
            Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (plugin.getGameManager().getArena(player.getWorld().getName()) != null
                && plugin.getGameManager()
                        .getArena(player.getWorld().getName())
                        .getAsArenaPlayer(player)
                        .isPresent()
                && !plugin.getGameManager()
                        .getArena(player.getWorld().getName())
                        .getAsArenaPlayer(player)
                        .get()
                        .isSpectator()) {

            ArenaPlayer arenaPlayer =
                    plugin.getGameManager()
                            .getArena(player.getWorld().getName())
                            .getAsArenaPlayer(player)
                            .get();
            Location spawn = playerInteractEvent.getClickedBlock().getLocation().add(0, 2, 0);
            IronGolem ironGolem =
                    (IronGolem) spawn.getWorld().spawnEntity(spawn, EntityType.IRON_GOLEM);
            Utils.useItem(player);
            ironGolem.setMetadata("isDBedwarsGolem", GOLEM_META_VALUE);
            plugin.getNMSAdaptor()
                    .getBedwarsGolem(ironGolem, 32, arenaPlayer)
                    .clearDefaultPathfinding()
                    .addCustomDefaults()
                    .initTargets(1);

            for (String effect : cfgGolem.getGolemPotionEffects()) {
                if (effect == null || effect.equals("")) continue;
                PotionEffectAT effectAT = PotionEffectAT.valueOf(effect);
                if (effectAT != null) effectAT.applyTo(ironGolem);
            }
            plugin.getThreadHandler()
                    .addAsyncWork(
                            new GolemDisplayNameUpdateTask(
                                    ironGolem, arenaPlayer.getTeam(), cfgGolem));
        }
    }

    public void onDeath(EntityDeathEvent event) {
        event.getDrops().clear();
    }
}
