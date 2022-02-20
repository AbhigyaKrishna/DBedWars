package org.zibble.dbedwars.hooks.nicknamer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.nicknamer.api.NickManager;
import org.inventivetalent.nicknamer.api.NickNamerAPI;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.plugin.PluginDependence;

public class NickNamerHook extends PluginDependence implements NickNameHook {

    private NickManager pluginHook;

    public NickNamerHook() {
        super("NickNamer");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if(plugin != null){
            pluginHook = NickNamerAPI.getNickManager();
        }
        return true;
    }

    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        return this.pluginHook.isNicked(player.getUniqueId());
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {
        this.pluginHook.setNick(player.getUniqueId(), nick);
    }

    @Override
    public void unNick(@NotNull Player player) {
        this.pluginHook.removeNick(player.getUniqueId());
    }

    @Override
    public String getNickName(@NotNull Player player) {
        return isPlayerNicked(player) ? this.pluginHook.getNick(player.getUniqueId()) : getRealName(player);
    }
}
