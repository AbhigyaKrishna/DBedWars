package org.zibble.dbedwars.api.nms;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.objects.profile.PlayerGameProfile;
import org.zibble.dbedwars.api.util.nbt.NBT;

public interface NMSAdaptor {

    void clearRegionFileCache(World world);

    void clearChunkCache(World world);

    void refreshPlayerChunk(Player player);

    void clearDefaultPathFinding(LivingEntity entity);

    void clearDefaultPathFinding(Object entityCreature);

    void setBlockResistance(Block block, Float resistance);

    IVillager spawnNPCVillager(Location location);

    IGolem getBedwarsGolem(IronGolem golem, Team spawningTeam);

    IBedBug getAsBedwarsBedBug(Silverfish bedBug, Team spawningTeam);

    PlayerGameProfile getProfile(Player player);

    NBTItem getNBTItem(ItemStack item);

    NBTItem.INbt getNBT(NBT nbt);

}
