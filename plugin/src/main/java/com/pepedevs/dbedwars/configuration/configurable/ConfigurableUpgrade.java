package com.pepedevs.dbedwars.configuration.configurable;

import com.pepedevs.corelib.utils.configuration.Loadable;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableCollectionEntry;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableUpgrade implements Loadable {

    @LoadableEntry(key = "default-page")
    private String defaultPage;

    @LoadableCollectionEntry(subsection = "pages")
    private Map<String, ConfigurableUpgradePage> pages;

    public ConfigurableUpgrade() {
        this.pages = new HashMap<>();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.defaultPage != null
                && !this.pages.isEmpty()
                && this.pages.containsKey(this.defaultPage);
    }

    @Override
    public boolean isInvalid() {
        return !this.isValid();
    }

    public String getDefaultPage() {
        return this.defaultPage;
    }

    public Map<String, ConfigurableUpgradePage> getPages() {
        return this.pages;
    }

    public static class ConfigurableUpgradePage implements Loadable {

        @LoadableEntry(key = "title")
        private String title;

        @LoadableEntry(key = "pattern")
        private List<String> pattern;

        @LoadableCollectionEntry(subsection = "items")
        private Map<String, ConfigurableItem> items;

        protected ConfigurableUpgradePage() {
            this.pattern = new ArrayList<>();
            this.items = new HashMap<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return this.title != null && !this.pattern.isEmpty();
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
        }

        public String getTitle() {
            return this.title;
        }

        public List<String> getPattern() {
            return this.pattern;
        }

        public Map<String, ConfigurableItem> getItems() {
            return this.items;
        }

        public static class ConfigurableItem implements Loadable {

            @LoadableEntry(key = "id")
            private String id;

            @LoadableEntry(key = "type")
            private String type;

            @LoadableCollectionEntry(subsection = "tier")
            private Map<String, ConfigurableUpgradeTier> tiers;

            protected ConfigurableItem() {
                this.tiers = new HashMap<>();
            }

            @Override
            public Loadable load(ConfigurationSection section) {
                return this.loadEntries(section);
            }

            @Override
            public boolean isValid() {
                return this.id != null
                        && this.type != null
                        && !tiers.isEmpty()
                        && tiers.containsKey("max");
            }

            @Override
            public boolean isInvalid() {
                return !this.isValid();
            }

            public String getId() {
                return this.id;
            }

            public String getType() {
                return this.type;
            }

            public Map<String, ConfigurableUpgradeTier> getTiers() {
                return this.tiers;
            }

            public static class ConfigurableUpgradeTier implements Loadable {

                @LoadableEntry(key = "material")
                private String material;

                @LoadableEntry(key = "amount")
                private int amount;

                @LoadableEntry(key = "name")
                private String name;

                @LoadableEntry(key = "lore")
                private List<String> lore;

                @LoadableEntry(key = "cost")
                private String cost;

                @LoadableCollectionEntry(subsection = "actions")
                private List<ConfigurableTierAction> actions;

                protected ConfigurableUpgradeTier() {
                    this.lore = new ArrayList<>();
                    this.actions = new ArrayList<>();
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
                    return !this.isValid();
                }

                public String getMaterial() {
                    return this.material;
                }

                public int getAmount() {
                    return this.amount;
                }

                public String getName() {
                    return this.name;
                }

                public List<String> getLore() {
                    return this.lore;
                }

                public String getCost() {
                    return this.cost;
                }

                public List<ConfigurableTierAction> getActions() {
                    return this.actions;
                }

                public static class ConfigurableTierAction implements Loadable {

                    @LoadableEntry(key = "target")
                    private String target;

                    @LoadableEntry(key = "goal")
                    private List<String> goal;

                    protected ConfigurableTierAction() {
                        this.goal = new ArrayList<>();
                    }

                    @Override
                    public Loadable load(ConfigurationSection section) {
                        return this.loadEntries(section);
                    }

                    @Override
                    public boolean isValid() {
                        return this.target != null && !this.goal.isEmpty();
                    }

                    @Override
                    public boolean isInvalid() {
                        return !this.isValid();
                    }

                    public String getTarget() {
                        return this.target;
                    }

                    public List<String> getGoal() {
                        return this.goal;
                    }
                }
            }
        }
    }
}
