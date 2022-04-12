package org.zibble.dbedwars.api.util.item;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;

import java.util.Collection;

/**
 * Abstract class to be used for creating Action Items.
 */
public abstract class ActionItemBase implements ActionItem {

    protected final Message display_name;
    protected final Message lore;
    protected final XMaterial material;
    protected final EventPriority priority;

    /**
     * Constructs the Action Item.
     *
     * <p>
     *
     * @param display_name Display name of the Action Item
     * @param lore         Lore of the Action Item
     * @param material     Material of the Action Item
     * @param priority     {@link EventPriority} of the Action Item
     */
    public ActionItemBase(Message display_name, Message lore, XMaterial material, EventPriority priority) {
        this.display_name = display_name;
        this.lore = lore;
        this.material = material;
        this.priority = priority;
    }

    /**
     * Constructs the Action Item.
     *
     * <p>
     *
     * @param display_name Display name of the Action Item
     * @param lore         Lore of the Action Item
     * @param material     Material of the Action Item
     * @param priority     {@link EventPriority} of the Action Item
     */
    public ActionItemBase(String display_name, Collection<String> lore, XMaterial material, EventPriority priority) {
        this.display_name = AdventureMessage.from(display_name);
        this.lore = AdventureMessage.from(lore);
        this.material = material;
        this.priority = priority;
    }

    /**
     * Constructs the Action Item.
     *
     * <p>
     *
     * @param display_name Display name of the Action Item
     * @param lore         Lore of the Action Item
     * @param material     Material of the Action Item
     */
    public ActionItemBase(Component display_name, Collection<Component> lore, XMaterial material, EventPriority priority) {
        this(AdventureMessage.from(display_name), AdventureMessage.from(lore.toArray(new Component[0])), material, priority);
    }

    @Override
    public Message getDisplayName() {
        return display_name;
    }

    @Override
    public Message getLore() {
        return lore;
    }

    @Override
    public XMaterial getMaterial() {
        return material;
    }

    @Override
    public EventPriority getPriority() {
        return priority;
    }

    public BwItemStack asBwItemStack() {
        BwItemStack item = new BwItemStack(this.getMaterial());
        item.setDisplayName(this.getDisplayName());
        item.setLore(this.getLore());
        return item;
    }

    public void give(Player player) {
        player.getInventory().addItem(this.asBwItemStack().asItemStack(player));
    }

}
