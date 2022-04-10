package org.zibble.dbedwars.hooks.cmi.vanish;

import com.Zrips.CMI.CMI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.vanish.VanishHook;

public class CMIVanish implements VanishHook {

    public CMIVanish() {

    }

    @Override
    public boolean isVanished(@NotNull Player player) {
        return CMI.getInstance().getPlayerManager().getUser(player).isVanished();
    }

    @Override
    public void vanish(@NotNull Player player) {
        CMI.getInstance().getPlayerManager().getUser(player).setVanished(true);
    }

    @Override
    public void unVanish(@NotNull Player player) {
        CMI.getInstance().getPlayerManager().getUser(player).setVanished(false);
    }

}
