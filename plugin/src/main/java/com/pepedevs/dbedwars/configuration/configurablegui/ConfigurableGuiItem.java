package com.pepedevs.dbedwars.configuration.configurablegui;

import com.pepedevs.corelib.gui.inventory.item.action.ActionItem;
import com.pepedevs.corelib.gui.inventory.item.action.ItemAction;
import com.pepedevs.corelib.gui.inventory.item.action.ItemActionPriority;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.corelib.utils.itemstack.ItemMetaBuilder;
import com.pepedevs.corelib.utils.configuration.Loadable;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableCollectionEntry;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableEntry;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.api.util.LEnchant;
import com.pepedevs.dbedwars.configuration.configurablegui.action.ActionParser;
import com.pepedevs.dbedwars.configuration.configurablegui.action.ActionType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ConfigurableGuiItem implements Loadable {

    private static final List<ClickType> BLACKLISTED_CLICKTYPE =
            Arrays.asList(
                    ClickType.CREATIVE,
                    ClickType.UNKNOWN,
                    ClickType.WINDOW_BORDER_LEFT,
                    ClickType.WINDOW_BORDER_RIGHT);

    @LoadableEntry(key = "itemflag")
    private final List<String> flags;

    @LoadableEntry(key = "enchantment")
    private final List<String> enchantments;

    @LoadableCollectionEntry(subsection = "actions")
    private final List<ConfigurableGuiItemAction> itemActions;

    @LoadableEntry(key = "type")
    private String type;

    @LoadableEntry(key = "size")
    private int size;

    @LoadableEntry(key = "name")
    private String name;

    @LoadableEntry(key = "lore")
    private List<String> lore;

    @LoadableEntry(key = "data")
    private short data;

    @LoadableEntry(key = "glowing")
    private boolean glowing;

    public ConfigurableGuiItem() {
        flags = new ArrayList<>();
        enchantments = new ArrayList<>();
        itemActions = new ArrayList<>();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        Optional<XMaterial> match = XMaterial.matchXMaterial(this.type);
        return this.type != null
                && match.isPresent()
                && match.get().isSupported()
                && this.name != null;
    }

    @Override
    public boolean isInvalid() {
        return !this.isValid();
    }

    public ActionItem toActionItem() {
        ItemStack stack =
                new ItemStack(
                        XMaterial.matchXMaterial(this.type).get().parseMaterial(),
                        this.size == 0 ? 1 : this.size);
        stack.setDurability(this.data);
        ItemMetaBuilder meta =
                new ItemMetaBuilder(stack.getType())
                        .withDisplayName(StringUtils.translateAlternateColorCodes(this.name))
                        .withLore(StringUtils.translateAlternateColorCodes(this.lore))
                        .withItemFlags(
                                this.flags.stream()
                                        .map(
                                                s -> {
                                                    try {
                                                        return ItemFlag.valueOf(s);
                                                    } catch (IllegalArgumentException ignored) {
                                                    }
                                                    return null;
                                                })
                                        .filter(Objects::nonNull)
                                        .toArray(ItemFlag[]::new));

        if (glowing && enchantments.isEmpty()) {
            meta.withEnchantment(Enchantment.SILK_TOUCH, 1, true)
                    .withItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        enchantments.stream()
                .map(LEnchant::valueOf)
                .filter(Objects::nonNull)
                .forEach(e -> e.apply(stack));

        ActionItem item = new ActionItem(meta.applyTo(stack));
        this.itemActions.forEach(a -> item.addAction(a.toAction()));

        return item;
    }

    public static class ConfigurableGuiItemAction implements Loadable {

        private ItemAction itemAction;

        @LoadableEntry(key = "type")
        private String type;

        @LoadableEntry(key = "priority")
        private String priority;

        @LoadableEntry(key = "action")
        private String action;

        @LoadableEntry(key = "clickType")
        private String clickType;

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            if (this.type == null || this.action == null) return false;

            try {
                ActionType.valueOf(this.type);
            } catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
        }

        public ItemAction toAction() {
            if (this.itemAction == null) {
                ActionType actionType = ActionType.valueOf(this.type);
                ItemActionPriority actionPriority = ItemActionPriority.NORMAL;
                if (this.priority != null) {
                    try {
                        actionPriority = ItemActionPriority.valueOf(priority);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                ClickType actionClick = null;
                if (this.clickType != null) {
                    try {
                        actionClick = ClickType.valueOf(this.clickType);
                    } catch (IllegalArgumentException ignored) {
                    }
                }

                ActionParser ap =
                        new ActionParser(actionType, this.action, actionPriority, actionClick);
                this.itemAction = ap.toAction();
            }

            return this.itemAction;
        }

    }

}
