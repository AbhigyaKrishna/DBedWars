package org.zibble.dbedwars.hooks.eazynick;

import net.dev.eazynick.api.NickManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

public class EazyNickHook extends PluginDependence implements NickNameHook {

    public EazyNickHook() {
        super("EazyNick");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into EazyNick!"));
        }
        return true;
    }

    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        final NickManager manager = new NickManager(player);
        return manager.isNicked();
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {
        final NickManager manager = new NickManager(player);
        manager.setName(nick);
    }

    @Override
    public void unNick(@NotNull Player player) {
        final NickManager manager = new NickManager(player);
        manager.unnickPlayer();
    }

    @Override
    public String getNickName(@NotNull Player player) {
        final NickManager manager = new NickManager(player);
        return isPlayerNicked(player) ? manager.getNickName() : manager.getRealName();
    }

}
