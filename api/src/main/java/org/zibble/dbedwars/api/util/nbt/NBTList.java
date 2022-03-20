package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NBTList<T extends NBT> extends NBT {

    private NBTType<T> type;
    protected final List<T> tags = new ArrayList<>();

    public NBTList(NBTType<T> type) {
        this.type = type;
    }

    public NBTList(NBTType<T> type, List<T> tags) {
        this.type = type;
        this.tags.addAll(tags);
    }

    public NBTList(DataInput input) throws IOException {
        super(input);
    }

    public static NBTList<NBTCompound> createCompoundList() {
        return new NBTList<>(NBTType.COMPOUND);
    }

    public static NBTList<NBTString> createStringList() {
        return new NBTList<>(NBTType.STRING);
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.LIST;
    }

    public NBTType<T> getTagsType() {
        return type;
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    public int size() {
        return tags.size();
    }

    public List<T> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public T getTag(int index) {
        return tags.get(index);
    }

    public void setTag(int index, T tag) {
        validateAddTag(tag);
        tags.set(index, tag);
    }

    public void addTag(int index, T tag) {
        validateAddTag(tag);
        tags.add(index, tag);
    }

    public void addTag(T tag) {
        validateAddTag(tag);
        tags.add(tag);
    }

    protected void validateAddTag(T tag) {
        if (type != tag.getType()) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid tag type. Expected {0}, got {1}.", type.getNBTClass(), tag.getClass()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NBTList<T> other = (NBTList<T>) obj;
        return Objects.equals(type, other.type) && Objects.equals(tags, other.tags);
    }

    @Override
    public void read(DataInput input) throws IOException {
        NBTType<T> type = (NBTType<T>) this.readTagType(input);
        int size = input.readInt();
        if ((type == NBTType.END) && (size > 0)) {
            throw new IllegalStateException("Missing nbt list values tag type");
        }
        for (int i = 0; i < size; i++) {
            this.addTag(this.readTag(input, type));
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        this.writeTagType(output, type);
        output.writeInt(tags.size());
        for (T tag : tags) {
            tag.write(output);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, tags);
    }

    @Override
    public NBTList<T> clone() {
        List<T> newTags = new ArrayList<>();
        for (T tag : this.tags) {
            newTags.add((T) tag.clone());
        }
        return new NBTList<>(type, newTags);
    }
}
