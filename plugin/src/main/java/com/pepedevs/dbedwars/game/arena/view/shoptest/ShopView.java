package com.pepedevs.dbedwars.game.arena.view.shoptest;

import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.LEnchant;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableShop;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.itemstack.ItemMetaBuilder;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ShopView implements com.pepedevs.dbedwars.api.game.view.ShopView {

    private final ArenaPlayer player;
    private com.pepedevs.dbedwars.api.game.view.ShopPage defaultPage;
    private final Map<String, com.pepedevs.dbedwars.api.game.view.ShopPage> shopPages;

    public ShopView(ArenaPlayer player) {
        this.player = player;
        this.shopPages = new LinkedHashMap<>();
    }

    public void loadFromConfig(ConfigurableShop cfgShop) {
        Map<String, GuiItem> commons = new HashMap<>();
        System.out.println(ChatColor.RED + "COMMON ITEMS -- " + cfgShop.getCommonItems().size());
        cfgShop.getCommonItems()
                .forEach(
                        (s, item) -> {
                            System.out.println("CITEM--" + item.getItemName());
                            GuiItem guiItem = new GuiItem(s, this.getFormattedItem(item));
                            guiItem.loadFromConfig(null, this, commons, item, null);
                            commons.put(s, guiItem);
                        });
        System.out.println(ChatColor.RED + "NO OF PAGES -- " + cfgShop.getPages().size());
        cfgShop.getPages()
                .forEach(
                        (s, page) -> {
                            System.out.println("LOADING PAGE==" + page.getGuiTitle());
                            ShopPage shopPage = new ShopPage(s);
                            shopPage.loadFromConfig(this, commons, page);
                            this.shopPages.put(s, shopPage);
                        });
        this.defaultPage = this.getShopPages().getOrDefault(cfgShop.getDefaultPage(), null);
    }

    @Override
    public ArenaPlayer getPlayer() {
        return this.player;
    }

    @Override
    public com.pepedevs.dbedwars.api.game.view.ShopPage getDefaultPage() {
        return this.defaultPage;
    }

    @Override
    public void setDefaultPage(com.pepedevs.dbedwars.api.game.view.ShopPage defaultPage) {
        this.defaultPage = defaultPage;
    }

    @Override
    public Map<String, com.pepedevs.dbedwars.api.game.view.ShopPage> getShopPages() {
        return this.shopPages;
    }

    public BwItemStack getFormattedItem(ConfigurableShop.ConfigurablePage.BwGUIItem item) {
        BwItemStack stack =
                ConfigurationUtils.parseItem(this.player.getTeam().getColor(), item.getMaterial());
        ItemMetaBuilder builder =
                stack.getItemMetaBuilder()
                        .withDisplayName(
                                StringUtils.translateAlternateColorCodes(item.getItemName()))
                        .withLore(StringUtils.translateAlternateColorCodes(item.getItemLore()));
        stack.setItemMetaBuilder(builder);
        item.getEnchant().stream()
                .map(LEnchant::valueOf)
                .filter(Objects::nonNull)
                .forEach(stack::applyEnchant);
        return stack;
    }
}
