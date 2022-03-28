package org.zibble.dbedwars.api.game.statistics;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public class ResourceStatistics extends Statistics<ArenaPlayer, XMaterial, Integer> {

    public ResourceStatistics(ArenaPlayer tracker) {
        super(tracker);
    }

    public void add(ItemStack item) {
        this.add(XMaterial.matchXMaterial(item), item.getAmount());
    }

    public void add(XMaterial resource, int amount) {
        if (this.containsKey(resource)) {
            this.put(resource, this.get(resource) + amount);
        } else {
            this.put(resource, amount);
        }
    }

}
