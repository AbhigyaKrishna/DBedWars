package me.abhigya.dbedwars.game.arena.view;

import me.abhigya.dbedwars.api.exceptions.OverrideException;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableShop;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ShopPage {

    private ArenaPlayer player;
    private ConfigurableShop.ConfigurablePage page;
    private String[][] pattern;
    private Map<String, me.abhigya.dbedwars.api.game.view.ViewItem> items;
    private Map<String, me.abhigya.dbedwars.api.game.view.ViewItem> common;

    public ShopPage(ConfigurableShop.ConfigurablePage page, ArenaPlayer player, Map<String, me.abhigya.dbedwars.api.game.view.ViewItem> common) {
        this.items = new HashMap<>();
        this.page = page;
        this.player = player;
        this.common = common;
    }

    public void load() {
        this.parsePattern();
        this.page.getItems().forEach((s, item) -> {
            me.abhigya.dbedwars.api.game.view.ViewItem viewItem;
            if (common.containsKey(s)) {
                viewItem = common.get(s).clone();
                try {
                    viewItem.override(new ViewItem(this.player, item));
                } catch (OverrideException ignored) {}
            } else {
                viewItem = new ViewItem(this.player, item);
            }
            this.items.put(s, viewItem);
        });
        this.loadActionItems();
    }

    private void parsePattern() {
        this.pattern = new String[this.page.getPattern().size()][9];
        for (byte i = 0; i < this.page.getPattern().size(); i++) {
            this.pattern[i] = this.page.getPattern().get(i).split(" ");
        }
    }

    private void loadActionItems() {
        for (String[] str : this.pattern) {
            for (byte j = 0; j < 9; j++) {
                me.abhigya.dbedwars.api.game.view.ViewItem viewItem = this.items.getOrDefault(str[j], null);
                if (viewItem == null) {
                    if (this.common.containsKey(str[j])) {
                        viewItem = this.common.get(str[j]).clone();
                        this.items.put(str[j], viewItem);
                        viewItem.getActionItem(true);
                    }
                } else {
                    viewItem.getActionItem(true);
                }
            }
        }
    }

    public String getTitle() {
        return this.page.getGuiTitle();
    }

    public String[][] getPattern() {
        return pattern;
    }

    public Map<String, me.abhigya.dbedwars.api.game.view.ViewItem> getItems() {
        return items;
    }

}
