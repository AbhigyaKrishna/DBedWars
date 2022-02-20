package org.zibble.dbedwars.guis.setup;

import com.pepedevs.radium.gui.inventory.Item;
import com.pepedevs.radium.gui.inventory.action.ItemClickAction;
import com.pepedevs.radium.gui.inventory.custom.book.BookItemMenu;
import com.pepedevs.radium.gui.inventory.item.action.ActionItem;
import com.pepedevs.radium.gui.inventory.item.action.ItemAction;
import com.pepedevs.radium.gui.inventory.item.action.ItemActionPriority;
import com.pepedevs.radium.gui.inventory.item.voidaction.VoidActionItem;
import com.pepedevs.radium.gui.inventory.size.ItemMenuSize;
import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.radium.utils.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.util.gui.IMenu;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SetupStartGui extends IMenu<BookItemMenu> {

    private final DBedwars plugin;

    Item voidBar = new VoidActionItem("", XMaterial.WHITE_STAINED_GLASS_PANE.parseItem());
    private ActionItem newArena =
            new ActionItem(
                    StringUtils.translateAlternateColorCodes("&5Create new arena!"),
                    XMaterial.EMERALD.parseItem());

    public SetupStartGui(DBedwars plugin) {
        super(
                "START_SETUP",
                new BookItemMenu(
                        StringUtils.translateAlternateColorCodes("Select arena to configure"),
                        ItemMenuSize.SIX_LINE,
                        ItemMenuSize.ONE_LINE,
                        null));
        this.plugin = plugin;
        newArena.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        SetupStartGui.this
                                .plugin
                                .getGuiHandler()
                                .getAnvilGui("ARENA_NAME_SETUP")
                                .open(itemClickAction.getPlayer(), itemClickAction, null);
                        itemClickAction.setClose(true);
                    }
                });
    }

    @Override
    protected void setUpMenu(
            Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
        this.menu.clear();
        if (this.plugin.getGameManager().getArenas().isEmpty()) {

            for (byte b = 0; b < 45; b++) {
                if (b == 22) {
                    this.menu.addItem(newArena);
                    continue;
                }

                this.menu.addItem(IMenu.VOID_ITEM);
            }

            for (byte b = 0; b < 9; b++) {
                this.menu.setBarButton(b, voidBar);
            }
            return;
        }

        for (byte b = 0; b < 9; b++) {
            if (b == 2) {
                this.menu.setBarButton(b, newArena);
            } else if (b == 6) {
                this.menu.setBarButton(b, IMenu.PREVIOUS_PAGE);
            } else if (b == 7) {
                this.menu.setBarButton(b, IMenu.NEXT_PAGE);
            } else {
                this.menu.setBarButton(b, voidBar);
            }
        }

        for (Map.Entry<String, Arena> e : this.plugin.getGameManager().getArenas().entrySet()) {
            ActionItem item;
            if (e.getValue().isEnabled()) {
                item =
                        new ActionItem(
                                StringUtils.translateAlternateColorCodes("&a" + e.getKey()),
                                XMaterial.LIME_DYE.parseItem());
            } else {
                if (e.getValue().isConfigured())
                    item =
                            new ActionItem(
                                    StringUtils.translateAlternateColorCodes("&7" + e.getKey()),
                                    XMaterial.GRAY_DYE.parseItem());
                else
                    item =
                            new ActionItem(
                                    StringUtils.translateAlternateColorCodes("&c" + e.getKey()),
                                    XMaterial.RED_DYE.parseItem());
            }
            item.addAction(
                    new ItemAction() {
                        @Override
                        public ItemActionPriority getPriority() {
                            return ItemActionPriority.NORMAL;
                        }

                        @Override
                        public void onClick(ItemClickAction itemClickAction) {
                            Map<String, Object> info = new HashMap<>();
                            info.put("arena", e.getKey());
                            SetupStartGui.this
                                    .plugin
                                    .getGuiHandler()
                                    .getGui("TYPE_SETUP")
                                    .open(itemClickAction, info, itemClickAction.getPlayer());
                        }
                    });

            this.menu.addItem(item);
        }
    }
}