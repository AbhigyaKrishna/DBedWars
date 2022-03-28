package org.zibble.dbedwars.api.game.view;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.util.ArrayFunction;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.Keyed;

import java.util.Map;

public interface ShopInfo extends Keyed<String> {

    PageInfo getDefaultPage();

    Map<Key<String>, ? extends PageInfo> getPages();

    interface PageInfo extends Keyed<String> {

        int getRow();

        Message getTitle();

        String[] getMask();

        Map<Character, ? extends ItemInfo> getItems();

    }

    interface ItemInfo {

        ArrayFunction<Placeholder, BwItemStack> getItemFunction();

        TierGroupInfo getTierGroup();

    }

    interface TierGroupInfo {

        ItemInfo getItem(int tier);

        Integer[] getAllTiers();
    }

}
