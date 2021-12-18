package com.pepedevs.dbedwars.api.game.view.attributes;

import com.pepedevs.corelib.gui.inventory.action.ItemClickAction;
import com.pepedevs.dbedwars.api.game.view.GuiItem;

public interface UpgradableTier {

    GuiItem getNextTier();

    void setNextTier(GuiItem nextTier);

    void upgradeTier(ItemClickAction action);
}
