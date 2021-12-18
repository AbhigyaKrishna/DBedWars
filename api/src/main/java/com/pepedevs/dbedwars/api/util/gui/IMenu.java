package com.pepedevs.dbedwars.api.util.gui;

import com.pepedevs.corelib.gui.inventory.ItemMenu;
import com.pepedevs.corelib.gui.inventory.action.ItemClickAction;
import com.pepedevs.corelib.gui.inventory.custom.book.item.AlternateBookPageActionItem;
import com.pepedevs.corelib.gui.inventory.item.action.back.BackActionItem;
import com.pepedevs.corelib.gui.inventory.item.voidaction.VoidActionItem;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.corelib.utils.itemstack.ItemStackUtils;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class IMenu<T extends ItemMenu> {

    protected static final AlternateBookPageActionItem NEXT_PAGE =
            new AlternateBookPageActionItem(
                    StringUtils.translateAlternateColorCodes("&cNext Page"),
                    ItemStackUtils.getSkull(
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODI3MWE0NzEwNDQ5NWUzNTdjM2U4ZTgwZjUxMWE5ZjEwMmIwNzAwY2E5Yjg4ZTg4Yjc5NWQzM2ZmMjAxMDVlYiJ9fX0="))
                    .setGoNext(true);
    protected static final AlternateBookPageActionItem PREVIOUS_PAGE =
            new AlternateBookPageActionItem(
                    StringUtils.translateAlternateColorCodes("&cPrevious Page"),
                    ItemStackUtils.getSkull(
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjllYTFkODYyNDdmNGFmMzUxZWQxODY2YmNhNmEzMDQwYTA2YzY4MTc3Yzc4ZTQyMzE2YTEwOThlNjBmYjdkMyJ9fX0="))
                    .setGoNext(false);
    protected static final VoidActionItem VOID_ITEM =
            new VoidActionItem(
                    StringUtils.translateAlternateColorCodes("&7"),
                    XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
    protected static final BackActionItem BACK =
            new BackActionItem(
                    StringUtils.translateAlternateColorCodes("&9Back"),
                    XMaterial.ARROW.parseItem(),
                    new String[0]);

    private final String identifier;
    protected T menu;

    protected IMenu(String identifier, T menu) {
        this.identifier = identifier;
        this.menu = menu;
    }

    protected abstract void setUpMenu(
            Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info);

    public void open(
            @Nullable ItemClickAction action, @Nullable Map<String, Object> info, Player player) {
        this.setUpMenu(player, action, info);
        this.menu.open(player);
    }

    public T getMenu() {
        return menu;
    }

    public String getIdentifier() {
        return identifier;
    }

}
