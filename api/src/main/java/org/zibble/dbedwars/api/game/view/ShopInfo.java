package org.zibble.dbedwars.api.game.view;

import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.util.function.ArrayFunction;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.Keyed;

import java.util.Map;

public interface ShopInfo extends Keyed {

    PageInfo getDefaultPage();

    NPCModel getNpc();

    Map<Key, ? extends PageInfo> getPages();

    interface PageInfo extends Keyed {

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
