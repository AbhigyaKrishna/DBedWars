package org.zibble.dbedwars.hooks.nickapi;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;
import xyz.haoshoku.nick.api.NickAPI;

public class NickAPIHook extends PluginDependence implements NickNameHook {

    public NickAPIHook() {
        super("NickAPI");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into NickAPI!"));
        }
        return true;
    }

    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        return NickAPI.isNicked(player);
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {
        NickAPI.nick(player, nick);
    }

    @Override
    public void unNick(@NotNull Player player) {
        NickAPI.resetNick(player);
    }

    @Override
    public String getNickName(@NotNull Player player) {
        return isPlayerNicked(player) ? NickAPI.getName(player) : getRealName(player);
    }

}
