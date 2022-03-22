package org.zibble.dbedwars.game.arena.view;

import java.util.TreeMap;

public class ItemTierGroup {

    private final TreeMap<Integer, ShopItem> tiers;
    private int tier = 0;

    public ItemTierGroup() {
        this.tiers = new TreeMap<>(Integer::compare);
    }

    public void addItem(ShopItem item) {
        this.tiers.put(this.tier++, item);
    }

    public ShopItem getItem(int tier) {
        return tiers.get(tier);
    }

    public int getTierCount() {
        return this.tiers.size();
    }

}
