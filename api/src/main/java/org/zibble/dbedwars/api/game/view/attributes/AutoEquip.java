package org.zibble.dbedwars.api.game.view.attributes;

import org.bukkit.entity.Player;

public interface AutoEquip {

    boolean isAutoEquip();

    void setAutoEquip(boolean flag);

    int getSlot();

    void setSlot(int slot);

    void equip(Player player);
}
