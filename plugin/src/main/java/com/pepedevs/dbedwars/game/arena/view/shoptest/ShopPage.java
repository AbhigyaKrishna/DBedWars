package com.pepedevs.dbedwars.game.arena.view.shoptest;

import com.pepedevs.dbedwars.api.exceptions.OverrideException;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableShop;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;
import me.Abhigya.core.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ShopPage implements com.pepedevs.dbedwars.api.game.view.ShopPage {

    private final String key;

    private com.pepedevs.dbedwars.api.game.view.ShopView view;
    private String title;
    private com.pepedevs.dbedwars.api.game.view.GuiItem[][] pattern;
    private final Map<String, com.pepedevs.dbedwars.api.game.view.GuiItem> items;

    public ShopPage(String key) {
        this.key = key;
        this.items = new HashMap<>();
    }

    public void loadFromConfig(
            ShopView view, Map<String, GuiItem> common, ConfigurableShop.ConfigurablePage page) {
        this.view = view;
        this.title = StringUtils.translateAlternateColorCodes(page.getGuiTitle());
        page.getItems()
                .forEach(
                        (s, item) -> {
                            GuiItem guiItem;
                            if (common.containsKey(s)) {
                                guiItem = common.get(s).clone();
                                try {
                                    guiItem.override(new GuiItem(s, view.getFormattedItem(item)));
                                } catch (OverrideException ignored) {
                                }
                            } else {
                                guiItem = new GuiItem(s, view.getFormattedItem(item));
                            }
                            guiItem.loadFromConfig(this, view, common, item);
                            this.items.put(s, guiItem);
                        });

        String[][] pattern = ConfigurationUtils.parseGuiPattern(page.getPattern());
        this.pattern =
                new com.pepedevs.dbedwars.api.game.view.GuiItem[pattern.length][pattern[0].length];
        for (byte col = 0; col < pattern.length; col++) {
            for (byte row = 0; row < pattern[col].length; row++) {
                com.pepedevs.dbedwars.api.game.view.GuiItem item =
                        common.getOrDefault(pattern[col][row], null);
                if (item != null) {
                    item = item.clone();
                    if (this.items.containsKey(pattern[col][row])) {
                        try {
                            item.override(this.items.get(pattern[col][row]));
                        } catch (OverrideException ignored) {
                        }
                    }
                } else {
                    item = this.items.getOrDefault(pattern[col][row], null);
                }
                this.pattern[col][row] = item;
            }
        }
    }

    public String getKey() {
        return this.key;
    }

    public com.pepedevs.dbedwars.api.game.view.ShopView getView() {
        return this.view;
    }

    public void setView(com.pepedevs.dbedwars.api.game.view.ShopView view) {
        this.view = view;
    }

    public String getTitle() {
        return this.title;
    }

    public com.pepedevs.dbedwars.api.game.view.GuiItem[][] getPattern() {
        return this.pattern;
    }

    public Map<String, com.pepedevs.dbedwars.api.game.view.GuiItem> getItems() {
        return this.items;
    }
}
