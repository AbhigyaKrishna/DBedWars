package org.zibble.dbedwars.api.game.view;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.Keyed;

import java.util.Map;

public interface ShopType extends Keyed<String> {

    Page getDefaultPage();

    Map<Key<String>, ? extends Page> getPages();

    interface Page extends Keyed<String> {

        int getRow();

        Message getTitle();

        String[] getMask();

    }

}
