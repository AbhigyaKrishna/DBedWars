package org.zibble.dbedwars.game.arena.view;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.Keyed;
import org.zibble.dbedwars.guis.ShopPageGui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShopPage implements Keyed {

    private final ArenaPlayer player;
    private final Key key;
    private int row;
    private Message title;
    private String[] mask;
    private Map<Character, ShopItem> items;

    public ShopPage(ArenaPlayer player, Key key, int row, Message title) {
        this.player = player;
        this.key = key;
        this.row = row;
        this.title = title;
        this.mask = new String[row];
        this.items = new HashMap<>();
    }

    ShopPage(ArenaPlayer player, Key key, ShopInfoImpl.PageInfoImpl page) {
        this.player = player;
        this.key = key;
        this.row = page.getRow();
        this.title = page.getTitle();
        this.mask = new String[this.row];
        this.items = new HashMap<>();
        for (Map.Entry<Character, ShopInfoImpl.ItemInfoImpl> entry : page.getItems().entrySet()) {
            this.items.put(entry.getKey(), new ShopItem(this.player, entry.getValue()));
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.mask = Arrays.copyOf(this.mask, row);
        if (this.row < row) {
            for (int i = this.row; i < row; i++) {
                this.mask[i] = "XXXXXXXXX";
            }
        }
        this.row = row;
    }

    public Message getTitle() {
        return title;
    }

    public void setTitle(Message title) {
        this.title = title;
    }

    public String[] getMask() {
        return mask;
    }

    public void setMask(String... mask) {
        if (mask.length != this.row)
            throw new IllegalArgumentException("Mask length must be equal to rows. " + mask.length);
        this.mask = mask;
    }

    public void addItem(char key, ShopItem item) {
        items.put(key, item);
    }

    public ShopPageGui getGuiComponent(ShopView shopView) {
        ShopPageGui menu = new ShopPageGui(this.row)
                .title(this.title)
                .mask(this.mask);
        for (Map.Entry<Character, ShopItem> entry : this.items.entrySet()) {
            ItemTierGroup group = entry.getValue().getTierGroup();
            if (group == null) menu.item(entry.getKey(), entry.getValue().asMenuItem(shopView));
            else {
                int tier = shopView.getDataTracker().getCurrentTierOrDefault(group, -1);
                if (tier == -1) continue; //SHOULD NOT HAPPEN
                menu.item(entry.getKey(), group.getItem(tier).asMenuItem(shopView));
            }
        }
        return menu;
    }

    @Override
    public Key getKey() {
        return this.key;
    }

}
