package org.zibble.dbedwars.game.arena.view.shoptest;

import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.radium.utils.itemstack.ItemMetaBuilder;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.objects.serializable.LEnchant;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.configuration.configurable.ConfigurableShop;
import org.zibble.dbedwars.utils.ConfigurationUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ShopView implements org.zibble.dbedwars.api.game.view.ShopView {

    private final ArenaPlayer player;
    private final Map<String, org.zibble.dbedwars.api.game.view.ShopPage> shopPages;
    private org.zibble.dbedwars.api.game.view.ShopPage defaultPage;

    public ShopView(ArenaPlayer player) {
        this.player = player;
        this.shopPages = new LinkedHashMap<>();
    }

    public void loadFromConfig(ConfigurableShop cfgShop) {
        Map<String, GuiItem> commons = new HashMap<>();
        cfgShop.getCommonItems()
                .forEach(
                        (s, item) -> {
                            GuiItem guiItem = new GuiItem(s, this.getFormattedItem(item));
                            guiItem.loadFromConfig(null, this, commons, item, null);
                            commons.put(s, guiItem);
                        });
        cfgShop.getPages()
                .forEach(
                        (s, page) -> {
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
    public org.zibble.dbedwars.api.game.view.ShopPage getDefaultPage() {
        return this.defaultPage;
    }

    @Override
    public void setDefaultPage(org.zibble.dbedwars.api.game.view.ShopPage defaultPage) {
        this.defaultPage = defaultPage;
    }

    @Override
    public Map<String, org.zibble.dbedwars.api.game.view.ShopPage> getShopPages() {
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
