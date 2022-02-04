package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.ConsoleCommandAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;

public class ConsoleCommandActionTranslator implements ActionTranslator<Void, ConsoleCommandAction> {

    @Override
    public ConsoleCommandAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        return new ConsoleCommandAction(untranslated);
    }

    @Override
    public String deserialize(ConsoleCommandAction action) {
        return action.getCommand();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("CONSOLE_COMMAND");
    }

}
