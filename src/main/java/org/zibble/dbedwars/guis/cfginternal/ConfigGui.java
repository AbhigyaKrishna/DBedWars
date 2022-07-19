package org.zibble.dbedwars.guis.cfginternal;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigGui {

    public static final Map<Key, ConfigGui> GUIS = new ConcurrentHashMap<>();

    private final Key key;
    private final Json json;

    public ConfigGui(Key key, Json json) {
        this.key = key;
        this.json = json;
        GUIS.put(key, this);
    }

    public void open(Player player) {
        Placeholder[] placeholders = new Placeholder[]{
                PlaceholderEntry.symbol("player_name", player.getName()),
                PlaceholderEntry.symbol("player_uuid", player.getUniqueId().toString())
        };
        ChestMenu menu = new ChestMenu(json.get("rows").getAsInt(), AdventureMessage.from(json.get("title").getAsString(), placeholders).asComponentWithPAPI(player)[0]);
        GuiComponent<ChestMenu, GuiComponent> component = GuiComponent.creator(menu);
        String[] mask = new String[menu.getRows()];
        Preconditions.checkArgument(json.getAsJsonArray("mask").size() == mask.length, "Mask size does not match menu rows");
        int index = 0;
        for (JsonElement element : json.getAsJsonArray("mask")) {
            mask[index++] = element.getAsString().replace(" ", "").trim();
        }
        component.mask(mask);

        for (Map.Entry<String, JsonElement> items : json.getAsJson("items").entrySet()) {
            component.item(items.getKey().charAt(0), BwItemStack.fromJson(Json.of(items.getValue().getAsJsonObject()), Optional.of(player), placeholders));
        }

        component.open(player);
    }

    public Key getKey() {
        return key;
    }

}