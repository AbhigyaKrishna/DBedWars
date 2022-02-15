package com.pepedevs.dbedwars.hooks.betternick;

import com.pepedevs.dbedwars.api.hooks.nickname.NickNameHook;
import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.plugin.PluginDependence;
import net.korex.betternick.BetterNick;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BetterNickHook extends PluginDependence implements NickNameHook {

    public BetterNickHook() {
        super("BetterNick");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if(plugin != null){
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into BetterNick!"));
        }
        return true;
    }

    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        return BetterNick.isNicked(player.getUniqueId().toString());
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {
        BetterNick.setNick(player,nick);
    }

    @Override
    public void unNick(@NotNull Player player) {
        BetterNick.remove(player);
    }

    @Override
    public String getNickName(@NotNull Player player) {
        return isPlayerNicked(player) ? BetterNick.getRealName(player.getUniqueId().toString()) : getRealName(player);
    }
}
