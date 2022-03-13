package org.zibble.dbedwars.configuration.language;

import org.zibble.dbedwars.api.messaging.Placeholder;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;

public enum PluginLang implements Lang {

    //SETUP SESSION PROMPTS
    SETUP_START(),
    SETUP_ARENA_DISPLAY_NAME_SET(),
    SETUP_WORLD_MOB_SPAWNING_DISABLED(),
    SETUP_WORLD_CLEANUP_PROMPT(),
    SETUP_WORLD_CLEANUP_CLEANING(),
    SETUP_WAITING_LOCATION_PROMPT(),
    SETUP_SPECTATOR_LOCATION_PROMPT(),
    SETUP_TEAM_AUTO_SETUP_TRY(),


    HOLOGRAM_SETUP_TEAM_SPAWN(),
    MESSAGE_SETUP_TEAM_SPAWN(),
    HOLOGRAM_SETUP_TEAM_SHOP(),
    MESSAGE_SETUP_TEAM_SHOP(),
    HOLOGRAM_SETUP_TEAM_UPGRADES(),
    MESSAGE_SETUP_TEAM_UPGRADES(),
    HOLOGRAM_SETUP_TEAM_BED(),
    MESSAGE_SETUP_TEAM_BED(),
    HOLOGRAM_SETUP_TEAM_GEN(),
    MESSAGE_SETUP_TEAM_GEN(),
    HOLOGRAM_SETUP_SPECTATOR(),
    MESSAGE_SETUP_SPECTATOR(),
    MESSAGE_SETUP_LOBBY_CORNER_1(),
    MESSAGE_SETUP_LOBBY_CORNER_2(),

    SETUP_DISABLE_MOB_SPAWNING_PROMPT(),

    HOLOGRAM_SETUP_WAITING_LOCATION_SET(),
    SETUP_WAITING_LOCATION_SET(),


    //SETUP SESSION COMMANDS
    NOT_IN_SETUP_SESSION(),
    SETUP_SESSION_ENTITIES_CLEARED(),
    SETUP_SESSION_DISABLED_MOB_SPAWNING(),
    SETUP_SESSION_SPECTATOR_LOCATION_SET(),
    SETUP_SESSION_INVALID_COLOR(),
    SETUP_SESSION_COLOR_NOT_FOUND_AS_TEAM(),
    SETUP_SESSION_NOT_LOOKING_AT_BLOCK(),
    SETUP_SESSION_BOX_CORNER_USAGE(),

    SETUP_SESSION_TEAM_BED_USAGE(),
    SETUP_SESSION_TEAM_GEN_USAGE(),
    SETUP_SESSION_TEAM_GEN_INVALID_DROP_TYPE(),
    SETUP_SESSION_TEAM_SHOP_USAGE(),
    SETUP_SESSION_TEAM_SPAWN_USAGE(),
    SETUP_SESSION_TEAM_UPGRADE_USAGE()

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
