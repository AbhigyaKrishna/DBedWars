package org.zibble.dbedwars.api.util.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.zibble.dbedwars.api.util.DataType;

import java.util.Map;

public class JsonNbtSerializer {

    public static NBT deserialize(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isBoolean()) {
                return new NBTByte(primitive.getAsBoolean());
            } else if (primitive.isString()) {
                return new NBTString(primitive.getAsString());
            } else if (primitive.isNumber()) {
                Number number = primitive.getAsNumber();
                if (DataType.DOUBLE.isInstance(number)) {
                    return new NBTDouble(number.doubleValue());
                } else if (DataType.FLOAT.isInstance(number)) {
                    return new NBTFloat(number.floatValue());
                } else if (DataType.LONG.isInstance(number)) {
                    return new NBTLong(number.longValue());
                } else if (DataType.INTEGER.isInstance(number)) {
                    return new NBTInt(number.intValue());
                } else if (DataType.SHORT.isInstance(number)) {
                    return new NBTShort(number.shortValue());
                } else if (DataType.BYTE.isInstance(number)) {
                    return new NBTByte(number.byteValue());
                }
            }
        } else if (element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            return deserializeArray(arr);
        } else if (element.isJsonObject()) {
            NBTCompound compound = new NBTCompound();
            JsonObject obj = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                compound.setTag(entry.getKey(), deserialize(entry.getValue()));
            }
            return compound;
        }

        return null;
    }

    static NBT deserializeArray(JsonArray array) {
        if (array.size() == 0) return null;
        NBTType type = null;
        JsonElement first = array.get(0);
        if (first.isJsonPrimitive()) {
            if (first.getAsJsonPrimitive().isNumber()) {
                if (DataType.INTEGER.isInstance(first.getAsJsonPrimitive().getAsNumber())) {
                    type = NBTType.INT_ARRAY;
                } else if (DataType.BYTE.isInstance(first.getAsJsonPrimitive().getAsNumber())) {
                    type = NBTType.BYTE_ARRAY;
                }
            }
        }

        switch (type) {
            case INT_ARRAY: {
                int[] ints = new int[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    ints[i] = array.get(i).getAsInt();
                }
                return new NBTIntArray(ints);
            }
            case BYTE_ARRAY: {
                byte[] bytes = new byte[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    bytes[i] = array.get(i).getAsByte();
                }
                return new NBTByteArray(bytes);
            }
            default: {
                NBTList<NBT> list = new NBTList<>(type);
                for (JsonElement element : array) {
                    list.addTag(deserialize(element));
                }
                return list;
            }
        }
    }

}
