package org.zibble.dbedwars.game.arena.view.shop;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.configuration.configurable.ConfigurableShop;
import org.zibble.dbedwars.guis.ShopGui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShopView {

    private final ArenaPlayer player;
    private ShopPage defaultPage;
    private final Map<Key<String>, ShopPage> pages;
    private final Map<Key<String>, UseCondition<?>> conditions;

    public static ShopView fromConfig(ArenaPlayer player, ConfigurableShop config) {
        ShopView view = new ShopView(player);
        for (Map.Entry<String, ConfigurableShop.ConfigurablePage> entry : config.getPages().entrySet()) {
            view.addPage(entry.getKey(), ShopPage.fromConfig(player, entry.getValue()));
        }
        view.setDefaultPage(view.pages.getOrDefault(Key.of(config.getDefaultPage()), view.pages.entrySet().iterator().next().getValue()));
        return view;
    }

    public ShopView(ArenaPlayer player) {
        this.player = player;
        this.pages = new LinkedHashMap<>();
        this.conditions = new HashMap<>();
    }

    public void addDefaultConditions() {
        this.addCondition("item", UseConditions.HAS_ITEM);
        this.addCondition("permission", UseConditions.HAS_PERMISSION);
        this.addCondition("exp", UseConditions.HAS_EXP);
    }

    public ArenaPlayer getPlayer() {
        return player;
    }

    public ShopPage getDefaultPage() {
        return defaultPage;
    }

    public void setDefaultPage(ShopPage defaultPage) {
        this.defaultPage = defaultPage;
    }

    public void addCondition(String key, UseCondition<?> condition) {
        this.conditions.put(Key.of(key), condition);
    }

    public void addPage(String key, ShopPage page) {
        this.pages.put(Key.of(key), page);
    }

    public ShopGui getGui() {
        ShopGui gui = ShopGui.creator();
        for (Map.Entry<Key<String>, ShopPage> entry : this.pages.entrySet()) {
            gui.addPage(entry.getValue().getGuiComponent());
        }
        return gui;
    }

}
