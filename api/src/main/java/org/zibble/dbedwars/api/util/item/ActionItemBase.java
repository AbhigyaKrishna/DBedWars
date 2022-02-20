package org.zibble.dbedwars.api.util.item;

import com.google.common.base.Objects;
import com.pepedevs.radium.utils.itemstack.ItemStackUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.adventure.AdventureUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Abstract class to be used for creating Action Items. */
public abstract class ActionItemBase implements ActionItem {

    protected final Component display_name;
    protected final List<Component> lore;
    protected final Material material;
    protected final EventPriority priority;

    /**
     * Constructs the Action Item.
     *
     * <p>
     *
     * @param display_name Display name of the Action Item
     * @param lore Lore of the Action Item
     * @param material Material of the Action Item
     * @param priority {@link EventPriority} of the Action Item
     */
    public ActionItemBase(Component display_name, Collection<Component> lore, Material material, EventPriority priority) {
        this.display_name = display_name;
        this.lore = new ArrayList<>(lore);
        this.material = material;
        this.priority = priority;
    }

    /**
     * Constructs the Action Item.
     *
     * <p>
     *
     * @param display_name Display name of the Action Item
     * @param lore Lore of the Action Item
     * @param material Material of the Action Item
     * @param priority {@link EventPriority} of the Action Item
     */
    public ActionItemBase(String display_name, Collection<String> lore, Material material, EventPriority priority) {
        this.display_name = AdventureUtils.fromLegacyText(display_name);
        this.lore = AdventureUtils.fromLegacyText(lore);
        this.material = material;
        this.priority = priority;
    }

    /**
     * Constructs the Action Item.
     *
     * <p>
     *
     * @param display_name Display name of the Action Item
     * @param lore Lore of the Action Item
     * @param material Material of the Action Item
     */
    public ActionItemBase(Component display_name, Collection<Component> lore, Material material) {
        this(display_name, lore, material, EventPriority.NORMAL);
    }

    /**
     * Constructs the Action Item.
     *
     * <p>
     *
     * @param display_name Display name of the Action Item
     * @param lore Lore of the Action Item
     * @param material Material of the Action Item
     */
    public ActionItemBase(String display_name, Collection<String> lore, Material material) {
        this(display_name, lore, material, EventPriority.NORMAL);
    }

    @Override
    public Component getDisplayName() {
        return display_name;
    }

    @Override
    public List<Component> getLore() {
        return lore;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public EventPriority getPriority() {
        return priority;
    }

    @Override
    public ItemStack toItemStack() {
        return new ItemMetaBuilder(getMaterial())
                .displayName(getDisplayName())
                .lore(getLore())
                .toItemStack();
    }

    @Override
    public boolean isThis(ItemStack item) {
        if (item != null) {
            return item.getType() == getMaterial()
                    && Objects.equal(ItemStackUtils.extractName(item), this.getDisplayName())
                    && Objects.equal(ItemStackUtils.extractLore(item), this.getLore());
        } else {
            return false;
        }
    }
}
