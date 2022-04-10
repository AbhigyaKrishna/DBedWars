package org.zibble.dbedwars.configuration.language;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;

public interface Lang {

    Message asMessage();

    Message asMessage(Placeholder... placeholders);

}
