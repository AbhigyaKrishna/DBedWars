package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.ConsoleCommandAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;

public class ConsoleCommandTranslator implements ActionTranslator<Void> {

    @Override
    public Action<Void> serialize(String untranslated) {
        return new ConsoleCommandAction(untranslated);
    }

    @Override
    public String deserialize(Action<Void> action) {
        return ((ConsoleCommandAction) action).getCommand();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("CONSOLE_COMMAND");
    }

}
