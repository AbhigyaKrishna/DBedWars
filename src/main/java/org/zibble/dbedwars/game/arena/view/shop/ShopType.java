package org.zibble.dbedwars.game.arena.view.shop;

import org.apache.commons.lang.math.NumberUtils;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.messaging.Placeholder;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.LEnchant;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.Keyed;
import org.zibble.dbedwars.api.util.NewBwItemStack;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableShop;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;

public class ShopType implements Keyed<String> {

    private final Key<String> key;
    private Pair<Key<String>, Page> defaultPage;
    private Map<Key<String>, Page> pages = new HashMap<>();

    public static ShopType fromConfig(ConfigurableShop config) {
        ShopType shopType = new ShopType(config.getName());
        for (Map.Entry<String, ConfigurableShop.ConfigurablePage> entry : config.getPages().entrySet()) {
            shopType.pages.put(Key.of(entry.getKey()), Page.fromConfig(entry.getValue()));
        }
        if (config.getDefaultPage() != null && shopType.pages.containsKey(Key.of(config.getDefaultPage()))) {
            shopType.defaultPage = Pair.of(Key.of(config.getDefaultPage()), shopType.pages.get(Key.of(config.getDefaultPage())));
        } else {
            Map.Entry<Key<String>, Page> entry = shopType.pages.entrySet().iterator().next();
            shopType.defaultPage = Pair.of(entry.getKey(), entry.getValue());
        }
        return shopType;
    }

    public ShopType(String key) {
        this.key = Key.of(key);
    }

    public Pair<Key<String>, Page> getDefaultPage() {
        return defaultPage;
    }

    public Map<Key<String>, Page> getPages() {
        return pages;
    }

    @Override
    public Key<String> getKey() {
        return this.key;
    }

    public static class Page {

        private int row;
        private Message title;
        private String mask[];
        private Map<Character, Item> items;

        public static Page fromConfig(ConfigurableShop.ConfigurablePage config) {
            String[] mask = new String[config.getPattern().size()];
            for (int i = 0; i < config.getPattern().size(); i++) {
                mask[i] = config.getPattern().get(i).replace(" ", "");
            }
            Page page = new Page(config.getPattern().size(), ConfigMessage.from(config.getTitle()), mask);
            for (Map.Entry<String, ConfigurableShop.ConfigurablePage.ConfigurableItem> entry : config.getItems().entrySet()) {
                page.items.put(entry.getKey().charAt(0), Item.fromConfig(entry.getValue()));
            }
            return page;
        }

        public Page(int row, Message title, String[] mask) {
            this.row = row;
            this.title = title;
            this.mask = mask;
            this.items = new HashMap<>();
        }

        public int getRow() {
            return row;
        }

        public Message getTitle() {
            return title;
        }

        public String[] getMask() {
            return mask;
        }

        public Map<Character, Item> getItems() {
            return items;
        }

    }

    public static class Item {

        private Function<Placeholder[], NewBwItemStack> item;
        private TierGroup tierGroup;
        private Set<String> useConditions;
        private Set<String> actions;

        public static Item fromConfig(ConfigurableShop.ConfigurablePage.ConfigurableItem config) {
            Matcher matcher = NewBwItemStack.JSON_MATCHER.matcher(config.getMaterial());
            Item item;
            if (matcher.matches()) {
                Json json = DBedwars.getInstance().getConfigHandler().getJsonItem().get(matcher.group("item"));
                if (json == null) return null;
                item = new Item((placeholders) -> {
                    NewBwItemStack i = NewBwItemStack.fromJson(json);
                    if (matcher.group("amount") != null) {
                        i.setAmount(NumberUtils.toInt(matcher.group("amount")));
                    }
                    return i;
                });
            } else {
                NewBwItemStack itemStack = NewBwItemStack.valueOf(config.getMaterial());
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
            return item;
        }

        public Item(Json item) {
            this.item = (placeholders) -> NewBwItemStack.fromJson(item, placeholders);
        }

        public Item(NewBwItemStack item) {
            this.item = (placeholders) -> item;
        }

        public Item(Function<Placeholder[], NewBwItemStack> item) {
            this.item = item;
        }

        public Function<Placeholder[], NewBwItemStack> getItemFunction() {
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
