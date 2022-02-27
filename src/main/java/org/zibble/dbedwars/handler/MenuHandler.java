package org.zibble.dbedwars.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.menus.actions.ActionRegistry;
import org.zibble.dbedwars.menus.MenuPlayer;

import java.util.HashMap;

public class MenuHandler implements Listener {

    private static final HashMap<Player, MenuPlayer> playerCache;

    static {
        playerCache = new HashMap<>();
    }

    private final DBedwars plugin;
    private final ActionRegistry actionRegistry;

    public MenuHandler(DBedwars plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.actionRegistry = new ActionRegistry(this);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        playerCache.put(event.getPlayer(), new MenuPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        playerCache.remove(event.getPlayer());
    }

    public static MenuPlayer ofMenuPlayer(@NotNull Player player){
        return playerCache.get(player);
    }

    public DBedwars getPlugin() {
        return plugin;
    }

    public ActionRegistry getActionRegistry() {
        return actionRegistry;
    }
}
