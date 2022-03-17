package org.zibble.dbedwars.nms.v1_8_R3;

import com.mojang.authlib.GameProfile;
import com.pepedevs.radium.utils.reflection.general.FieldReflection;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_8_R3.util.LongObjectHashMap;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.nms.*;
import org.zibble.dbedwars.api.objects.profile.PlayerGameProfile;
import org.zibble.dbedwars.api.objects.profile.Property;
import org.zibble.dbedwars.api.util.nbt.NBT;
import org.zibble.dbedwars.api.util.nbt.NBTCompound;
import org.zibble.dbedwars.api.util.nbt.NBTType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NMSUtils implements NMSAdaptor {

    @Override
    public void clearRegionFileCache(World world) {
        try {
            ChunkProviderServer chunkProviderServer = ((CraftWorld) world).getHandle().chunkProviderServer;
            IChunkLoader chunkLoader = FieldReflection.getValue(chunkProviderServer, "chunkLoader");
            File file = FieldReflection.getValue(chunkLoader, "d");
            file = new File(file, "region");
            if (!file.exists()) {
                return;
            }

            File[] files = file.listFiles();

            for (File current : files) {
                synchronized (RegionFileCache.a) {
                    RegionFileCache.a.remove(current);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearChunkCache(World world) {
        WorldServer ws = ((CraftWorld) world).getHandle();
        LongObjectHashMap<Chunk> objMap = ws.chunkProviderServer.chunks;
        for (Chunk c : objMap.values()) c.removeEntities();
        synchronized (ws.chunkProviderServer.chunks) {
            ws.chunkProviderServer.chunks.clear();
        }
        synchronized (ws.chunkProviderServer.unloadQueue) {
            ws.chunkProviderServer.unloadQueue.clear();
        }
    }

    @Override
    public void refreshPlayerChunk(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PlayerChunkMap playerChunkMap = entityPlayer.u().getPlayerChunkMap();
        playerChunkMap.removePlayer(entityPlayer);
        playerChunkMap.addPlayer(entityPlayer);
    }

    @Override
    public void clearDefaultPathFinding(LivingEntity entity) {
        this.clearDefaultPathFinding(((CraftEntity) entity).getHandle());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void clearDefaultPathFinding(Object entityCreature) {
        EntityCreature creature = (EntityCreature) entityCreature;
        try {
            FieldReflection.setValue(creature.goalSelector, "b", new UnsafeList());
            FieldReflection.setValue(creature.goalSelector, "c", new UnsafeList());
            FieldReflection.setValue(creature.targetSelector, "b", new UnsafeList());
            FieldReflection.setValue(creature.targetSelector, "c", new UnsafeList());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBlockResistance(Block block, Float resistance) {
        net.minecraft.server.v1_8_R3.Block nmsBlock = CraftMagicNumbers.getBlock(block);
        try {
            FieldReflection.getAccessible(net.minecraft.server.v1_8_R3.Block.class, "durability")
                    .set(nmsBlock, Float.MAX_VALUE / 5);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IVillager spawnNPCVillager(Location location) {
        IVillager villager = new Villager(this, location);
        villager.spawn();
        return villager;
    }

    @Override
    public IGolem getBedwarsGolem(IronGolem golem, Team spawningPlayerTeam) {
        return new BedwarsGolem(golem, spawningPlayerTeam);
    }

    @Override
    public IBedBug getAsBedwarsBedBug(Silverfish bedBug, Team spawningTeam) {
        return new BedwarsBedBug(bedBug, spawningTeam);
    }

    @Override
    public PlayerGameProfile getProfile(Player player) {
        GameProfile gameProfile = ((CraftPlayer) player).getHandle().getProfile();
        List<Property> properties = new ArrayList<>();
        for (com.mojang.authlib.properties.Property textures : gameProfile.getProperties().get("textures")) {
            properties.add(Property.builder()
                    .name(textures.getName())
                    .value(textures.getValue())
                    .signature(textures.getSignature())
                    .build());
        }
        return PlayerGameProfile.builder()
                .uuid(gameProfile.getId())
                .name(gameProfile.getName())
                .properties(properties)
                .build();
    }

    @Override
    public NBTItem getNBTItem(ItemStack item) {
        return new NBTItemImpl(item);
    }

    @Override
    public NBTItem.INbt getNBT(NBT nbt) {
        if (nbt.getType() == NBTType.COMPOUND) {
            return new NBTItemImpl.NbtTagCompoundImpl((NBTCompound) nbt);
        } else {
            return new NBTItemImpl.NbtImpl(nbt);
        }
    }

}
