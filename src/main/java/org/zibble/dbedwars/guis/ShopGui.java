package org.zibble.dbedwars.guis;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.guis.component.PaginatedGuiComponent;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShopGui extends PaginatedGuiComponent<Key<String>, ChestMenu, ShopGui> {

    private static final Map<Player, ShopGui> OPEN_GUIS = new ConcurrentHashMap<>();

    public static ShopGui creator(Player player) {
        return new ShopGui(player);
    }

    public static boolean isOpen(Player player) {
        return OPEN_GUIS.containsKey(player);
    }

    public static ShopGui get(Player player) {
        return OPEN_GUIS.get(player);
    }

    protected ShopGui(Player player) {
        super(player);
    }

    @Override
    public ShopGui open(Key<String> key) {
        super.open(key);
        OPEN_GUIS.put(player, this);
        return this;
    }

    @Override
    public ShopGui close() {
        super.close();
        OPEN_GUIS.remove(this.player);
        return this;
    }

}
