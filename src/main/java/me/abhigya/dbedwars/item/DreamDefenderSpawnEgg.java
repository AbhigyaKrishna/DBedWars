package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.nms.v1_8_R3.BedwarsGolem;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class DreamDefenderSpawnEgg extends PluginActionItem {

    DBedwars plugin;

    public DreamDefenderSpawnEgg(DBedwars plugin) {
        super(plugin,
                StringUtils.translateAlternateColorCodes(plugin.getConfigHandler().getCustomItems().getDreamDefender().getItemName()),
                StringUtils.translateAlternateColorCodes((plugin.getConfigHandler().getCustomItems().getDreamDefender().getItemLore() == null ? new ArrayList<>() : plugin.getConfigHandler().getCustomItems().getDreamDefender().getItemLore())),
                XMaterial.WOLF_SPAWN_EGG.parseMaterial());
        this.plugin = plugin;
        }


    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
        if(playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if(plugin.getGameManager().getArena(player.getWorld().getName()) != null &&
                plugin.getGameManager().getArena(player.getWorld().getName()).getAsArenaPlayer(player).isPresent() &&
                !plugin.getGameManager().getArena(player.getWorld().getName()).getAsArenaPlayer(player).get().isSpectator()){

            ArenaPlayer arenaPlayer = plugin.getGameManager().getArena(player.getWorld().getName()).getAsArenaPlayer(player).get();

            Location spawn = playerInteractEvent.getClickedBlock().getLocation().add(0,2,0);
            IronGolem ironGolem = (IronGolem) spawn.getWorld().spawnEntity(spawn, EntityType.IRON_GOLEM);
            new BedwarsGolem(ironGolem,32,arenaPlayer)
                    .clearDefaultPathfinding()
                    .addCustomDefaults()
                    .initTargets();
        }
    }
}
