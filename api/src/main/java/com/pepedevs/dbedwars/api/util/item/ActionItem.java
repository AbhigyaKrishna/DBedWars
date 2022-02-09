package com.pepedevs.dbedwars.api.util.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ActionItem {

    /**
     * Gets the display name of the Action Item.
     *
     * <p>
     *
     * @return Display name of the Action Item
     */
    Component getDisplayName();

    /**
     * Gets the lore of the Action Item.
     *
     * <p>
     *
     * @return Lore of the Action Item
     */
    List<Component> getLore();

    /**
     * Gets the material of the Action Item.
     *
     * <p>
     *
     * @return Material of the Action Item
     */
    Material getMaterial();

    /**
     * Gets the {@link EventPriority} for the Action Item.
     *
     * <p>
     *
     * @return Event priority of the Action Item
     */
    EventPriority getPriority();

    /**
     * Get the ItemStack of the Action Item.
     *
     * <p>
     *
     * @return ItemStack of the Action Item
     */
    ItemStack toItemStack();

    /**
     * Checks if the provided ItemStack is of this Action Item.
     *
     * <p>
     *
     * @param item ItemStack to check
     * @return <strong>{@code true}</strong> if the ItemStack is of this Action Item, else false
     */
    boolean isThis(ItemStack item);

    /**
     * Register the action to be performed on interact with this item.
     *
     * <p>
     *
     * @param player Player who performs the action
     * @param action {@link EnumAction} performed on this item
     * @param event {@link PlayerInteractEvent} triggered in the action event
     */
    void onActionPerform(Player player, EnumAction action, PlayerInteractEvent event);

    /** Enumeration for actions defined for an item. */
    enum EnumAction {
        LEFT_CLICK,
        LEFT_CLICK_SNEAKING,
        LEFT_CLICK_SPRINTING,

        RIGHT_CLICK,
        RIGHT_CLICK_SNEAKING,
        RIGHT_CLICK_SPRINTING,
        ;
    }
}
