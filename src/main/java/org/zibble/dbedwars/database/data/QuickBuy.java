package org.zibble.dbedwars.database.data;

import org.zibble.dbedwars.api.util.NumberUtils;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class QuickBuy implements PlayerDataCache {

    public static final Key UUID = Key.of("uuid");
    public static final Key NAME = Key.of("name");
    public static final Key DATA = Key.of("data");

    private UUID uuid;
    private String name;
    private QuickBuyData data;

    public QuickBuy() {
    }

    public QuickBuy(UUID uuid, String name, QuickBuyData data) {
        this.uuid = uuid;
        this.name = name;
        this.data = data;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuickBuyData getData() {
        return data;
    }

    public void setData(QuickBuyData data) {
        this.data = data;
    }

    @Override
    public DataCache copy() {
        return new QuickBuy(this.uuid, this.name, this.data);
    }

    @Override
    public void save(DataWriter<?> writer) throws Exception {
        writer.writeUUID(UUID, this.uuid);
        writer.writeString(NAME, this.name);
        writer.writeMap(DATA, this.data.getSlots(), String::valueOf, (k, v) -> {
            try {
                writer.writeString(Key.of(k), v);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void load(DataReader<?> reader) throws Exception {
        this.uuid = reader.readUUID(UUID);
        this.name = reader.readString(NAME);
        this.data = new QuickBuyData();
        Map<Integer, String> map = reader.readMap(DATA, NumberUtils::toInt, k -> {
            try {
                return reader.readString(Key.of(k));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            this.data.set(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties.builder()
                .add("uuid", this.uuid)
                .add("name", this.name)
                .add("data", this.data)
                .build();
    }

}
