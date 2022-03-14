package org.zibble.dbedwars.guis;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.guis.component.PaginatedGuiComponent;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShopGui extends PaginatedGuiComponent<ChestMenu, ShopGui> {

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
        super(player, 3);
    }

    @Override
    public ShopGui open(int page) {
        super.open(page);
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
