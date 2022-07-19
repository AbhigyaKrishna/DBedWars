package org.zibble.dbedwars.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.placeholders.PlayerPlaceholderEntry;
import org.zibble.dbedwars.io.GameProfileFetcher;
import org.zibble.dbedwars.io.UUIDFetcher;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class JoinLeaveListener implements Listener {

    private final DBedwars plugin;

    public JoinLeaveListener(DBedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.plugin.getDataHandler().tryInsertNew(player.getUniqueId(), player.getName())
                .thenCompose(v -> this.plugin.getDataHandler().loadAll(player.getUniqueId()));

        GameProfileFetcher.getInstance().updateCache(player);
        UUIDFetcher.getInstance().updateCache(player);

        ScoreboardData scoreboard = this.plugin.getGameManager().getScoreboardData().get("lobby");
        scoreboard.show(player,
                PlaceholderEntry.symbol("date", SimpleDateFormat.getDateInstance().format(Date.from(Instant.now()))),
                PlayerPlaceholderEntry.symbol("player_name", HumanEntity::getName),
                PlayerPlaceholderEntry.symbol("player_uuid", p -> p.getUniqueId().toString())
        );
    }

    @EventHandler
    public void handleLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.plugin.getDataHandler().saveAll(player.getUniqueId());

        this.plugin.getHookManager().getScoreboardHook().removeScoreboard(player);
    }

}
