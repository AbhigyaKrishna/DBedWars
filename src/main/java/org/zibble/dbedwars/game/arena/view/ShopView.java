package org.zibble.dbedwars.game.arena.view;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.guis.ShopGui;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShopView {

    private final ArenaPlayer player;
    private final ShopInfoImpl type;
    private ShopPage defaultPage;
    private final Map<Key<String>, ShopPage> pages;

//    public static ShopView fromConfig(ArenaPlayer player, ConfigurableShop config) {
//        ShopView view = new ShopView(player);
//        for (Map.Entry<String, ConfigurableShop.ConfigurablePage> entry : config.getPages().entrySet()) {
//            view.addPage(entry.getKey(), ShopPage.fromConfig(player, entry.getValue()));
//        }
//        view.setDefaultPage(view.pages.getOrDefault(Key.of(config.getDefaultPage()), view.pages.entrySet().iterator().next().getValue()));
//        return view;
//    }

    public ShopView(ArenaPlayer player, ShopInfoImpl type) {
        this.player = player;
        this.type = type;
        this.pages = new LinkedHashMap<>();
        for (Map.Entry<Key<String>, ShopInfoImpl.PageInfoImpl> entry : this.type.getPages().entrySet()) {
            this.createPage(entry.getKey(), entry.getValue());
        }
        this.defaultPage = this.pages.get(type.getDefaultPage().getKey());
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

    public ShopPage createPage(String key, int row, Message title) {
        ShopPage page = new ShopPage(this.player, row, title);
        this.pages.put(Key.of(key), page);
        return page;
    }

    ShopPage createPage(Key<String> key, ShopInfoImpl.PageInfoImpl pg) {
        ShopPage page = new ShopPage(this.player, pg);
        this.pages.put(key, page);
        return page;
    }

    public void addPage(String key, ShopPage page) {
        this.pages.put(Key.of(key), page);
    }

    public ShopGui getGui() {
        ShopGui gui = ShopGui.creator(this.player.getPlayer());
        for (Map.Entry<Key<String>, ShopPage> entry : this.pages.entrySet()) {
            gui.addPage(entry.getValue().getGuiComponent());
        }
        return gui;
    }

}
