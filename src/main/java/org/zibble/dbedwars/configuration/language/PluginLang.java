package org.zibble.dbedwars.configuration.language;

import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;

public enum PluginLang implements Lang {

    DEFAULT_CONSOLE_TRY("<red>You need to be a player to run this command!"),

    DISABLE_MOB_SPAWNING_PROMPT(),
    DISABLE_MOB_SPAWNING_DONE(),
    ENTITY_CLEANUP_PROMPT(),
    ENTITY_CLEANUP_DONE(),
    SETUP_WAITING_LOCATION_PROMPT(),
    SETUP_WAITING_LOCATION_DONE(),
    SETUP_WAITING_LOCATION_HOLOGRAM(),
    SETUP_SPECTATOR_LOCATION_PROMPT(),
    SETUP_SPECTATOR_LOCATION_DONE(),
    SETUP_SPECTATOR_LOCATION_HOLOGRAM(),
    SETUP_WAITING_LOCATION_CORNERS_PROMPT(),
    SETUP_WAITING_LOCATION_CORNERS_DONE(),
    ADD_TEAMS_PROMPT(),
    ADD_TEAM_DONE(),
    REMOVE_TEAM_DONE(),
    SETUP_CUSTOM_NAME_DONE(),
    SETUP_TEAM_SPAWN_DONE(),
    SETUP_TEAM_SPAWN_HOLOGRAM(),
    SETUP_TEAM_BED_DONE(),
    SETUP_TEAM_BED_HOLOGRAM(),
    SETUP_TEAM_SPAWNER_DONE(),
    SETUP_TEAM_SPAWNER_CLEAR(),
    SETUP_TEAM_SPAWNER_HOLOGRAM(),
    SETUP_TEAM_SHOP_DONE(),
    SETUP_TEAM_SHOP_CLEAR(),
    SETUP_TEAM_SHOP_HOLOGRAM(),
    SETUP_COMMON_SPAWNER_DONE(),
    SETUP_COMMON_SPAWNER_CLEAR(),
    SETUP_COMMON_SPAWNER_HOLOGRAM(),


    //SETUP SESSION COMMANDS
    NOT_IN_SETUP_SESSION(),
    ALREADY_IN_SETUP_SESSION(),
    SETUP_SESSION_ENTITIES_CLEARED(),
    SETUP_SESSION_DISABLED_MOB_SPAWNING(),
    SETUP_SESSION_SPECTATOR_LOCATION_SET(),
    SETUP_SESSION_INVALID_COLOR(),
    SETUP_SESSION_COLOR_NOT_FOUND_AS_TEAM(),
    SETUP_SESSION_COLOR_ALREADY_ADDED,
    SETUP_SESSION_NOT_LOOKING_AT_BLOCK(),
    SETUP_SESSION_BOX_CORNER_USAGE(),
    SETUP_WAITING_AREA_DONE(),
    SETUP_SESSION_GEN_USAGE(),
    SETUP_SESSION_NO_SELECTION_FOUND(),

    SETUP_SESSION_ADD_TEAM_USAGE(),
    SETUP_SESSION_REMOVE_TEAM_USAGE(),
    SETUP_SESSION_TEAM_BED_USAGE(),
    SETUP_SESSION_TEAM_GEN_INVALID_DROP_TYPE(),
    SETUP_SESSION_TEAM_SHOP_USAGE(),
    SETUP_SESSION_TEAM_SPAWN_USAGE(),
    SETUP_SESSION_TEAM_UPGRADE_USAGE(),
    SETUP_SESSION_TEAM_SHOP_NOT_FOUND(),
    SETUP_SESSION_TEAM_BED_NOT_FOUND(),
    SETUP_SESSION_FOUND_GENERATOR_LOCATION(),
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
