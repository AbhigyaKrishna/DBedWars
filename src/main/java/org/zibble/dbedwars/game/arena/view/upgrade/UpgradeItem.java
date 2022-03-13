package org.zibble.dbedwars.game.arena.view.upgrade;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.objects.serializable.LEnchant;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.configuration.configurable.ConfigurableUpgrade;
import org.zibble.dbedwars.utils.ConfigurationUtils;
import org.zibble.dbedwars.utils.ItemConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class UpgradeItem {

    private Team team;
    private ConfigurableUpgrade.ConfigurableUpgradePage.ConfigurableItem cfgItem;
    private String key;
    private UpgradePage page;
    private Map<String, UpgradeTier> tiers;

    public UpgradeItem(
            Team team,
            ConfigurableUpgrade.ConfigurableUpgradePage.ConfigurableItem item,
            String key,
            UpgradePage page) {
        this.team = team;
        this.cfgItem = item;
        this.key = key;
        this.page = page;
        this.tiers = new LinkedHashMap<>();
        this.cfgItem
                .getTiers()
                .forEach(
                        (s, t) -> {
                            this.tiers.put(s, new UpgradeTier(s, t));
                        });
    }

    public Team getTeam() {
        return this.team;
    }

    public String getKey() {
        return this.key;
    }

    public UpgradePage getPage() {
        return this.page;
    }

    public Map<String, UpgradeTier> getTiers() {
        return this.tiers;
    }

    enum ItemType {
        VOID,
        CHANGE_PAGE,
        PERMANENT,
        TRAP,
        ;
    }

    enum Target {
        TEAM,
        ENEMY_TEAM;
    }

    enum TierActionType {
        ENCHANT {
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                String[] split = statement.replace("[ENCHANT]", "").trim().split("~");
                if (split.length < 2) {
                    return super.getAction(statement);
                }
                LEnchant enchant = LEnchant.valueOf(split[0].trim());
                if (enchant == null) return super.getAction(statement);

                XMaterial material = XMaterial.matchXMaterial(split[1].trim()).orElse(null);
                if (material == null) {
                    ItemConstant constant =
                            ConfigurationUtils.matchEnum(split[1].trim(), ItemConstant.values());
                    if (constant == null) return super.getAction(statement);
                    for (XMaterial item : constant.getItems()) {
                        if (item.isSupported()) {
                            return player -> {
                                ItemStack[] items =
                                        player.getPlayer().getInventory().getContents();
                                for (ItemStack stack : items) {
                                    if (stack.getType() == item.parseMaterial()) {
                                        enchant.applyUnsafe(stack);
                                    }
                                }
                            };
                        }
                    }
                } else {
                    if (material.isSupported()) {
                        return player -> {
                            ItemStack[] items = player.getPlayer().getInventory().getContents();
                            for (ItemStack stack : items) {
                                if (stack.getType() == material.parseMaterial()) {
                                    enchant.applyUnsafe(stack);
                                }
                            }
                        };
                    }
                }

                return super.getAction(statement);
            }
        },
        POTION {
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                return super.getAction(statement);
            }
        },
        SPAWNER,
        TRAP,
        DRAGON_BUFF,
        CHANGE_PAGE,
        ;

        public Consumer<ArenaPlayer> getAction(String statement) {
            return team -> {};
        }
    }

    public class UpgradeTier {

        private UpgradeItem item;
        private ConfigurableUpgrade.ConfigurableUpgradePage.ConfigurableItem.ConfigurableUpgradeTier
                tier;
        private String key;

        private BwItemStack material;
        private int amount;
        private String name;
        private List<String> lore;
        private Set<ItemStack> cost;
        private Map<Target, TierAction> actions;

        public UpgradeTier(
                String key,
                ConfigurableUpgrade.ConfigurableUpgradePage.ConfigurableItem.ConfigurableUpgradeTier
                        tier) {
            this.item = UpgradeItem.this;
            this.key = key;
            this.tier = tier;
            this.material =
                    ConfigurationUtils.parseItem(
                            this.item.team.getColor(), this.tier.getMaterial());
            this.amount = this.tier.getAmount();
            this.name = this.tier.getName();
            this.lore = this.tier.getLore();
            this.cost =
                    this.tier.getCost() != null
                            ? ConfigurationUtils.parseCost(this.tier.getCost())
                            : new HashSet<>();
            this.actions = new ConcurrentHashMap<>();
            this.tier
                    .getActions()
                    .forEach(
                            a ->
                                    this.actions.put(
                                            ConfigurationUtils.matchEnum(
                                                    a.getTarget(), Target.values()),
                                            new TierAction(a)));
        }

        public String getKey() {
            return key;
        }

        public UpgradeItem getItem() {
            return this.item;
        }

        public BwItemStack getMaterial() {
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

        public Set<ItemStack> getCost() {
            return this.cost;
        }

        public Map<Target, TierAction> getActions() {
            return actions;
        }

        public class TierAction {

            private UpgradeTier tier;
            private ConfigurableUpgrade.ConfigurableUpgradePage.ConfigurableItem
                            .ConfigurableUpgradeTier.ConfigurableTierAction
                    action;

            private Target target;
            private List<Consumer<Team>> goals;

            public TierAction(
                    ConfigurableUpgrade.ConfigurableUpgradePage.ConfigurableItem
                                    .ConfigurableUpgradeTier.ConfigurableTierAction
                            action) {
                this.tier = UpgradeTier.this;
                this.action = action;
                this.target =
                        ConfigurationUtils.matchEnum(this.action.getTarget(), Target.values());
                this.goals = Collections.synchronizedList(new ArrayList<>());
            }

            public UpgradeTier getTier() {
                return tier;
            }

            public Target getTarget() {
                return target;
            }

            public List<Consumer<Team>> getGoals() {
                return goals;
            }
        }
    }
}
