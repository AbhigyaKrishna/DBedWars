package org.zibble.dbedwars.hooks.cmi.nickname;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;

public class CMINick implements NickNameHook {

    @Override
    public boolean isPlayerNicked(@NotNull Player player) {
        final CMIUser cmiUser = CMI.getInstance().getPlayerManager().getUser(player);

        if (cmiUser == null)
            return false;

        return cmiUser.getNickName() != null || !cmiUser.getNickName().equals(player.getName());
    }

    @Override
    public void nick(@NotNull Player player, @NotNull String nick) {
        final CMIUser cmiUser = CMI.getInstance().getPlayerManager().getUser(player);

        if (cmiUser == null)
            return;

        cmiUser.setNickName(nick, true);
    }

    @Override
    public void unNick(@NotNull Player player) {
        final CMIUser cmiUser = CMI.getInstance().getPlayerManager().getUser(player);

        if (cmiUser == null)
            return;

        cmiUser.setNickName(null, false);
    }

    @Override
    public String getNickName(@NotNull Player player) {
        final CMIUser cmiUser = CMI.getInstance().getPlayerManager().getUser(player);

        if (cmiUser == null)
            return getRealName(player);
        return isPlayerNicked(player) ? cmiUser.getNickName() : getRealName(player);
    }

}
