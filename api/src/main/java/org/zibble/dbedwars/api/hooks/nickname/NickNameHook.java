package org.zibble.dbedwars.api.hooks.nickname;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.Hook;

public interface NickNameHook extends Hook {

    boolean isPlayerNicked(@NotNull final Player player);

    void nick(@NotNull final Player player, @NotNull final String nick);

    void unNick(@NotNull final Player player);

    String getNickName(@NotNull final Player player);

    default String getRealName(@NotNull final Player player) {
        return player.getName();
    }

}
