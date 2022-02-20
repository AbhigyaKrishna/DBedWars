package org.zibble.dbedwars.hooks.autonicker;

import me.blvckbytes.autonicker.NickSession;
import me.blvckbytes.autonicker.api.NickAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

public class AutoNickerHook extends PluginDependence implements NickNameHook {


    public AutoNickerHook() {
        super("AutoNicker");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into AutoNicker!"));
        return true;
    }

    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        final NickSession session = NickAPI.getSession(player);
        if(session == null)
            return false;

        return session.isNicked();
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {
        final NickSession session = NickAPI.getSession(player);
        if(session == null)
            return;

        //NEVER RUN THIS ON SYNC. This guy uses Thread#sleep()
        session.setName(nick, false);
    }

    @Override
    public void unNick(@NotNull Player player) {
        final NickSession session = NickAPI.getSession(player);
        if(session == null)
            return;

        //NEVER RUN THIS ON SYNC. This guy uses Thread#sleep()
        session.reset(false);
    }

    @Override
    public String getNickName(@NotNull Player player) {
       return isPlayerNicked(player) ? NickAPI.getSession(player).current_nick : getRealName(player);
    }
}
