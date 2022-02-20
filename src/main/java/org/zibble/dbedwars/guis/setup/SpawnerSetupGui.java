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
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.util.gui.IMenu;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class SpawnerSetupGui extends IMenu<BookItemMenu> {

    private final DBedwars plugin;

    protected SpawnerSetupGui(DBedwars plugin) {
        super(
                "SPAWNER_SETUP",
                new BookItemMenu(
                        StringUtils.translateAlternateColorCodes("&eSpawner Setup"),
                        ItemMenuSize.THREE_LINE,
                        ItemMenuSize.ONE_LINE,
                        null));
        this.plugin = plugin;
    }

    @Override
    protected void setUpMenu(
            Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
        this.menu.clear();
        Arena arena = (Arena) info.get("arena");
        boolean teamSpawner = (boolean) info.get("team-spawner");
        Set<DropType> dropTypes = this.plugin.getGameManager().getDropTypes();
        this.menu.setParent(action.getMenu());

        Item bar =
                new VoidActionItem(
                        StringUtils.translateAlternateColorCodes("&7"),
                        XMaterial.WHITE_STAINED_GLASS_PANE.parseItem());
        for (byte b = 0; b < 9; b++) {
            switch (b) {
                case 2:
                    this.menu.setBarButton(b, IMenu.PREVIOUS_PAGE);
                    break;
                case 4:
                    this.menu.setBarButton(b, IMenu.BACK);
                    break;
                case 6:
                    this.menu.setBarButton(b, IMenu.NEXT_PAGE);
                    break;
                default:
                    this.menu.setBarButton(b, bar);
            }
        }

        if (dropTypes.size() == 0) {
            Item[] items = new Item[18];
            for (byte i = 0; i < 18; i++) {
                items[i] = IMenu.VOID_ITEM;
            }
            ActionItem item =
                    new ActionItem(
                            StringUtils.translateAlternateColorCodes("&cNo spawners found!"),
                            XMaterial.PAPER.parseItem(),
                            StringUtils.translateAlternateColorCodes(
                                    "&cPlease configure some to continue!"));
            items[4] = item;
            this.menu.addItemsNotFull(items);
            return;
        }

        Item[] items = new Item[dropTypes.size()];
        byte i = 0;
        for (DropType d : dropTypes) {
            if (teamSpawner != d.isTeamSpawner()) continue;

            StringBuilder builder = new StringBuilder("&bMaterial: ");
            for (DropType.Drop drop : d.getTier(1).getDrops()) {
                builder.append(drop.getKey()).append(", ");
            }
            builder.delete(builder.length() - 1, builder.length());

            ActionItem item =
                    new ActionItem(
                            StringUtils.translateAlternateColorCodes("&6" + d.getSimpleName()),
                            d.getIcon().toItemStack(),
                            StringUtils.translateAlternateColorCodes(
                                    new String[] {"&bId: " + d.getId(), builder.toString()}));
            item.addAction(
                    new ItemAction() {
                        @Override
                        public ItemActionPriority getPriority() {
                            return ItemActionPriority.NORMAL;
                        }

                        @Override
                        public void onClick(ItemClickAction itemClickAction) {
                            LocationXYZ loc =
                                    LocationXYZ.valueOf(itemClickAction.getPlayer().getLocation());
                            if (!teamSpawner) arena.getSettings().addDrop(d, loc);
                            else ((Team) info.get("team")).addSpawner(d, loc);
                            itemClickAction
                                    .getPlayer()
                                    .sendMessage(
                                            StringUtils.translateAlternateColorCodes(
                                                    "&aAdded "
                                                            + d.getSimpleName()
                                                            + " to "
                                                            + loc.toString()));
                        }
                    });

            items[i] = item;
            i++;
        }

        this.menu.addItems(items);
    }
}
