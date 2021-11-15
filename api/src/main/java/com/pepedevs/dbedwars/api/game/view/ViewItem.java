package com.pepedevs.dbedwars.api.game.view;

import me.Abhigya.core.menu.inventory.item.action.ActionItem;
import me.Abhigya.core.util.Validable;
import com.pepedevs.dbedwars.api.exceptions.OverrideException;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.LEnchant;
import com.pepedevs.dbedwars.api.util.Overridable;

import java.util.List;
import java.util.Map;

public interface ViewItem extends Cloneable, Overridable {

    //    ShopPage getPage();

    ArenaPlayer getPlayer();

    BwItemStack getMaterial();

    void setMaterial(BwItemStack material);

    int getAmount();

    void setAmount(int amount);

    String getName();

    void setName(String name);

    List<String> getLore();

    List<LEnchant> getEnchants();

    //    Map<AttributeType, Attribute> getAttributes();

    BwItemStack getFormatted();

    ActionItem getActionItem(boolean recreate);

    @Override
    void override(Overridable override) throws OverrideException;

    ViewItem clone();

    interface Attribute extends Validable, Cloneable, Overridable {

        //        AttributeType getType();

        Map<String, Object> getKeyEntry();

        @Override
        boolean isValid();

        @Override
        boolean isInvalid();

        @Override
        void override(Overridable override) throws OverrideException;

        Attribute clone();
    }
}
