package org.zibble.dbedwars.hooks.cmi.nickname;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

public class CMINickHook extends PluginDependence implements NickNameHook {

    private CMI api;

    public CMINickHook() {
        super("CMI");
    }

    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        final CMIUser cmiUser = api.getPlayerManager().getUser(player);

        if(cmiUser == null)
            return false;

        return cmiUser.getNickName() != null || !cmiUser.getNickName().equals(player.getName());
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {
        final CMIUser cmiUser = api.getPlayerManager().getUser(player);

        if(cmiUser == null)
            return;

        cmiUser.setNickName(nick,true);
    }

    @Override
    public void unNick(@NotNull Player player) {
        final CMIUser cmiUser = api.getPlayerManager().getUser(player);

        if(cmiUser == null)
            return;

        cmiUser.setNickName(null,false);
    }

    @Override
    public String getNickName(@NotNull Player player) {
        final CMIUser cmiUser = api.getPlayerManager().getUser(player);

        if(cmiUser == null)
            return getRealName(player);
        return isPlayerNicked(player) ? cmiUser.getNickName() : getRealName(player);

    }

    @Override
    public Boolean apply(Plugin plugin) {
        if(plugin != null){
            api = CMI.getInstance();
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into CMI!"));
        }
        return true;
    }
}
