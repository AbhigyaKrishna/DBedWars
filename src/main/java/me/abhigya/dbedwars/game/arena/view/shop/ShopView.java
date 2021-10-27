package me.abhigya.dbedwars.game.arena.view.shop;

import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.exceptions.OverrideException;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.util.Overridable;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableShop;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShopView implements me.abhigya.dbedwars.api.game.view.ShopView {

  private final ArenaPlayer player;
  private final ConfigurableShop cfgShop;
  private final String defaultPage;
  private final Map<String, me.abhigya.dbedwars.api.game.view.ViewItem> commons;
  private final Map<String, ShopPage> shopPages;

  public ShopView(ArenaPlayer player) {
    this.player = player;
    this.cfgShop = DBedwars.getInstance().getConfigHandler().getShop();
    this.defaultPage = cfgShop.getDefaultPage();
    this.commons = new ConcurrentHashMap<>();
    this.shopPages = new LinkedHashMap<>();
  }

  public void load() {
    this.cfgShop
        .getCommonItems()
        .forEach(
            (s, item) -> {
              ViewItem viewItem = new ViewItem(this.player, null, item, s);
              this.commons.put(s, viewItem);
            });
    this.cfgShop
        .getPages()
        .forEach(
            (s, page) -> {
              ShopPage shopPage = new ShopPage(s, page, this.player, this.commons);
              shopPage.load();
              this.shopPages.put(s, shopPage);
            });
  }

  @Override
  public ArenaPlayer getPlayer() {
    return player;
  }

  @Override
  public String getDefaultPage() {
    return defaultPage;
  }

  @Override
  public Map<String, me.abhigya.dbedwars.api.game.view.ViewItem> getCommons() {
    return this.commons;
  }

  @Override
  public Map<String, ShopPage> getShopPages() {
    return this.shopPages;
  }

  @Override
  public void override(Overridable override) throws OverrideException {}
}
