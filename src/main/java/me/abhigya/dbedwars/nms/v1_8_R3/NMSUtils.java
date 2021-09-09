package me.abhigya.dbedwars.nms.v1_8_R3;

import com.google.common.collect.Lists;
import me.Abhigya.core.util.reflection.general.FieldReflection;
import me.abhigya.dbedwars.nms.NMSAdaptor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.LongObjectHashMap;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.Field;
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

    @Override
    public void setMetadata(org.bukkit.inventory.ItemStack item, String metadata, Object value) {
        this.setMetadata(CraftItemStack.asNMSCopy(item), metadata, value);
    }

    private void setMetadata(ItemStack item, String metadata, Object value) {
        NBTTagCompound tag = item.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        this.setTag(tag, metadata, value);
        item.setTag(tag);
    }

    @Override
    public boolean hasMetadata(org.bukkit.inventory.ItemStack item, String metadata) {
        return hasMetadata(CraftItemStack.asNMSCopy(item), metadata);
    }

    private boolean hasMetadata(ItemStack item, String metadata) {
        return item.getTag().hasKey(metadata);
    }

    @Override
    public Object getMetadata(org.bukkit.inventory.ItemStack item, String metadata) {
        return this.getMetadata(CraftItemStack.asNMSCopy(item), metadata);
    }

    private Object getMetadata(ItemStack item, String metadata) {
        return this.getObject(item.getTag().get(metadata));
    }

    private NBTTagCompound setTag(NBTTagCompound tag, String tagString, Object value) {

        if (value instanceof Boolean) {
            tag.setBoolean(tagString, (Boolean) value);
        } else if (value instanceof Integer) {
            tag.setInt(tagString, (Integer) value);
        } else if (value instanceof Byte) {
            tag.setByte(tagString, (Byte) value);
        } else if (value instanceof Double) {
            tag.setDouble(tagString, (Double) value);
        } else if (value instanceof Float) {
            tag.setFloat(tagString, (Float) value);
        } else if (value instanceof String) {
            tag.setString(tagString, (String) value);
        } else if (value instanceof Short) {
            tag.setShort(tagString, (Short) value);
        } else if (value instanceof Long) {
            tag.setLong(tagString, (Long) value);
        }

        return tag;
    }

    @SuppressWarnings("unchecked")
    private Object getObject(NBTBase tag) {
        if (tag instanceof NBTTagEnd) {
            return null;
        } else if (tag instanceof NBTTagLong) {
            return ((NBTTagLong) tag).c();
        } else if (tag instanceof NBTTagByte) {
            return ((NBTTagByte) tag).f();
        } else if (tag instanceof NBTTagShort) {
            return ((NBTTagShort) tag).e();
        } else if (tag instanceof NBTTagInt) {
            return ((NBTTagInt) tag).d();
        } else if (tag instanceof NBTTagFloat) {
            return ((NBTTagFloat) tag).h();
        } else if (tag instanceof NBTTagDouble) {
            return ((NBTTagDouble) tag).g();
        } else if (tag instanceof NBTTagByteArray) {
            return ((NBTTagByteArray) tag).c();
        } else if (tag instanceof NBTTagString) {
            return ((NBTTagString) tag).a_();
        } else if (tag instanceof NBTTagList) {
            List<NBTBase> list = null;
            try {
                Field field = tag.getClass().getDeclaredField("list");
                field.setAccessible(true);
                list = (List<NBTBase>) field.get(tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list == null) return null;
            List<Object> toReturn = Lists.newArrayList();
            for (NBTBase base : list) {
                toReturn.add(getObject(base));
            }
            return toReturn;
        } else if (tag instanceof NBTTagCompound) {
            return tag;
        } else if (tag instanceof NBTTagIntArray) {
            return ((NBTTagIntArray) tag).c();
        }
        return null;
    }

}
