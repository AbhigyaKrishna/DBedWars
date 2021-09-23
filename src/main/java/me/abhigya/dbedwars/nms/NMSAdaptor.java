package me.abhigya.dbedwars.nms;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface NMSAdaptor {

    void clearRegionFileCache(World world);

    void clearChunkCache(World world);

    void refreshPlayerChunk(Player player);

    void clearDefaultPathFinding(LivingEntity entity);

    void clearDefaultPathFinding(Object entityCreature);

    IVillager spawnNPCVillager(Location location);
}
