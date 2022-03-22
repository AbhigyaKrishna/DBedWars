package org.zibble.dbedwars.database.data;

import org.zibble.dbedwars.api.util.json.Json;

import java.util.Map;
import java.util.TreeMap;

public class QuickBuyData {

    private TreeMap<Integer, String> slots = new TreeMap<>(Integer::compare);

    public QuickBuyData() {
    }

    public static QuickBuyData from(Json json) {
        QuickBuyData data = new QuickBuyData();
        for (int i = 0; i < 54; i++) {
            if (json.has(String.valueOf(i))) {
                data.slots.put(i, json.get(String.valueOf(i)).getAsString());
            }
        }
        return data;
    }

    public String get(int i) {
        return this.slots.get(i);
    }

    public int size() {
        return this.slots.size();
    }

    public boolean has(int i) {
        return this.slots.containsKey(i);
    }

    public void set(int i, String value) {
        this.slots.put(i, value);
    }

    public Json toJson() {
        Json json = Json.getNew();
        for (Map.Entry<Integer, String> entry : this.slots.entrySet()) {
            json.addProperty(String.valueOf(entry.getKey()), entry.getValue());
        }
        return json;
    }

}
