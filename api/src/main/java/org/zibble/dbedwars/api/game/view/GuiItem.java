package org.zibble.dbedwars.api.game.view;

import com.pepedevs.radium.gui.inventory.Item;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.view.attributes.ClickCommand;
import org.zibble.dbedwars.api.game.view.attributes.PageChange;
import org.zibble.dbedwars.api.game.view.attributes.UpgradableTier;
import org.zibble.dbedwars.api.util.Overridable;

import java.util.Map;

public abstract class GuiItem extends Item
        implements PageChange, ClickCommand, UpgradableTier, Cloneable, Overridable {

    public GuiItem(ItemStack icon) {
        super(icon);
    }

    public abstract String getKey();

    public abstract boolean isLoaded();

    public abstract Map<AttributeType, Attribute> getAttributes();

    public abstract ShopPage getShopPage();

    public abstract void setShopPage(ShopPage shopPage);

    public abstract GuiItem clone();
}
