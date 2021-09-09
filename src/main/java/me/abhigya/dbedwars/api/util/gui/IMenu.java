package me.abhigya.dbedwars.api.util.gui;

import me.Abhigya.core.menu.inventory.ItemMenu;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.Abhigya.core.menu.inventory.custom.book.item.AlternateBookPageActionItem;
import me.Abhigya.core.menu.inventory.item.action.back.BackActionItem;
import me.Abhigya.core.menu.inventory.item.voidaction.VoidActionItem;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.itemstack.ItemStackUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class IMenu<T extends ItemMenu> {

    protected final static AlternateBookPageActionItem NEXT_PAGE = new AlternateBookPageActionItem(StringUtils.translateAlternateColorCodes("&cNext Page"),
            ItemStackUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODI3MWE0NzEwNDQ5NWUzNTdjM2U4ZTgwZjUxMWE5ZjEwMmIwNzAwY2E5Yjg4ZTg4Yjc5NWQzM2ZmMjAxMDVlYiJ9fX0="))
            .setGoNext(true);
    protected final static AlternateBookPageActionItem PREVIOUS_PAGE = new AlternateBookPageActionItem(StringUtils.translateAlternateColorCodes("&cPrevious Page"),
            ItemStackUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjllYTFkODYyNDdmNGFmMzUxZWQxODY2YmNhNmEzMDQwYTA2YzY4MTc3Yzc4ZTQyMzE2YTEwOThlNjBmYjdkMyJ9fX0="))
            .setGoNext(false);
    protected static final VoidActionItem VOID_ITEM = new VoidActionItem(StringUtils.translateAlternateColorCodes("&7"),
            XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
    protected static final BackActionItem BACK = new BackActionItem(StringUtils.translateAlternateColorCodes("&9Back"), XMaterial.ARROW.parseItem(), new String[0]);

    private final DBedwars plugin;

    private final String identifier;
    protected T menu;

    protected IMenu(DBedwars plugin, String identifier, T menu) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.menu = menu;
        this.menu.registerListener(plugin);
    }

    protected abstract void setUpMenu(Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info);

    public void open(@Nullable ItemClickAction action, @Nullable Map<String, Object> info, Player player) {
        this.getPlugin().getThreadHandler().addAsyncWork(() -> {
            this.setUpMenu(player, action, info);
            this.menu.open(player);
        });
    }

    public DBedwars getPlugin() {
        return plugin;
    }

    public T getMenu() {
        return menu;
    }

    public String getIdentifier() {
        return identifier;
    }
}
