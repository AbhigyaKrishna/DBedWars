package me.abhigya.dbedwars.api.util.gui;

import me.Abhigya.core.menu.anvil.AnvilMenu;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class IAnvilMenu {

  private final String identifier;
  protected AnvilMenu menu;

  protected IAnvilMenu(String identifier, AnvilMenu menu) {
    this.identifier = identifier;
    this.menu = menu;
  }

  protected abstract void setUpMenu(
      Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info);

  public void open(
      Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
    this.setUpMenu(player, action, info);
    this.menu.open(player);
  }

  public String getIdentifier() {
    return identifier;
  }

  public AnvilMenu getMenu() {
    return menu;
  }
}
