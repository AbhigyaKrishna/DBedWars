package com.pepedevs.dbedwars.api.game.view.attributes;

import com.pepedevs.dbedwars.api.game.view.GuiItem;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;

public interface UpgradableTier {

    GuiItem getNextTier();

    void setNextTier(GuiItem nextTier);

    void upgradeTier(ItemClickAction action);
}
