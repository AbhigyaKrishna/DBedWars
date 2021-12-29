package com.pepedevs.dbedwars.nms.v1_8_R3;

import com.pepedevs.corelib.utils.reflection.general.FieldReflection;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.nms.IBedBug;
import com.pepedevs.dbedwars.api.nms.IGolem;
import com.pepedevs.dbedwars.api.nms.IVillager;
import com.pepedevs.dbedwars.api.nms.NMSAdaptor;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NMSUtils implements NMSAdaptor {

    @Override
    public void clearRegionFileCache(World world) {
        try {
            ChunkProviderServer chunkProviderServer =
                    ((CraftWorld) world).getHandle().chunkProviderServer;
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
        for (Chunk c : objMap.values()) c.removeEntities();
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
    public IGolem getBedwarsGolem(IronGolem golem, float chaseRadius, ArenaPlayer spawningPlayer) {
        return new BedwarsGolem(golem, chaseRadius, spawningPlayer);
    }

    @Override
    public IBedBug getBedwarsBedBug(Silverfish bedBug, Team spawningTeam) {
        return new BedwarsBedBug(bedBug, spawningTeam);
    }

    @Override
    public void sendTeamPacket(Team team, String displayName, String prefix, String suffix, int mode, int data) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        List<String> names = new ArrayList<>();
        for (ArenaPlayer player : team.getPlayers()) {
            names.add(player.getName());
        }
        try {
            FieldReflection.setValue(packet, "a", team.getName());
            FieldReflection.setValue(packet, "b", displayName);
            FieldReflection.setValue(packet, "c", prefix);
            FieldReflection.setValue(packet, "d", suffix);
            FieldReflection.setValue(packet, "e", "always");
            FieldReflection.setValue(packet, "f", EnumChatFormat.valueOf(team.getColor().getChatColor().name()).b());
            FieldReflection.setValue(packet, "g", names);
            FieldReflection.setValue(packet, "h", mode);
            FieldReflection.setValue(packet, "i", data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        for (ArenaPlayer player : team.getArena().getPlayers()) {
            ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
