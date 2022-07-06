package org.zibble.dbedwars.guis;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.guis.component.PaginatedGuiComponent;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShopGui extends PaginatedGuiComponent<Key, ChestMenu, ShopGui> {

    private static final Map<Player, ShopGui> OPEN_GUIS = new ConcurrentHashMap<>();

    protected ShopGui() {
    }

    public static ShopGui creator() {
        return new ShopGui();
    }

    public static boolean isOpen(Player player) {
        return OPEN_GUIS.containsKey(player);
    }

    public static ShopGui get(Player player) {
        return OPEN_GUIS.get(player);
    }

    @Override
    public ShopGui open(Player player, Key key) {
        super.open(player, key);
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
