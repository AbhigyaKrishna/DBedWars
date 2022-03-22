package org.zibble.dbedwars.database.data;

import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

import java.util.UUID;

public class QuickBuy implements PlayerDataCache {

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
        writer.writeUUID("uuid", this.uuid);
        writer.writeString("name", this.name);
        for (int i = 0; i < this.data.size(); i++) {
            writer.writeString(String.valueOf(i), this.data.get(i));
        }
    }

    @Override
    public void load(DataReader<?> reader) throws Exception {
        this.uuid = reader.readUUID("uuid");
        this.name = reader.readString("name");
        this.data = new QuickBuyData();
        for (int i = 0; i < this.data.size(); i++) {
            try {
                this.data.set(i, reader.readString(String.valueOf(i)));
            } catch (Exception ignored) {}
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
