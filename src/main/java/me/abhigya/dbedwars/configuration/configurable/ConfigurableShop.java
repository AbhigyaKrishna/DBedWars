package me.abhigya.dbedwars.configuration.configurable;

import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableCollectionEntry;
import me.Abhigya.core.util.loadable.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableShop implements Loadable {

    @LoadableEntry(key = "default-page")
    private String defaultPage;

    @LoadableCollectionEntry(subsection = "pages")
    private Map<String, ConfigurablePage> pages;

    @LoadableCollectionEntry(subsection = "common")
    private Map<String, ConfigurablePage.BwGUIItem> commonItems;

    public ConfigurableShop() {
        this.pages = new HashMap<>();
        this.commonItems = new HashMap<>();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        return this.loadEntries(section);
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

    public Map<String, ConfigurablePage.BwGUIItem> getCommonItems() {
        return commonItems;
    }

    public Map<String, ConfigurablePage> getPages() {
        return pages;
    }

    public static class ConfigurablePage implements Loadable {

        @LoadableEntry(key = "title")
        private String guiTitle;

        @LoadableEntry(key = "pattern")
        private List<String> pattern;

        @LoadableCollectionEntry(subsection = "items")
        private Map<String, BwGUIItem> items;

        public ConfigurablePage() {
            this.pattern = new ArrayList<>();
            this.items = new HashMap<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return this.guiTitle != null && !this.pattern.isEmpty();
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
        }

        public Map<String, BwGUIItem> getItems() {
            return items;
        }

        public List<String> getPattern() {
            return pattern;
        }

        public String getGuiTitle() {
            return guiTitle;
        }

        public static class BwGUIItem implements Loadable {

            @LoadableEntry(key = "name")
            private String itemName;

            @LoadableEntry(key = "lore")
            private List<String> itemLore;

            @LoadableEntry(key = "material")
            private String material;

            @LoadableEntry(key = "amount")
            private int amount;

            @LoadableEntry(key = "enchantment")
            private List<String> enchant;

            private ConfigurableAttribute attribute;

            public BwGUIItem() {
                this.amount = 1;
                this.itemLore = new ArrayList<>();
                this.attribute = new ConfigurableAttribute();
                this.enchant = new ArrayList<>();
            }

            @Override
            public Loadable load(ConfigurationSection section) {
                this.attribute.load(section.getConfigurationSection("attribute"));
                return this.loadEntries(section);
            }

            @Override
            public boolean isValid() {
                return true;
            }

            @Override
            public boolean isInvalid() {
                return false;
            }

            public List<String> getItemLore() {
                return itemLore;
            }

            public String getItemName() {
                return itemName;
            }

            public int getAmount() {
                return amount;
            }

            public String getMaterial() {
                return material;
            }

            public List<String> getEnchant() {
                return enchant;
            }

            public ConfigurableAttribute getAttribute() {
                return attribute;
            }
        }
    }

    public static class ConfigurableAttribute implements Loadable {

        @LoadableEntry(key = "type")
        private String type;

        @LoadableEntry(key = "page")
        private String pageToChangeTo;

        @LoadableEntry(key = "cost")
        private String cost;

        @LoadableEntry(key = "next-tier")
        private String nextTier;

        @LoadableEntry(key = "previous-tier")
        private String previousTier;

        @LoadableEntry(key = "downgrade-event")
        private String downgradeEvent;

        @LoadableEntry(key = "command")
        private List<String> command;

        @LoadableCollectionEntry(subsection = "item")
        private Map<String, AttributeItems> itemsToGive;

        public ConfigurableAttribute() {
            itemsToGive = new HashMap<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
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

            @LoadableEntry(key = "custom-item")
            private String customItem;

            @LoadableEntry(key = "material")
            private String material;

            @LoadableEntry(key = "amount")
            private int amount;

            @LoadableEntry(key = "name")
            private String name;

            @LoadableEntry(key = "lore")
            private List<String> lore;

            @LoadableEntry(key = "enchantment")
            List<String> enchant;

            @LoadableEntry(key = "auto-equip-slot")
            private int autoEquipSlot;

            public AttributeItems() {
                this.amount = 1;
                this.lore = new ArrayList<>();
            }

            @Override
            public Loadable load(ConfigurationSection section) {
                return this.loadEntries(section);
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

            public List<String> getEnchant() {
                return enchant;
            }

            public int getAutoEquipSlot() {
                return autoEquipSlot;
            }
        }

    }
}