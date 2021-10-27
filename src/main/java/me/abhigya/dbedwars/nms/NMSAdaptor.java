package me.abhigya.dbedwars.nms;

import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.Team;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;

public interface NMSAdaptor {

  void clearRegionFileCache(World world);

  void clearChunkCache(World world);

  void refreshPlayerChunk(Player player);

  void clearDefaultPathFinding(LivingEntity entity);

  void clearDefaultPathFinding(Object entityCreature);

  void setBlockResistance(Block block, Float resistance);

  IVillager spawnNPCVillager(Location location);

  IGolem getBedwarsGolem(IronGolem golem, float chaseRadius, ArenaPlayer spawningPlayer);

  IBedBug getBedwarsBedBug(Silverfish bedBug, Team spawningTeam);
}
