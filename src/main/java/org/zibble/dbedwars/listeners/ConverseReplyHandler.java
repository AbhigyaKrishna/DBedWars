package org.zibble.dbedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ConverseReplyHandler implements Listener {

    public static final ConverseReplyHandler INSTANCE = new ConverseReplyHandler();

    private final Map<Player, Function<AsyncPlayerChatEvent, Boolean>> listenableReplies = new ConcurrentHashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void handleChat(AsyncPlayerChatEvent event) {
        if (listenableReplies.containsKey(event.getPlayer())) {
            event.setCancelled(true);

            if (listenableReplies.get(event.getPlayer()).apply(event)) {
                listenableReplies.remove(event.getPlayer());
            }
        }
    }

    public void listenForReply(Player player, Function<AsyncPlayerChatEvent, Boolean> reply) {
        this.listenableReplies.put(player, reply);
    }

}
