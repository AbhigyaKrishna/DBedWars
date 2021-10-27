package me.abhigya.dbedwars.guis.setup;

import me.Abhigya.core.menu.inventory.ItemMenu;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.Abhigya.core.menu.inventory.item.action.ActionItem;
import me.Abhigya.core.menu.inventory.item.action.ItemAction;
import me.Abhigya.core.menu.inventory.item.action.ItemActionPriority;
import me.Abhigya.core.menu.inventory.size.ItemMenuSize;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.util.gui.IMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@SuppressWarnings("unchecked")
public class TeamSizeGui extends IMenu<ItemMenu> {

  private final DBedwars plugin;

  public TeamSizeGui(DBedwars plugin) {
    super(
        "TEAM_SIZE_SETUP",
        new ItemMenu(
            StringUtils.translateAlternateColorCodes("&7Select Number of Teams"),
            ItemMenuSize.THREE_LINE,
            null));
    this.plugin = plugin;
  }

  @Override
  protected void setUpMenu(
      Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
    Arena arena = (Arena) info.get("arena");

    if (action != null) {
      this.menu.setParent(action.getMenu());
      this.menu.setItem(18, BACK);
    }

    ActionItem main =
        new ActionItem(
            StringUtils.translateAlternateColorCodes("&a&l" + arena.getSettings().getTeamPlayers()),
            XMaterial.ARMOR_STAND.parseItem());
    main.getIcon().setAmount(arena.getSettings().getTeamPlayers());
    this.menu.setItem(13, main);

    ActionItem add =
        new ActionItem(
            StringUtils.translateAlternateColorCodes("&eAdd &f&l1"),
            XMaterial.LIME_WOOL.parseItem(),
            StringUtils.translateAlternateColorCodes("&fClick to add 1"));
    add.addAction(
        new ItemAction() {
          @Override
          public ItemActionPriority getPriority() {
            return ItemActionPriority.HIGH;
          }

          @Override
          public void onClick(ItemClickAction itemClickAction) {
            TeamSizeGui.this.addOne(main, arena, arena.getSettings().getTeamPlayers());
            TeamSizeGui.this.menu.update(itemClickAction.getPlayer());
          }
        });
    menu.setItem(12, add);

    ActionItem sub =
        new ActionItem(
            StringUtils.translateAlternateColorCodes("&eRemove &f&l1"),
            XMaterial.RED_WOOL.parseItem(),
            StringUtils.translateAlternateColorCodes("&fClick to subtract 1"));
    sub.addAction(
        new ItemAction() {
          @Override
          public ItemActionPriority getPriority() {
            return ItemActionPriority.HIGH;
          }

          @Override
          public void onClick(ItemClickAction itemClickAction) {
            TeamSizeGui.this.subOne(main, arena, arena.getSettings().getTeamPlayers());
            TeamSizeGui.this.menu.update(itemClickAction.getPlayer());
          }
        });
    menu.setItem(14, sub);
  }

  private void addOne(ActionItem main, Arena arena, int size) {
    arena.getSettings().setTeamPlayers(++size);
    main.getIcon().setAmount(size);
    main.setName(StringUtils.translateAlternateColorCodes("&a&l" + size));
  }

  private void subOne(ActionItem main, Arena arena, int size) {
    arena.getSettings().setTeamPlayers(--size);
    main.getIcon().setAmount(size);
    main.setName(StringUtils.translateAlternateColorCodes("&a&l" + size));
  }
}
