package org.zibble.dbedwars.menus.actions.defaults;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.menu.MenuAction;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.messaging.Messaging;

public class SendMessageActionImpl implements MenuAction {

    @Override
    public String tag() {
        return "[SEND_MESSAGE]";
    }

    @Override
    public String description() {
        return "Sends a message to the clicked player!";
    }

    //TODO
    @Override
    public boolean execute(@NotNull Player player, @NotNull String[] args) {
        if (args.length <= 1) {
            DBedwars.getInstance().getLogger().warning("The action of " + tag() + " didn't provide a statement to send message on... Skipping!");
            return false;
        }

        Message message = ConfigLang.getTranslator().asMessage(args[1],
                PlaceholderEntry.symbol("name", player.getName())
        );
        if (args.length > 2) {
            for (int i = 2; i < args.length; i++) {
                message.addLine(args[i]);
            }
        }

        Messaging.get().getMessagingMember(player).sendMessage(message);
        return true;
    }

}
