package org.zibble.dbedwars.io;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

public class UUIDTypeAdaptor extends TypeAdapter<UUID> {

    public static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    public static String fromUUID(UUID value) {
        return value.toString().replace("-", "");
    }

    public static UUID fromString(String input) {
        return UUID.fromString(input.replaceFirst(UUID_PATTERN.pattern(), "$1-$2-$3-$4-$5"));
    }

    public void write(JsonWriter out, UUID value) throws IOException {
        out.value(fromUUID(value));
    }

    public UUID read(JsonReader in) throws IOException {
        return fromString(in.nextString());
    }

}
