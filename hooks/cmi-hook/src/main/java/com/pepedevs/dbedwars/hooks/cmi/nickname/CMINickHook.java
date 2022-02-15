package com.pepedevs.dbedwars.hooks.cmi.nickname;

import com.Zrips.CMI.CMI;
import com.pepedevs.dbedwars.api.hooks.nickname.NickNameHook;
import com.pepedevs.dbedwars.api.plugin.PluginDependence;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CMINickHook extends PluginDependence implements NickNameHook {

    public CMINickHook() {
        super("CMI");
    }

    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        return false;
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {

    }

    @Override
    public void unNick(@NotNull Player player) {

    }

    @Override
    public String getNickName(@NotNull Player player) {
        return null;
    }

    @Override
    public Boolean apply(Plugin plugin) {
        return null;
    }
}
