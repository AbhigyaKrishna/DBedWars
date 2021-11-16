package com.pepedevs.dbedwars.game.arena.view.shop;

import com.pepedevs.dbedwars.api.exceptions.OverrideException;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableShop;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;

import java.util.HashMap;
import java.util.Map;

public class ShopPage {

    private final String key;

    private ArenaPlayer player;
    private ConfigurableShop.ConfigurablePage page;
    private String[][] pattern;
    private Map<String, com.pepedevs.dbedwars.api.game.view.ViewItem> items;
    private Map<String, com.pepedevs.dbedwars.api.game.view.ViewItem> common;

    public ShopPage(
            String key,
            ConfigurableShop.ConfigurablePage page,
            ArenaPlayer player,
            Map<String, com.pepedevs.dbedwars.api.game.view.ViewItem> common) {
        this.key = key;
        this.items = new HashMap<>();
        this.page = page;
        this.player = player;
        this.common = common;
    }

    public void load() {
        this.pattern = ConfigurationUtils.parseGuiPattern(this.page.getPattern());
        this.page
                .getItems()
                .forEach(
                        (s, item) -> {
                            com.pepedevs.dbedwars.api.game.view.ViewItem viewItem;
                            if (common.containsKey(s)) {
                                viewItem = common.get(s).clone();
                                try {
                                    viewItem.override(new ViewItem(this.player, this, item, s));
                                } catch (OverrideException ignored) {
                                }
                            } else {
                                viewItem = new ViewItem(this.player, this, item, s);
                            }
                            this.items.put(s, viewItem);
                        });
        for (String[] str : this.pattern) {
            for (byte j = 0; j < 9; j++) {
                com.pepedevs.dbedwars.api.game.view.ViewItem viewItem =
                        this.items.getOrDefault(str[j], null);
                if (viewItem == null) {
                    if (this.common.containsKey(str[j])) {
                        viewItem = this.common.get(str[j]).clone();
                        this.items.put(str[j], viewItem);
                    }
                }
            }
        }
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return this.page.getGuiTitle();
    }

    public String[][] getPattern() {
        return pattern;
    }

    public Map<String, com.pepedevs.dbedwars.api.game.view.ViewItem> getItems() {
        return items;
    }
}