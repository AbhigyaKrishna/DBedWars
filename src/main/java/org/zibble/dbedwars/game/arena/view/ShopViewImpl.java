package org.zibble.dbedwars.game.arena.view;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.view.ItemTierGroup;
import org.zibble.dbedwars.api.game.view.ShopPage;
import org.zibble.dbedwars.api.game.view.ShopView;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.game.arena.ArenaPlayerImpl;
import org.zibble.dbedwars.guis.ShopGui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShopViewImpl implements ShopView {

    private final ArenaPlayer player;
    private final ShopInfoImpl type;
    private final Map<Key, ShopPage> pages;
    private final DataTracker dataTracker;
    private ShopPage defaultPage;
    private BedwarsNPC npc;

    public ShopViewImpl(ArenaPlayerImpl player, ShopInfoImpl type) {
        this.player = player;
        this.type = type;
        this.pages = new LinkedHashMap<>();
        for (Map.Entry<Key, ShopInfoImpl.PageInfoImpl> entry : this.type.getPages().entrySet()) {
            this.createPage(entry.getKey(), entry.getValue());
        }
        this.defaultPage = this.pages.get(type.getDefaultPage().getKey());
        this.dataTracker = new DataTracker();
    }

    public void setNpc(BedwarsNPC npc) {
        this.npc = npc;
    }

    @Override
    public ArenaPlayer getPlayer() {
        return player;
    }

    @Override
    public ShopPage getDefaultPage() {
        return defaultPage;
    }

    @Override
    public void setDefaultPage(ShopPage defaultPage) {
        this.defaultPage = defaultPage;
    }

    @Override
    public BedwarsNPC getNpc() {
        return npc;
    }

    @Override
    public ShopPageImpl createPage(String key, int row, Message title) {
        ShopPageImpl page = new ShopPageImpl(this.player, Key.of(key), this, row, title);
        this.pages.put(Key.of(key), page);
        return page;
    }

    ShopPageImpl createPage(Key key, ShopInfoImpl.PageInfoImpl pg) {
        ShopPageImpl page = new ShopPageImpl(this.player, key, this, pg);
        this.pages.put(key, page);
        return page;
    }

    @Override
    public void addPage(String key, ShopPage page) {
        this.pages.put(Key.of(key), page);
    }

    public ShopGui getGui() {
        ShopGui gui = ShopGui.creator();
        for (Map.Entry<Key, ShopPage> entry : this.pages.entrySet()) {
            gui.addPage(entry.getKey(), entry.getValue().getGuiComponent());
        }
        return gui;
    }

    protected DataTracker getDataTracker() {
        return dataTracker;
    }

    protected static class DataTracker {

        private final Map<ItemTierGroup, Integer> currentTiers;

        private DataTracker() {
            this.currentTiers = new HashMap<>();
        }

        protected int getCurrentTierOrThrow(ItemTierGroupImpl group) {
            return this.currentTiers.get(group);
        }

        protected int getCurrentTierOrDefault(ItemTierGroup group, int defaultValue) {
            Integer tier = this.currentTiers.get(group);
            return tier == null ? defaultValue : tier;
        }

        protected int getOrZero(ItemTierGroup group) {
            return this.getCurrentTierOrDefault(group, 0);
        }

        protected void setCurrentTier(ItemTierGroup group, int tier) {
            this.currentTiers.put(group, tier);
        }

        protected void addTier(ItemTierGroup group) {
            int currentTier = this.getCurrentTierOrDefault(group, 0);
            if (currentTier + 1 >= group.getTierCount()) this.setCurrentTier(group, currentTier);
            else this.setCurrentTier(group, currentTier + 1);
        }

    }

}
