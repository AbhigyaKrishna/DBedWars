package org.zibble.dbedwars.game.arena.view.shoptest;

import com.pepedevs.radium.gui.inventory.action.ItemClickAction;
import com.pepedevs.radium.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.events.PlayerPurchaseItemEvent;
import org.zibble.dbedwars.api.exceptions.OverrideException;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.view.AttributeType;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.Overridable;
import org.zibble.dbedwars.configuration.configurable.ConfigurableShop;
import org.zibble.dbedwars.guis.ShopGui;
import org.zibble.dbedwars.utils.ConfigurationUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class GuiItem extends org.zibble.dbedwars.api.game.view.GuiItem {

    private final String key;
    private org.zibble.dbedwars.api.game.view.ShopPage shopPage;
    private Map<AttributeType, org.zibble.dbedwars.api.game.view.Attribute> attributes;
    private List<org.zibble.dbedwars.api.game.view.ShopItem> item;
    private org.zibble.dbedwars.api.game.view.GuiItem nextTier;
    private String nextPage;
    private List<String> command;
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
                                        this.item.add(si);
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
                    org.zibble.dbedwars.api.game.view.GuiItem guiItem =
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
                                ((GuiItem) guiItem)
                                        .loadFromConfig(page, view, common, next, cfgPage);
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
    public Map<AttributeType, org.zibble.dbedwars.api.game.view.Attribute> getAttributes() {
        return this.attributes;
    }

    @Override
    public void onClick(ItemClickAction action) {
        ArenaPlayer ap = this.shopPage.getView().getPlayer();
        if (this.attributes.containsKey(AttributeType.CHANGE_PAGE)) {
            this.changePage(action);
        }
        if (this.attributes.containsKey(AttributeType.PURCHASABLE)) {
            Optional<org.zibble.dbedwars.api.game.view.ShopItem> optional =
                    this.item.stream().findFirst();
            optional.ifPresent(
                    si -> {
                        if (si.isCostFullFilled(action.getPlayer())) {
                            PlayerPurchaseItemEvent event =
                                    new PlayerPurchaseItemEvent(
                                            ap, ap.getArena(), si.getCost(), this.item);
                            event.call();

                            if (!event.isCancelled())
                                event.getItems().forEach(i -> i.onPurchase(ap));
                        } else {
                            ap.getPlayer()
                                    .sendMessage(
                                            StringUtils.translateAlternateColorCodes(
                                                    "&cYou don't have required items!"));
                        }

                        if (this.attributes.containsKey(AttributeType.UPGRADEABLE_TIER))
                            this.upgradeTier(action);
                    });
        }
        if (this.attributes.containsKey(AttributeType.COMMAND)) {
            this.runCommand(action.getPlayer());
        }
    }

    @Override
    public org.zibble.dbedwars.api.game.view.ShopPage getPage() {
        return this.shopPage.getView().getShopPages().getOrDefault(this.nextPage, null);
    }

    @Override
    public void setPage(org.zibble.dbedwars.api.game.view.ShopPage page) {
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
    public org.zibble.dbedwars.api.game.view.GuiItem getNextTier() {
        return this.nextTier;
    }

    @Override
    public void setNextTier(org.zibble.dbedwars.api.game.view.GuiItem nextTier) {
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
    public org.zibble.dbedwars.api.game.view.ShopPage getShopPage() {
        return this.shopPage;
    }

    @Override
    public void setShopPage(org.zibble.dbedwars.api.game.view.ShopPage shopPage) {
        this.shopPage = shopPage;
    }

    @Override
    public void override(Overridable override) throws OverrideException {
        if (!(override instanceof GuiItem)) throw new OverrideException("Invalid override type!");

        GuiItem item = (GuiItem) override;
        if (!item.isLoaded())
            throw new OverrideException("Can't override item which isn't loaded!");

        this.shopPage = item.shopPage;
        this.attributes = new ConcurrentHashMap<>(item.attributes);
        this.item = new ArrayList<>(item.item);
        this.nextTier = item.nextTier;
        this.nextPage = item.nextPage;
        this.command = new ArrayList<>(item.command);
        this.loaded = true;
    }
}
