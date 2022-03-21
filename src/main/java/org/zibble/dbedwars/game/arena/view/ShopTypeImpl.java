package org.zibble.dbedwars.game.arena.view;

import org.apache.commons.lang.math.NumberUtils;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.view.ShopType;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.objects.serializable.LEnchant;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableShop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;

public class ShopTypeImpl implements ShopType {

    private final Key<String> key;
    private PageImpl defaultPage;
    private Map<Key<String>, PageImpl> pages = new HashMap<>();

    public static ShopTypeImpl fromConfig(ConfigurableShop config) {
        ShopTypeImpl shopType = new ShopTypeImpl(config.getName());
        for (Map.Entry<String, ConfigurableShop.ConfigurablePage> entry : config.getPages().entrySet()) {
            shopType.pages.put(Key.of(entry.getKey()), PageImpl.fromConfig(entry.getKey(), entry.getValue()));
        }
        shopType.defaultPage = shopType.pages.getOrDefault(Key.of(config.getDefaultPage()), shopType.pages.values().iterator().next());
        return shopType;
    }

    public ShopTypeImpl(String key) {
        this.key = Key.of(key);
    }

    @Override
    public Key<String> getKey() {
        return this.key;
    }

    @Override
    public Page getDefaultPage() {
        return this.defaultPage;
    }

    @Override
    public Map<Key<String>, PageImpl> getPages() {
        return pages;
    }

    public static class PageImpl implements ShopType.Page {

        private final String key;
        private int row;
        private Message title;
        private String mask[];
        private Map<Character, Item> items;

        public static PageImpl fromConfig(String key, ConfigurableShop.ConfigurablePage config) {
            String[] mask = new String[config.getPattern().size()];
            for (int i = 0; i < config.getPattern().size(); i++) {
                mask[i] = config.getPattern().get(i).replace(" ", "");
            }
            PageImpl page = new PageImpl(key, config.getPattern().size(), ConfigMessage.from(config.getTitle()), mask);
            for (Map.Entry<String, ConfigurableShop.ConfigurablePage.ConfigurableItem> entry : config.getItems().entrySet()) {
                page.items.put(entry.getKey().charAt(0), Item.fromConfig(entry.getValue()));
            }
            return page;
        }

        public PageImpl(String key, int row, Message title, String[] mask) {
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

        public Map<Character, Item> getItems() {
            return items;
        }

    }

    public static class Item {

        private Function<Placeholder[], BwItemStack> item;
        private TierGroup tierGroup;
        private Set<String> useConditions;
        private Set<String> actions;

        public static Item fromConfig(ConfigurableShop.ConfigurablePage.ConfigurableItem config) {
            Matcher matcher = BwItemStack.JSON_MATCHER.matcher(config.getMaterial());
            Item item;
            if (matcher.matches()) {
                Json json = DBedwars.getInstance().getConfigHandler().getJsonItem().get(matcher.group("item"));
                if (json == null) return null;
                item = new Item((placeholders) -> {
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
                item = new Item(itemStack);
            }
            item.useConditions.addAll(config.getConditions());
            item.actions.addAll(config.getActions());
            return item;
        }

        public Item(Json item) {
            this((placeholders) -> BwItemStack.fromJson(item, placeholders));
        }

        public Item(BwItemStack item) {
            this((placeholders) -> item);
        }

        public Item(Function<Placeholder[], BwItemStack> item) {
            this.item = item;
            this.useConditions = new HashSet<>();
            this.actions = new HashSet<>();
        }

        public Function<Placeholder[], BwItemStack> getItemFunction() {
            return item;
        }

        public TierGroup getTierGroup() {
            return tierGroup;
        }

        public Set<String> getUseConditions() {
            return useConditions;
        }

        public Set<String> getActions() {
            return actions;
        }

    }

}
