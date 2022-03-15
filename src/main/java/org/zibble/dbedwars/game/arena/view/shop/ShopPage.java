package org.zibble.dbedwars.game.arena.view.shop;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableShop;
import org.zibble.dbedwars.guis.ShopPageGui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShopPage {

    private final ArenaPlayer player;
    private int row;
    private Message title;
    private String[] mask;
    private Map<Character, ShopItem> items;

    public static ShopPage fromConfig(ArenaPlayer player, ConfigurableShop.ConfigurablePage config) {
        ShopPage page = new ShopPage(player, config.getPattern().size(), ConfigMessage.from(config.getTitle()));
        String[] mask = new String[config.getPattern().size()];
        for (int i = 0; i < config.getPattern().size(); i++) {
            mask[i] = config.getPattern().get(i).replace(" ", "");
        }
        page.setMask(mask);
        for (Map.Entry<String, ConfigurableShop.ConfigurablePage.ConfigurableItem> entry : config.getItems().entrySet()) {

        }
    }

    public ShopPage(ArenaPlayer player, int row, Message title) {
        this.player = player;
        this.row = row;
        this.title = title;
        this.mask = new String[row];
        this.items = new HashMap<>();
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

    public ShopPageGui getGuiComponent() {
        ShopPageGui menu = new ShopPageGui(this.row)
                .title(this.title)
                .mask(this.mask);
        for (Map.Entry<Character, ShopItem> entry : this.items.entrySet()) {
            menu.item(entry.getKey(), entry.getValue().getGuiIcon(), (player, clickType) -> {
                if (entry.getValue().canUse())
                    entry.getValue().use(this.player, clickType);
            });
        }
        return menu;
    }

}
