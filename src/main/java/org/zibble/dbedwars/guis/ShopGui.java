package org.zibble.dbedwars.guis;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.guis.component.PaginatedGuiComponent;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShopGui extends PaginatedGuiComponent<ChestMenu, ShopGui> {

    private static final Map<Player, ShopGui> OPEN_GUIS = new ConcurrentHashMap<>();

    public static ShopGui creator() {
        return new ShopGui();
    }

    public static boolean isOpen(Player player) {
        return OPEN_GUIS.containsKey(player);
    }

    public static ShopGui get(Player player) {
        return OPEN_GUIS.get(player);
    }

    protected ShopGui() {
        super(3);
    }

    @Override
    public ShopGui open(Player player, int page) {
        super.open(player, page);
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
