package com.pepedevs.dbedwars.game.arena.view.shoptest;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.events.PlayerPurchaseItemEvent;
import com.pepedevs.dbedwars.api.exceptions.OverrideException;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.view.AttributeType;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.Overridable;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableShop;
import com.pepedevs.dbedwars.guis.ShopGui;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class GuiItem extends com.pepedevs.dbedwars.api.game.view.GuiItem {

    private final String key;
    private com.pepedevs.dbedwars.api.game.view.ShopPage shopPage;
    private final Map<AttributeType, com.pepedevs.dbedwars.api.game.view.Attribute> attributes;
    private final List<com.pepedevs.dbedwars.api.game.view.ShopItem> item;
    private com.pepedevs.dbedwars.api.game.view.GuiItem nextTier;
    private String nextPage;
    private final List<String> command;
    private boolean loaded;

    public GuiItem(String key, BwItemStack icon) {
        super(icon.toItemStack());
        this.key = key;
        this.attributes = new ConcurrentHashMap<>();
        this.item = new ArrayList<>();
        this.command = new ArrayList<>();
    }

    public void loadFromConfig(
            ShopPage page,
            ShopView view,
            Map<String, GuiItem> common,
            ConfigurableShop.ConfigurablePage.BwGUIItem item,
            ConfigurableShop.ConfigurablePage cfgPage) {
        if (this.isLoaded()) return;

        this.shopPage = page;
        ConfigurationUtils.getAttributeTypes(item.getAttribute().getAttributeType())
                .forEach(
                        t -> {
                            Attribute attribute = new Attribute(t);
                            attribute.loadFromConfig(item.getAttribute());
                            this.attributes.put(t, attribute);
                        });
        this.attributes.computeIfPresent(
                AttributeType.PURCHASABLE,
                (type, attribute) -> {
                    Set<ItemStack> cost =
                            ConfigurationUtils.parseCost(
                                    (String) attribute.getKeyEntry().get("cost"));
                    boolean b = this.attributes.containsKey(AttributeType.PERMANENT);
                    boolean c = this.attributes.containsKey(AttributeType.AUTO_EQUIP);
                    Map<String, Integer> index = new HashMap<>();
                    this.attributes.computeIfPresent(
                            AttributeType.AUTO_EQUIP,
                            (attributeType, atr) -> {
                                atr.getKeyEntry()
                                        .forEach(
                                                (s, o) ->
                                                        index.put(
                                                                s.replace("item:auto-equip-", ""),
                                                                (int) o));
                                return atr;
                            });
                    attribute.getKeyEntry().entrySet().stream()
                            .filter(entry -> entry.getKey().startsWith("item-"))
                            .forEach(
                                    entry -> {
                                        String key =
                                                entry.getKey()
                                                        .replace("item-", "")
                                                        .replace("true-", "")
                                                        .replace("false-", "");
                                        boolean isColorable =
                                                Boolean.parseBoolean(entry.getKey().split("-")[1]);
                                        BwItemStack i = (BwItemStack) entry.getValue();
                                        ShopItem si = new ShopItem(key, i);
                                        if (b) si.setPermanent(true);
                                        if (c) si.setAutoEquip(true);
                                        si.setColorable(isColorable);
                                        si.getCost().addAll(cost);
                                        si.setSlot(index.getOrDefault(key, 0));
                                    });

                    return attribute;
                });
        this.attributes.computeIfPresent(
                AttributeType.CHANGE_PAGE,
                (type, attribute) -> {
                    this.nextPage = (String) attribute.getKeyEntry().get("page");
                    return attribute;
                });
        this.attributes.computeIfPresent(
                AttributeType.COMMAND,
                (type, attribute) -> {
                    this.getCommand().addAll((List<String>) attribute.getKeyEntry().get("command"));
                    return attribute;
                });
        if (page == null) return;
        this.attributes.computeIfPresent(
                AttributeType.UPGRADEABLE_TIER,
                (type, attribute) -> {
                    String nextTier =
                            (String)
                                    attribute
                                            .getKeyEntry()
                                            .get(AttributeType.UPGRADEABLE_TIER.getKeys()[0]);
                    com.pepedevs.dbedwars.api.game.view.GuiItem guiItem =
                            common.getOrDefault(nextTier, null);
                    if (cfgPage.getItems().containsKey(nextTier)) {
                        if (guiItem != null) {
                            guiItem = guiItem.clone();
                            if (this.shopPage.getItems().containsKey(nextTier)) {
                                try {
                                    guiItem.override(this.shopPage.getItems().get(nextTier));
                                } catch (OverrideException ignored) {
                                }
                            }
                        } else {
                            guiItem = this.shopPage.getItems().getOrDefault(nextTier, null);
                            if (guiItem == null) {
                                ConfigurableShop.ConfigurablePage.BwGUIItem next =
                                        cfgPage.getItems().get(nextTier);
                                guiItem = new GuiItem(nextTier, view.getFormattedItem(next));
                                ((GuiItem) guiItem).loadFromConfig(page, view, common, next, cfgPage);
                                this.shopPage.getItems().put(nextTier, guiItem);
                            }
                        }
                    }
                    this.setNextTier(guiItem);

                    return attribute;
                });
        this.loaded = true;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public Map<AttributeType, com.pepedevs.dbedwars.api.game.view.Attribute> getAttributes() {
        return this.attributes;
    }

    @Override
    public void onClick(ItemClickAction action) {
        System.out.println("SHOP PAGE" + this.shopPage);
        System.out.println("SHOP VIEW" + this.shopPage.getView());
        ArenaPlayer ap = this.shopPage.getView().getPlayer();
        if (this.attributes.containsKey(AttributeType.CHANGE_PAGE)) {
            this.changePage(action);
        }
        if (this.attributes.containsKey(AttributeType.PURCHASABLE)) {
            Optional<com.pepedevs.dbedwars.api.game.view.ShopItem> optional =
                    this.item.stream().findFirst();
            if (!optional.isPresent()) return;

            if (optional.get().isCostFullFilled(action.getPlayer())) {
                PlayerPurchaseItemEvent event =
                        new PlayerPurchaseItemEvent(
                                ap, ap.getArena(), optional.get().getCost(), this.item);
                event.call();

                if (!event.isCancelled()) event.getItems().forEach(i -> i.onPurchase(ap));
            }

            if (this.attributes.containsKey(AttributeType.UPGRADEABLE_TIER))
                this.upgradeTier(action);
        }
        if (this.attributes.containsKey(AttributeType.COMMAND)) {
            this.runCommand(action.getPlayer());
        }
    }

    @Override
    public com.pepedevs.dbedwars.api.game.view.ShopPage getPage() {
        return this.shopPage.getView().getShopPages().getOrDefault(this.nextPage, null);
    }

    @Override
    public void setPage(com.pepedevs.dbedwars.api.game.view.ShopPage page) {
        this.nextPage = page.getKey();
    }

    @Override
    public void changePage(ItemClickAction action) {
        Map<String, Object> info = new HashMap<>();
        info.put("player", this.shopPage.getView().getPlayer());
        info.put("page", this.getPage());
        ((ShopGui) DBedwars.getInstance().getGuiHandler().getGuis().get("SHOP"))
                .setUpMenu(action.getPlayer(), action, info);
        action.getMenu().update(action.getPlayer());
        action.setUpdate(true);
    }

    @Override
    public List<String> getCommand() {
        return this.command;
    }

    @Override
    public void runCommand(Player player) {
        this.getCommand()
                .forEach(
                        c -> {
                            if (c.startsWith("[CONSOLE]")) {
                                Bukkit.getServer()
                                        .dispatchCommand(
                                                Bukkit.getConsoleSender(),
                                                c.replace("[CONSOLE]", "").trim());
                            } else if (c.startsWith("[PLAYER]")) {
                                player.performCommand(c.replace("[PLAYER]", "").trim());
                            }
                        });
    }

    @Override
    public com.pepedevs.dbedwars.api.game.view.GuiItem getNextTier() {
        return this.nextTier;
    }

    @Override
    public void setNextTier(com.pepedevs.dbedwars.api.game.view.GuiItem nextTier) {
        this.nextTier = nextTier;
    }

    @Override
    public void upgradeTier(ItemClickAction action) {
        if (this.getNextTier() == null) return;

        this.shopPage.getPattern()[action.getSlot() / 9][action.getSlot() % 9] = this.getNextTier();
    }

    @Override
    public GuiItem clone() {
        return new GuiItem(this.key, new BwItemStack(this.icon));
    }

    @Override
    public com.pepedevs.dbedwars.api.game.view.ShopPage getShopPage() {
        return this.shopPage;
    }

    @Override
    public void setShopPage(com.pepedevs.dbedwars.api.game.view.ShopPage shopPage) {
        this.shopPage = shopPage;
    }

    @Override
    public void override(Overridable override) throws OverrideException {}
}
