package org.zibble.dbedwars.hooks.premiumvanish;

import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.vanish.VanishHook;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

public class PremiumVanishHook extends PluginDependence implements VanishHook {

    public PremiumVanishHook() {
        super("PremiumVanish");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into PremiumVanish!"));
        }
        return true;
    }

    @Override
    public boolean isVanished(@NotNull Player player) {
        return VanishAPI.isInvisible(player);
    }

    @Override
    public void vanish(@NotNull Player player) {
        VanishAPI.hidePlayer(player);
    }

    @Override
    public void unVanish(@NotNull Player player) {
        VanishAPI.showPlayer(player);
    }

}
