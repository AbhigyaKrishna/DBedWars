package org.zibble.dbedwars.configuration.language;

import org.zibble.dbedwars.api.messaging.Placeholder;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;

public enum PluginLang implements Lang {

    HOLOGRAM_SETUP_TEAM_SPAWN(),
    HOLOGRAM_SETUP_TEAM_SHOP(),
    HOLOGRAM_SETUP_TEAM_UPGRADES(),
    HOLOGRAM_SETUP_TEAM_BED(),
    HOLOGRAM_SETUP_TEAM_GEN(),
    SETUP_TEAM_SPAWN(),
    SETUP_TEAM_SHOP(),
    SETUP_TEAM_UPGRADES(),
    SETUP_TEAM_BED(),
    SETUP_TEAM_GEN(),

    ;

    private final Message message;

    PluginLang(String... messages) {
        this.message = AdventureMessage.from(messages);
    }

    PluginLang(Message message) {
        this.message = message;
    }

    @Override
    public Message asMessage() {
        return this.message;
    }

    @Override
    public Message asMessage(Placeholder... placeholders) {
        Message msg = this.message.clone();
        msg.addPlaceholders(placeholders);
        return msg;
    }

}
