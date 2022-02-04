package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.CommandAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import org.bukkit.command.CommandSender;

public class CommandActionTranslator implements ActionTranslator<CommandSender> {

    @Override
    public Action<CommandSender> serialize(String untranslated) {
        return new CommandAction(untranslated);
    }

    @Override
    public String deserialize(Action<CommandSender> action) {
        return ((CommandAction) action).getCommand();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("COMMAND");
    }

}
