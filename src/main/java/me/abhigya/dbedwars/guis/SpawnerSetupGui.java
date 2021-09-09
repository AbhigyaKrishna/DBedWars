package me.abhigya.dbedwars.guis;

import me.Abhigya.core.menu.inventory.Item;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.Abhigya.core.menu.inventory.custom.book.BookItemMenu;
import me.Abhigya.core.menu.inventory.item.action.ActionItem;
import me.Abhigya.core.menu.inventory.item.action.ItemAction;
import me.Abhigya.core.menu.inventory.item.action.ItemActionPriority;
import me.Abhigya.core.menu.inventory.item.voidaction.VoidActionItem;
import me.Abhigya.core.menu.inventory.size.ItemMenuSize;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.gui.IMenu;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class SpawnerSetupGui extends IMenu<BookItemMenu> {

    protected SpawnerSetupGui(DBedwars plugin) {
        super(plugin, "SPAWNER_SETUP", new BookItemMenu(StringUtils.translateAlternateColorCodes("&eSpawner Setup"),
                ItemMenuSize.THREE_LINE, ItemMenuSize.ONE_LINE, null));
    }

    @Override
    protected void setUpMenu(Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
        this.menu.clear();
        Arena arena = (Arena) info.get("arena");
        boolean teamSpawner = (boolean) info.get("team-spawner");
        Set<DropType> dropTypes = this.getPlugin().getGameManager().getDropTypes();
        this.menu.setParent(action.getMenu());

        Item bar = new VoidActionItem(StringUtils.translateAlternateColorCodes("&7"), XMaterial.WHITE_STAINED_GLASS_PANE.parseItem());
        for (byte b = 0; b < 9; b++) {
            switch (b) {
                case 2:
                    this.menu.setBarButton(b, PREVIOUS_PAGE);
                    break;
                case 4:
                    this.menu.setBarButton(b, BACK);
                    break;
                case 6:
                    this.menu.setBarButton(b, NEXT_PAGE);
                    break;
                default:
                    this.menu.setBarButton(b, bar);
            }
        }

        if (dropTypes.size() == 0) {
            Item[] items = new Item[18];
            for (byte i = 0; i < 18; i++) {
                items[i] = VOID_ITEM;
            }
            ActionItem item = new ActionItem(StringUtils.translateAlternateColorCodes("&cNo spawners found!"),
                    XMaterial.PAPER.parseItem(), StringUtils.translateAlternateColorCodes("&cPlease configure some to continue!"));
            items[4] = item;
            this.menu.addItemsNotFull(items);
            return;
        }

        Item[] items = new Item[dropTypes.size()];
        byte i = 0;
        for (DropType d : dropTypes) {
            if (teamSpawner != d.isTeamSpawner())
                continue;

            StringBuilder builder = new StringBuilder("&bMaterial: ");
            for (DropType.Drop drop : d.getTier(1).getDrops()) {
                builder.append(drop.getKey()).append(", ");
            }
            builder.delete(builder.length() - 1, builder.length());

            ActionItem item = new ActionItem(StringUtils.translateAlternateColorCodes("&6" + d.getSimpleName()), d.getIcon().toItemStack(),
                    StringUtils.translateAlternateColorCodes(new String[]{"&bId: " + d.getId(), builder.toString()}));
            item.addAction(new ItemAction() {
                @Override
                public ItemActionPriority getPriority() {
                    return ItemActionPriority.NORMAL;
                }

                @Override
                public void onClick(ItemClickAction itemClickAction) {
                    LocationXYZ loc = LocationXYZ.valueOf(itemClickAction.getPlayer().getLocation());
                    if (!teamSpawner)
                        arena.getSettings().addDrop(d, loc);
                    else
                        ((Team) info.get("team")).addSpawner(d, loc);
                    itemClickAction.getPlayer().sendMessage(StringUtils.translateAlternateColorCodes("&aAdded " + d.getSimpleName() + " to " + loc.toString()));
                }
            });

            items[i] = item;
            i++;
        }

        this.menu.addItems(items);
    }
}
