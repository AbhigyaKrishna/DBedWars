package org.zibble.dbedwars.api;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.handler.*;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.nms.NMSAdaptor;
import org.zibble.dbedwars.api.plugin.Plugin;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.version.Version;

import java.util.Optional;

public abstract class DBedWarsAPI {

    private static DBedWarsAPI INSTANCE = null;

    public DBedWarsAPI() {
        INSTANCE = this;
    }

    public static DBedWarsAPI getApi() {
        return INSTANCE;
    }

    public abstract Version getServerVersion();

    public abstract String getPluginVersion();

    public abstract GameManager getGameManager();

    public abstract CustomItemHandler getCustomItemHandler();

    public abstract ThreadHandler getThreadHandler();

    public abstract HookManager getHookManager();

    public abstract NMSAdaptor getNMS();

    public abstract BwItemStack getConfiguredItem(String key, Optional<Player> player, Placeholder... placeholders);

    public abstract Version getVersion();

    public abstract Plugin getPlugin();

}
