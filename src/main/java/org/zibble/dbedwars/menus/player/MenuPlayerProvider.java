package org.zibble.dbedwars.menus.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.handler.MenuHandler;

import java.util.HashMap;
import java.util.UUID;

public class MenuPlayerProvider implements Listener {

    private static final HashMap<UUID, MenuPlayer> playerCache;

    static {
        playerCache = new HashMap<>();
    }

    private final MenuHandler handler;

    public MenuPlayerProvider(MenuHandler handler) {
        this.handler = handler;
        this.handler.getPlugin().getServer().getPluginManager().registerEvents(this, handler.getPlugin());
    }

    @Nullable
    public static MenuPlayer of(@NotNull final Player player){
        return playerCache.get(player.getUniqueId());
    }

    @Nullable
    public static MenuPlayer of(@NotNull final UUID uuid){
        return playerCache.get(uuid);
    }

    public static MenuPlayer update(@NotNull final Player player){
        playerCache.remove(player.getUniqueId());
        final MenuPlayer menuPlayer = new MenuPlayer(player);
        playerCache.put(player.getUniqueId(), menuPlayer);
        return menuPlayer;
    }

    public static boolean contains(@NotNull final Player player){
        return playerCache.containsKey(player.getUniqueId());
    }

    public static boolean contains(@NotNull final UUID playerUID){
        return playerCache.containsKey(playerUID);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        playerCache.put(event.getPlayer().getUniqueId(), new MenuPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        playerCache.remove(event.getPlayer().getUniqueId());
    }

    public void flushProvider(){
        playerCache.clear();
    }

}
