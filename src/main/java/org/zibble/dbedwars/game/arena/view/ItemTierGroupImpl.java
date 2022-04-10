package org.zibble.dbedwars.game.arena.view;

import org.zibble.dbedwars.api.game.view.ItemTierGroup;
import org.zibble.dbedwars.api.game.view.ShopItem;

import java.util.TreeMap;

public class ItemTierGroupImpl implements ItemTierGroup {

    private final TreeMap<Integer, ShopItem> tiers;
    private int tier = 0;

    public ItemTierGroupImpl() {
        this.tiers = new TreeMap<>(Integer::compare);
    }

    @Override
    public void addItem(ShopItem item) {
        this.tiers.put(this.tier++, item);
    }

    @Override
    public ShopItem getItem(int tier) {
        return tiers.get(tier);
    }

    @Override
    public int getTierCount() {
        return this.tiers.size();
    }

}
