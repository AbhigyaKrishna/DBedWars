package org.zibble.dbedwars.game.arena.view;

import org.apache.commons.lang.math.NumberUtils;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.view.ShopInfo;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.objects.serializable.LEnchant;
import org.zibble.dbedwars.api.util.ArrayFunction;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableShop;

import java.util.*;
import java.util.regex.Matcher;

public class ShopInfoImpl implements ShopInfo {

    private final Key<String> key;
    private PageInfoImpl defaultPage;
    private Map<Key<String>, PageInfoImpl> pages = new HashMap<>();

    public static ShopInfoImpl fromConfig(ConfigurableShop config) {
        ShopInfoImpl shopType = new ShopInfoImpl(config.getName());
        for (Map.Entry<String, ConfigurableShop.ConfigurablePage> entry : config.getPages().entrySet()) {
            shopType.pages.put(Key.of(entry.getKey()), PageInfoImpl.fromConfig(entry.getKey(), entry.getValue()));
        }
        shopType.defaultPage = shopType.pages.getOrDefault(Key.of(config.getDefaultPage()), shopType.pages.values().iterator().next());
        return shopType;
    }

    public ShopInfoImpl(String key) {
        this.key = Key.of(key);
    }

    @Override
    public Key<String> getKey() {
        return this.key;
    }

    @Override
    public PageInfo getDefaultPage() {
        return this.defaultPage;
    }

    @Override
    public Map<Key<String>, PageInfoImpl> getPages() {
        return pages;
    }

    public static class PageInfoImpl implements PageInfo {

        private final String key;
        private int row;
        private Message title;
        private String mask[];
        private Map<Character, ItemInfoImpl> items;

        public static PageInfoImpl fromConfig(String key, ConfigurableShop.ConfigurablePage config) {
            String[] mask = new String[config.getPattern().size()];
            for (int i = 0; i < config.getPattern().size(); i++) {
                mask[i] = config.getPattern().get(i).replace(" ", "");
            }
            PageInfoImpl page = new PageInfoImpl(key, config.getPattern().size(), ConfigMessage.from(config.getTitle()), mask);
            for (Map.Entry<String, ConfigurableShop.ConfigurablePage.ConfigurableItem> entry : config.getItems().entrySet()) {
                page.items.put(entry.getKey().charAt(0), ItemInfoImpl.fromConfig(entry.getValue()));
            }
            return page;
        }

        public PageInfoImpl(String key, int row, Message title, String[] mask) {
            this.key = key;
            this.row = row;
            this.title = title;
            this.mask = mask;
            this.items = new HashMap<>();
        }

        @Override
        public Key<String> getKey() {
            return Key.of(this.key);
        }

        @Override
        public int getRow() {
            return row;
        }

        @Override
        public Message getTitle() {
            return title;
        }

        @Override
        public String[] getMask() {
            return mask;
        }

        @Override
        public Map<Character, ItemInfoImpl> getItems() {
            return items;
        }

    }

    public static class ItemInfoImpl implements ItemInfo {

        private ArrayFunction<Placeholder, BwItemStack> item;
        private TierGroupInfoImpl tierGroup;
        private Set<String> useConditions;
        private Set<String> actions;

        public static ItemInfoImpl fromConfig(ConfigurableShop.ConfigurablePage.ConfigurableItem config) {
            Matcher matcher = BwItemStack.JSON_MATCHER.matcher(config.getMaterial());
            ItemInfoImpl item;
            if (matcher.matches()) {
                Json json = DBedwars.getInstance().getConfigHandler().getJsonItem().get(matcher.group("item"));
                if (json == null) return null;
                item = new ItemInfoImpl((placeholders) -> {
                    BwItemStack i = BwItemStack.fromJson(json);
                    if (matcher.group("amount") != null) {
                        i.setAmount(NumberUtils.toInt(matcher.group("amount")));
                    }
                    return i;
                });
            } else {
                BwItemStack itemStack = BwItemStack.valueOf(config.getMaterial());
                if (itemStack == null) return null;
                itemStack.setDisplayName(ConfigMessage.from(config.getName()));
                itemStack.setLore(ConfigMessage.from(config.getLore().toArray(new String[0])));
                for (String s : config.getEnchantment()) {
                    LEnchant enchant = LEnchant.valueOf(s);
                    if (enchant == null || !enchant.getEnchantment().isSupported()) continue;
                    itemStack.addEnchantment(enchant);
                }
                item = new ItemInfoImpl(itemStack);
            }
            item.useConditions.addAll(config.getConditions());
            item.actions.addAll(config.getActions());
            return item;
        }

        public ItemInfoImpl(Json item) {
            this((placeholders) -> BwItemStack.fromJson(item, placeholders));
        }

        public ItemInfoImpl(BwItemStack item) {
            this((placeholders) -> item);
        }

        public ItemInfoImpl(ArrayFunction<Placeholder, BwItemStack> item) {
            this.item = item;
            this.useConditions = new HashSet<>();
            this.actions = new HashSet<>();
        }

        @Override
        public ArrayFunction<Placeholder, BwItemStack> getItemFunction() {
            return item;
        }

        @Override
        public TierGroupInfo getTierGroup() {
            return tierGroup;
        }

        public Set<String> getUseConditions() {
            return useConditions;
        }

        public Set<String> getActions() {
            return actions;
        }

    }

    public static class TierGroupInfoImpl implements TierGroupInfo {

        private final TreeMap<Integer, ItemInfoImpl> tiers;
        private int tier = 0;

        public TierGroupInfoImpl() {
            this.tiers = new TreeMap<>(Integer::compare);
        }

        public void addItem(ShopInfoImpl.ItemInfoImpl item) {
            this.tiers.put(this.tier++, item);
        }

        @Override
        public ShopInfo.ItemInfo getItem(int tier) {
            return tiers.get(tier);
        }

        @Override
        public Integer[] getAllTiers() {
            return this.tiers.keySet().toArray(new Integer[0]);
        }

    }

}
