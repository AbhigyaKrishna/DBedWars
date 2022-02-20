package org.zibble.dbedwars.api.game.view.attributes;

import com.pepedevs.radium.gui.inventory.action.ItemClickAction;
import org.zibble.dbedwars.api.game.view.GuiItem;

public interface UpgradableTier {

    GuiItem getNextTier();

    void setNextTier(GuiItem nextTier);

    void upgradeTier(ItemClickAction action);
}
