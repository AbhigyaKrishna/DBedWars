package org.zibble.dbedwars.api.util.item;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.key.Keyed;

import java.util.Collection;

public abstract class BedWarsActionItem extends ActionItemBase implements Keyed {

    public BedWarsActionItem(String display_name, Collection<String> lore, XMaterial material) {
        this(display_name, lore, material, EventPriority.NORMAL);
    }

    public BedWarsActionItem(Component display_name, Collection<Component> lore, XMaterial material) {
        super(display_name, lore, material, EventPriority.NORMAL);
    }

    public BedWarsActionItem(Message display_name, Message lore, XMaterial material) {
        super(display_name, lore, material, EventPriority.NORMAL);
    }

    public BedWarsActionItem(String display_name, Collection<String> lore, XMaterial material, EventPriority priority) {
        super(display_name, lore, material, priority);
    }

    public BedWarsActionItem(Component display_name, Collection<Component> lore, XMaterial material, EventPriority priority) {
        super(display_name, lore, material, priority);
    }

    public BedWarsActionItem(Message display_name, Message lore, XMaterial material, EventPriority priority) {
        super(display_name, lore, material, priority);
    }

    @Override
    public ItemStack asItemStack() {
        return this.asBwItemStack().asItemStack();
    }

    @Override
    public boolean isThis(ItemStack item) {
        return this.asBwItemStack().isSimilar(new BwItemStack(item));
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
    }

}
