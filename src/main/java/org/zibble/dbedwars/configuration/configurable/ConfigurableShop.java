package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableShop implements Loadable {

    @ConfigPath
    private String defaultPage;

    @ConfigPath
    private Map<String, ConfigurablePage> pages;

    @ConfigPath("common")
    private Map<String, ConfigurablePage.ConfigurableItem> commonItems;

    public ConfigurableShop() {
        this.pages = new HashMap<>();
        this.commonItems = new HashMap<>();
    }

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return !this.pages.isEmpty();
    }

    @Override
    public boolean isInvalid() {
        return !this.isValid();
    }

    public String getDefaultPage() {
        return defaultPage;
    }

    public Map<String, ConfigurablePage.ConfigurableItem> getCommonItems() {
        return commonItems;
    }

    public Map<String, ConfigurablePage> getPages() {
        return pages;
    }

    public static class ConfigurablePage implements Loadable {

        @ConfigPath
        private String title;

        @ConfigPath
        private List<String> pattern;

        @ConfigPath
        private Map<String, ConfigurableItem> items;

        public ConfigurablePage() {
            this.pattern = new ArrayList<>();
            this.items = new HashMap<>();
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return this.title != null && !this.pattern.isEmpty();
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
        }

        public Map<String, ConfigurableItem> getItems() {
            return items;
        }

        public List<String> getPattern() {
            return pattern;
        }

        public String getTitle() {
            return title;
        }

        public static class ConfigurableItem implements Loadable {

            @ConfigPath
            private String name;

            @ConfigPath
            private List<String> lore;

            @ConfigPath
            private String material;

            @ConfigPath
            private int amount;

            @ConfigPath
            private List<String> enchantment;

            @ConfigPath
            private ConfigurableAttribute attribute;

            public ConfigurableItem() {
                this.amount = 1;
                this.lore = new ArrayList<>();
                this.attribute = new ConfigurableAttribute();
                this.enchantment = new ArrayList<>();
            }

            @Override
            public void load(ConfigurationSection section) {
                this.loadEntries(section);
            }

            @Override
            public boolean isValid() {
                return true;
            }

            @Override
            public boolean isInvalid() {
                return false;
            }

            public List<String> getLore() {
                return lore;
            }

            public String getName() {
                return name;
            }

            public int getAmount() {
                return amount;
            }

            public String getMaterial() {
                return material;
            }

            public List<String> getEnchantment() {
                return enchantment;
            }

            public ConfigurableAttribute getAttribute() {
                return attribute;
            }

            @Override
            public String toString() {
                return "BwGUIItem{" +
                        "itemName='" + name + '\'' +
                        ", itemLore=" + lore +
                        ", material='" + material + '\'' +
                        ", amount=" + amount +
                        ", enchant=" + enchantment +
                        ", attribute=" + attribute +
                        '}';
            }

        }

        @Override
        public String toString() {
            return "ConfigurablePage{" +
                    "guiTitle='" + title + '\'' +
                    ", pattern=" + pattern +
                    ", items=" + items +
                    '}';
        }
    }

    public static class ConfigurableAttribute implements Loadable {

        @ConfigPath
        private String type;

        @ConfigPath("page")
        private String pageToChangeTo;

        @ConfigPath
        private String cost;

        @ConfigPath
        private String nextTier;

        @ConfigPath
        private String previousTier;

        @ConfigPath
        private String downgradeEvent;

        @ConfigPath
        private List<String> command;

        @ConfigPath("item")
        private Map<String, AttributeItems> itemsToGive;

        public ConfigurableAttribute() {
            itemsToGive = new HashMap<>();
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return this.type != null && this.cost != null;
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
        }

        public Map<String, AttributeItems> getItemsToGive() {
            return itemsToGive;
        }

        public String getCost() {
            return cost;
        }

        public String getAttributeType() {
            return type;
        }

        public String getDowngradeEvent() {
            return downgradeEvent;
        }

        public String getNextTier() {
            return nextTier;
        }

        public String getPreviousTier() {
            return previousTier;
        }

        public String getPageToChangeTo() {
            return pageToChangeTo;
        }

        public List<String> getCommand() {
            return command;
        }

        public static class AttributeItems implements Loadable {

            @ConfigPath
            private List<String> enchantment;

            @ConfigPath
            private String customItem;

            @ConfigPath
            private String material;

            @ConfigPath
            private int amount;

            @ConfigPath
            private String name;

            @ConfigPath
            private List<String> lore;

            @ConfigPath
            private int autoEquipSlot;

            public AttributeItems() {
                this.amount = 1;
                this.lore = new ArrayList<>();
            }

            @Override
            public void load(ConfigurationSection section) {
                this.loadEntries(section);
            }

            @Override
            public boolean isValid() {
                return this.material != null;
            }

            @Override
            public boolean isInvalid() {
                return false;
            }

            public String getCustomItem() {
                return customItem;
            }

            public int getAmount() {
                return amount;
            }

            public String getMaterial() {
                return material;
            }

            public String getName() {
                return name;
            }

            public List<String> getLore() {
                return lore;
            }

            public List<String> getEnchantment() {
                return enchantment;
            }

            public int getAutoEquipSlot() {
                return autoEquipSlot;
            }

            @Override
            public String toString() {
                return "AttributeItems{" +
                        "enchant=" + enchantment +
                        ", customItem='" + customItem + '\'' +
                        ", material='" + material + '\'' +
                        ", amount=" + amount +
                        ", name='" + name + '\'' +
                        ", lore=" + lore +
                        ", autoEquipSlot=" + autoEquipSlot +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ConfigurableAttribute{" +
                    "type='" + type + '\'' +
                    ", pageToChangeTo='" + pageToChangeTo + '\'' +
                    ", cost='" + cost + '\'' +
                    ", nextTier='" + nextTier + '\'' +
                    ", previousTier='" + previousTier + '\'' +
                    ", downgradeEvent='" + downgradeEvent + '\'' +
                    ", command=" + command +
                    ", itemsToGive=" + itemsToGive +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ConfigurableShop{" +
                "defaultPage='" + defaultPage + '\'' +
                ", pages=" + pages +
                ", commonItems=" + commonItems +
                '}';
    }
}
