package org.zibble.dbedwars.commands.general;

import com.google.common.collect.ImmutableMap;
import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.Map;

@Permission("dbedwars.reload")
@SubCommandNode(parent = "bw", value = "reload")
public class ReloadCommand extends GeneralCommandNode {

    private final Map<Key, Runnable> reloadables;

    public ReloadCommand(DBedwars plugin, Messaging messaging) {
        super(plugin, messaging);
        this.reloadables = ImmutableMap.<Key, Runnable>builder()
                .put(Key.of("gui"), () -> plugin.getConfigHandler().loadGuis()).build();
    }

    @Override
    public boolean execute(MessagingMember member, CommandSender sender, String[] args) {
        if (args.length == 0) {
            for (Map.Entry<Key, Runnable> entry : this.reloadables.entrySet()) {
                entry.getValue().run();
            }
            member.sendMessage(AdventureMessage.from("<green>Reloaded all configurations."));
        } else {
            Key key = Key.of(args[0]);
            if (this.reloadables.containsKey(key)) {
                this.reloadables.get(key).run();
                member.sendMessage(AdventureMessage.from("<green>Reloaded configuration for <yellow>" + key.get() + "."));
            } else {
                member.sendMessage(AdventureMessage.from("<red>No configuration found for <yellow>" + key.get() + "."));
            }
        }
        return true;
    }

}
