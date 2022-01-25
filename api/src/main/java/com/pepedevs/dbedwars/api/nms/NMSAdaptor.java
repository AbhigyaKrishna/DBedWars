package com.pepedevs.dbedwars.api.nms;

import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;

import java.util.Collection;

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

    void sendTeamPacket(Team team, String displayName, String prefix, String suffix, int mode, int data);

    void sendDeathAnimation(Player player, Collection<Player> viewers);

}
