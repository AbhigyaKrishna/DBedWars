package org.zibble.dbedwars.hooks.nametagedit;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.INametagApi;
import com.nametagedit.plugin.api.data.Nametag;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

public class NameTagEditHook extends PluginDependence implements NickNameHook {

    private INametagApi nametagApi;

    public NameTagEditHook() {
        super("NametagEdit");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            nametagApi = NametagEdit.getApi();
            if (nametagApi == null) {
                enabled = false;
                return true;
            }
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into NametagEdit!"));
        }
        return true;
    }


    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        final Nametag tag = this.nametagApi.getNametag(player);
        return !tag.getPrefix().isEmpty() || !tag.getSuffix().isEmpty();
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {

    }

    @Override
    public void unNick(@NotNull Player player) {

    }

    @Override
    public String getNickName(@NotNull Player player) {
        final Nametag tag = this.nametagApi.getNametag(player);
        return isPlayerNicked(player) ? tag.getPrefix() + getRealName(player) + tag.getSuffix() : getRealName(player);
    }

}
