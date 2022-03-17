package org.zibble.dbedwars.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.nms.NBTItem;
import org.zibble.dbedwars.api.util.nbt.NBT;
import org.zibble.dbedwars.api.util.nbt.NBTCompound;
import org.zibble.dbedwars.api.util.nbt.NBTList;
import org.zibble.dbedwars.api.util.nbt.NBTType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTItemImpl implements NBTItem {

    private final net.minecraft.server.v1_8_R3.ItemStack item;
    private final NbtTagCompoundImpl tag;

    public NBTItemImpl(ItemStack item) {
        this.item = CraftItemStack.asNMSCopy(item);
        this.tag = new NbtTagCompoundImpl(this.item.getTag());
    }

    public INbtCompound getRootTag() {
        return this.tag;
    }

    @Override
    public void applyNbt(String key, NBT nbt) {
        this.tag.setTag(key, nbt);
    }

    @Override
    public void applyTags(Map<String, ? extends NBT> nbts) {
        for (Map.Entry<String, ? extends NBT> entry : nbts.entrySet()) {
            this.tag.setTag(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Map<String, NBT> getTags() {
        Map<String, NBT> map = new HashMap<>();
        for (Map.Entry<String, ? extends INbt> entry : this.tag.getTags().entrySet()) {
            map.put(entry.getKey(), entry.getValue().asNBT());
        }
        return map;
    }

    @Override
    public NBT getTag(String key) {
        return this.tag.getTag(key).asNBT();
    }

    @Override
    public boolean hasKey(String key) {
        return this.tag.hasKey(key);
    }

    @Override
    public void setString(String key, String value) {
        this.tag.setString(key, value);
    }

    @Override
    public void setInt(String key, int value) {
        this.tag.setInt(key, value);
    }

    @Override
    public void setDouble(String key, double value) {
        this.tag.setDouble(key, value);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        this.tag.setBoolean(key, value);
    }

    @Override
    public void setByte(String key, byte value) {
        this.tag.setByte(key, value);
    }

    @Override
    public void setShort(String key, short value) {
        this.tag.setShort(key, value);
    }

    @Override
    public void setLong(String key, long value) {
        this.tag.setLong(key, value);
    }

    @Override
    public void setFloat(String key, float value) {
        this.tag.setFloat(key, value);
    }

    @Override
    public void setByteArray(String key, byte[] value) {
        this.tag.setByteArray(key, value);
    }

    @Override
    public void setIntArray(String key, int[] value) {
        this.tag.setIntArray(key, value);
    }

    @Override
    public void setLongArray(String key, long[] value) {
        this.tag.setLongArray(key, value);
    }

    @Override
    public void setList(String key, NBTList<? extends NBT> value) {
        this.tag.setList(key, value);
    }

    @Override
    public void setCompound(String key, NBTCompound value) {
        this.tag.setCompound(key, value);
    }

    public static class NbtImpl implements INbt {

        private final NBTBase nbt;

        public NbtImpl(NBTBase nbt) {
            this.nbt = nbt;
        }

        public NbtImpl(NBT nbt) {
            NBTCompound tag = new NBTCompound();
            tag.setTag("nbt", nbt);
            byte[] bytes = new byte[0];
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(stream)))) {
                tag.write(out);
                bytes = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {;
                NBTTagCompound compound = NBTCompressedStreamTools.a(in);
                this.nbt = compound.get("nbt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object getHandle() {
            return this.nbt;
        }

        @Override
        public NBTType getType() {
            return NBTType.fromId(this.nbt.getTypeId());
        }

        @Override
        public NBT asNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.set("nbt", this.nbt);
            byte[] bytes = new byte[0];
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                NBTCompressedStreamTools.a(tag, out);
                bytes = out.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (DataInputStream in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes))))) {;
                NBTCompound compound = new NBTCompound(in);
                return compound.getTagOrNull("nbt");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class NbtTagCompoundImpl extends NbtImpl implements INbtCompound {

        private final NBTTagCompound tag;

        public NbtTagCompoundImpl(NBTTagCompound tag) {
            super(tag);
            this.tag = tag;
        }

        public NbtTagCompoundImpl(NBTCompound nbt) {
            super(nbt);
            this.tag = (NBTTagCompound) super.getHandle();
        }

        @Override
        public boolean hasKey(String key) {
            return this.tag.hasKey(key);
        }

        @Override
        public Map<String, ? extends INbt> getTags() {
            Map<String, NbtImpl> map = new HashMap<>();
            for (String s : this.tag.c()) {
                map.put(s, new NbtImpl(this.tag.get(s)));
            }
            return map;
        }

        @Override
        public INbt getTag(String key) {
            return new NbtImpl(this.tag.get(key));
        }

        @Override
        public void setTag(String key, NBT value) {
            this.tag.set(key, (NBTBase) new NbtImpl(value).getHandle());
        }

        @Override
        public void setTag(String key, INbt value) {
            this.tag.set(key, (NBTBase) value.getHandle());
        }

        @Override
        public void setString(String key, String value) {
            this.tag.setString(key, value);
        }

        @Override
        public void setInt(String key, int value) {
            this.tag.setInt(key, value);
        }

        @Override
        public void setDouble(String key, double value) {
            this.tag.setDouble(key, value);
        }

        @Override
        public void setBoolean(String key, boolean value) {
            this.tag.setBoolean(key, value);
        }

        @Override
        public void setByte(String key, byte value) {
            this.tag.setByte(key, value);
        }

        @Override
        public void setShort(String key, short value) {
            this.tag.setShort(key, value);
        }

        @Override
        public void setLong(String key, long value) {
            this.tag.setLong(key, value);
        }

        @Override
        public void setFloat(String key, float value) {
            this.tag.setFloat(key, value);
        }

        @Override
        public void setByteArray(String key, byte[] value) {
            this.tag.setByteArray(key, value);
        }

        @Override
        public void setIntArray(String key, int[] value) {
            this.tag.setIntArray(key, value);
        }

        @Override
        public void setLongArray(String key, long[] value) {

        }

        @Override
        public void setList(String key, NBTList<? extends NBT> value) {
            this.tag.set(key, (NBTBase) new NbtImpl(value).getHandle());
        }

        @Override
        public void setCompound(String key, NBTCompound value) {
            this.tag.set(key, (NBTBase) new NbtImpl(value).getHandle());
        }

    }

}
