package org.zibble.dbedwars.api.nms;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
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

    IGolem getBedwarsGolem(IronGolem golem, Team spawningTeam);

    IBedBug getAsBedwarsBedBug(Silverfish bedBug, Team spawningTeam);

    PlayerGameProfile getProfile(Player player);

    NBTItem getNBTItem(ItemStack item);

    NBTItem.INbt getNBT(NBT nbt);

    com.github.retrooper.packetevents.protocol.item.ItemStack asPacketItem(ItemStack item);

    ItemStack fromPacketItem(com.github.retrooper.packetevents.protocol.item.ItemStack item);

    SkullMeta setSkullProfile(SkullMeta meta, PlayerGameProfile profile);

}
