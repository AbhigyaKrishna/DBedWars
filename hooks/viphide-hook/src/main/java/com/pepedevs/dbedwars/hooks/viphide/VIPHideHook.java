package com.pepedevs.dbedwars.hooks.viphide;

import com.pepedevs.dbedwars.api.hooks.nickname.NickNameHook;
import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.plugin.PluginDependence;
import me.BukkitPVP.VIPHide.VIPHide;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class VIPHideHook extends PluginDependence implements NickNameHook {

    private VIPHide hookAPI;

    public VIPHideHook() {
        super("VIPHide");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if(plugin != null){
            hookAPI = (VIPHide) plugin;
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into VIPHide!"));
        }
        return true;
    }

    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        return this.hookAPI.isDisguised(player);
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {
        this.hookAPI.disguise(player);
    }

    @Override
    public void unNick(@NotNull Player player) {
        this.hookAPI.undisguise(player);
    }

    @Override
    public String getNickName(@NotNull Player player) {
        return isPlayerNicked(player) ? this.hookAPI.getName(player) : getRealName(player);
    }
}
