package org.zibble.dbedwars.configuration.language;

import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.messaging.message.Message;

public interface Lang {

    Message asMessage();

    Message asMessage(Placeholder... placeholders);

}
