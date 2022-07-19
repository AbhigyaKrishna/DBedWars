package org.zibble.dbedwars;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.handler.*;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.nms.NMSAdaptor;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.version.Version;

import java.util.Optional;

public final class APIImpl extends DBedWarsAPI {

    private final DBedwars plugin;

    public APIImpl(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public Version getServerVersion() {
        return this.plugin.getServerVersion();
    }

    @Override
    public String getPluginVersion() {
        return this.plugin.getVersion();
    }

    @Override
    public GameManager getGameManager() {
        return this.plugin.getGameManager();
    }

    @Override
    public CustomItemHandler getCustomItemHandler() {
        return this.plugin.getCustomItemHandler();
    }

    @Override
    public ThreadHandler getThreadHandler() {
        return this.plugin.getThreadHandler();
    }

    @Override
    public HookManager getHookManager() {
        return this.plugin.getHookManager();
    }

    @Override
    public NMSAdaptor getNMS() {
        return this.plugin.getNMSAdaptor();
    }

    @Override
    public BwItemStack getConfiguredItem(String key, Optional<Player> player, Placeholder... placeholders) {
        return BwItemStack.fromJson(this.plugin.getConfigHandler().getJsonItem().get(key), player, placeholders);
    }

    @Override
    public Version getVersion() {
        return this.plugin.getServerVersion();
    }

    @Override
    public DBedwars getPlugin() {
        return this.plugin;
    }

}
