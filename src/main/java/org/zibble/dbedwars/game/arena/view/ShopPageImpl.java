package org.zibble.dbedwars.game.arena.view;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.view.ItemTierGroup;
import org.zibble.dbedwars.api.game.view.ShopItem;
import org.zibble.dbedwars.api.game.view.ShopPage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.guis.ShopPageGui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShopPageImpl implements ShopPage {

    private final ArenaPlayer player;
    private final Key key;
    private final ShopViewImpl shopView;
    private int row;
    private Message title;
    private String[] mask;
    private Map<Character, ShopItem> items;

    public ShopPageImpl(ArenaPlayer player, Key key, ShopViewImpl shopView, int row, Message title) {
        this.player = player;
        this.key = key;
        this.shopView = shopView;
        this.row = row;
        this.title = title;
        this.mask = new String[row];
        this.items = new HashMap<>();
    }

    ShopPageImpl(ArenaPlayer player, Key key, ShopViewImpl shopView, ShopInfoImpl.PageInfoImpl page) {
        this.player = player;
        this.key = key;
        this.shopView = shopView;
        this.row = page.getRow();
        this.title = page.getTitle();
        this.mask = new String[this.row];
        this.items = new HashMap<>();
        for (Map.Entry<Character, ShopInfoImpl.ItemInfoImpl> entry : page.getItems().entrySet()) {
            this.items.put(entry.getKey(), new ShopItemImpl(this.player, entry.getValue()));
        }
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public void setRow(int row) {
        this.mask = Arrays.copyOf(this.mask, row);
        if (this.row < row) {
            for (int i = this.row; i < row; i++) {
                this.mask[i] = "XXXXXXXXX";
            }
        }
        this.row = row;
    }

    @Override
    public Message getTitle() {
        return title;
    }

    @Override
    public void setTitle(Message title) {
        this.title = title;
    }

    @Override
    public String[] getMask() {
        return mask;
    }

    @Override
    public void setMask(String... mask) {
        if (mask.length != this.row)
            throw new IllegalArgumentException("Mask length must be equal to rows. " + mask.length);
        this.mask = mask;
    }

    @Override
    public void addItem(char key, ShopItem item) {
        items.put(key, item);
    }

    @Override
    public ShopPageGui getGuiComponent() {
        ShopPageGui menu = new ShopPageGui(this.row)
                .title(this.title)
                .mask(this.mask);
        for (Map.Entry<Character, ShopItem> entry : this.items.entrySet()) {
            ItemTierGroup group = entry.getValue().getTierGroup();
            if (group == null) menu.item(entry.getKey(), entry.getValue().asMenuItem(shopView));
            else {
                int tier = ((ShopViewImpl) shopView).getDataTracker().getCurrentTierOrDefault(group, -1);
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
