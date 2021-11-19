package com.pepedevs.dbedwars.api.game.view;

import com.pepedevs.dbedwars.api.game.view.attributes.ClickCommand;
import com.pepedevs.dbedwars.api.game.view.attributes.PageChange;
import com.pepedevs.dbedwars.api.game.view.attributes.UpgradableTier;
import com.pepedevs.dbedwars.api.util.Overridable;
import me.Abhigya.core.menu.inventory.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public abstract class GuiItem extends Item implements PageChange, ClickCommand, UpgradableTier, Cloneable, Overridable {

    public GuiItem(ItemStack icon) {
        super(icon);
    }

    public abstract String getKey();

    public abstract boolean isLoaded();

    public abstract Map<AttributeType, Attribute> getAttributes();

    public abstract GuiItem clone();

}
