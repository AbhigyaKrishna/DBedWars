package me.abhigya.dbedwars.item;

import me.abhigya.dbedwars.DBedwars;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.Directional;

public class ChestListener implements Listener {
    @EventHandler
    public void onChestPlace(BlockPlaceEvent event){
        if (event.getBlockPlaced().getType() == Material.CHEST){
            event.getPlayer().sendMessage("FIX UP POPUP TOWER");
            BlockFace face = ((Directional) event.getBlockPlaced().getState().getData()).getFacing().getOppositeFace();
            Bukkit.getScheduler().runTaskLater(DBedwars.getInstance(),()->{
                DBedwars.getInstance().getThreadHandler().addSyncWork(new PopupTowerBuilder(event.getBlockPlaced().getLocation(),face, DyeColor.BLUE).getWorkload(2));
                event.getBlockPlaced().setType(Material.AIR);
            },1);
        }
    }
}
