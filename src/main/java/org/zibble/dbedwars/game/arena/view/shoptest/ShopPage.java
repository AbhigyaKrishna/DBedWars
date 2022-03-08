package org.zibble.dbedwars.game.arena.view.shoptest;

import com.pepedevs.radium.utils.StringUtils;
import org.zibble.dbedwars.api.exceptions.OverrideException;
import org.zibble.dbedwars.configuration.configurable.ConfigurableShop;
import org.zibble.dbedwars.utils.ConfigurationUtils;

import java.util.HashMap;
import java.util.Map;

public class ShopPage implements org.zibble.dbedwars.api.game.view.ShopPage {

    private final String key;
    private final Map<String, org.zibble.dbedwars.api.game.view.GuiItem> items;
    private org.zibble.dbedwars.api.game.view.ShopView view;
    private String title;
    private org.zibble.dbedwars.api.game.view.GuiItem[][] pattern;

    public ShopPage(String key) {
        this.key = key;
        this.items = new HashMap<>();
    }

    public void loadFromConfig(
            ShopView view, Map<String, GuiItem> common, ConfigurableShop.ConfigurablePage page) {
        this.view = view;
        this.title = StringUtils.translateAlternateColorCodes(page.getTitle());
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
                            guiItem.loadFromConfig(this, view, common, item, page);
                            this.items.put(s, guiItem);
                        });

        String[][] pattern = ConfigurationUtils.parseGuiPattern(page.getPattern());
        this.pattern =
                new org.zibble.dbedwars.api.game.view.GuiItem[pattern.length][pattern[0].length];
        for (byte col = 0; col < pattern.length; col++) {
            for (byte row = 0; row < pattern[col].length; row++) {
                org.zibble.dbedwars.api.game.view.GuiItem item =
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
                if (item != null) item.setShopPage(this);
                this.pattern[col][row] = item;
            }
        }
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public org.zibble.dbedwars.api.game.view.ShopView getView() {
        return this.view;
    }

    @Override
    public void setView(org.zibble.dbedwars.api.game.view.ShopView view) {
        this.view = view;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public org.zibble.dbedwars.api.game.view.GuiItem[][] getPattern() {
        return this.pattern;
    }

    @Override
    public Map<String, org.zibble.dbedwars.api.game.view.GuiItem> getItems() {
        return this.items;
    }
}
