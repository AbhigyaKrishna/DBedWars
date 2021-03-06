package me.abhigya.dbedwars.nms.v1_8_R3;

import me.Abhigya.core.util.reflection.general.FieldReflection;
import me.abhigya.dbedwars.nms.IVillager;
import me.abhigya.dbedwars.nms.NMSAdaptor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.LongObjectHashMap;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;

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
            int num = files.length;
            int n = 0;

            while (n < num) {
                File current = files[n];
                RegionFileCache.a.remove(current);
                ++n;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearChunkCache(World world) {
        WorldServer ws = ((CraftWorld) world).getHandle();
        LongObjectHashMap<Chunk> objMap = ws.chunkProviderServer.chunks;
        for (Chunk c : objMap.values())
            c.removeEntities();
        ws.chunkProviderServer.chunks.clear();
        ws.chunkProviderServer.unloadQueue.clear();
    }

    @Override
    public void refreshPlayerChunk(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PlayerChunkMap playerChunkMap = entityPlayer.u().getPlayerChunkMap();
        playerChunkMap.removePlayer(entityPlayer);
        playerChunkMap.addPlayer(entityPlayer);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void clearDefaultPathFinding(LivingEntity entity) {
        EntityCreature creature = (EntityCreature) ((CraftEntity) entity).getHandle();
        try {
            FieldReflection.setValue(creature.goalSelector, "b", new UnsafeList());
            FieldReflection.setValue(creature.goalSelector, "c", new UnsafeList());
            FieldReflection.setValue(creature.targetSelector, "b", new UnsafeList());
            FieldReflection.setValue(creature.targetSelector, "c", new UnsafeList());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
    public IVillager spawnNPCVillager(Location location) {
        IVillager villager = new Villager(this, location);
        villager.spawn();
        return villager;
    }
}
