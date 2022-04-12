package org.zibble.dbedwars.api.hooks.hologram;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;

public interface HologramLine<C> {

    HologramPage getParent();

    C getContent();

    void setContent(C content);

    float getHeight();

    interface Text extends HologramLine<Message> {

    }

    interface Head extends HologramLine<BwItemStack> {

    }

    interface SmallHead extends HologramLine<BwItemStack> {

    }

    interface Icon extends HologramLine<BwItemStack> {

    }

    interface Entity extends HologramLine<HologramEntityType> {

    }

}
