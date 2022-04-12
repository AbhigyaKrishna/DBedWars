package org.zibble.dbedwars.api.game;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.game.settings.ArenaSettings;
import org.zibble.dbedwars.api.game.spawner.Spawner;
import org.zibble.dbedwars.api.game.statistics.BedBrokenStatistics;
import org.zibble.dbedwars.api.game.statistics.DeathStatistics;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.messaging.AbstractMessaging;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.util.KickReason;
import org.zibble.dbedwars.api.util.mixin.Tickable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Arena extends AbstractMessaging, Tickable {

    String getName();

    String getGameId();

    ArenaSettings getSettings();

    World getWorld();

    ArenaDataHolder getDataHolder();

    ArenaCategory getCategory();

    ArenaStatus getStatus();

    void setStatus(ArenaStatus status);

    ActionFuture<World> loadWorld();

    ActionFuture<World> load();

    Team getTeam(Color color);

    Set<? extends Team> getTeams();

    List<? extends ArenaSpectator> getSpectators();

    void addSpawner(Spawner spawner);

    void removeSpawner(Spawner spawner);

    List<Spawner> getSpawners();

    Optional<Spawner> getSpawner(LocationXYZ location, float range);

    boolean isCurrentlyRegenerating();

    boolean isSleeping();

    boolean isRunning();

    void scheduleStart(int countdown);

    boolean interruptStart();

    boolean start(boolean force);

    boolean end();

    boolean joinGame(Player player);

    boolean joinGame(Player player, Color team);

    boolean rejoinGame(Player player);

    boolean spectateGame(Player player);

    default boolean kickPlayer(Player player) {
        return this.kickPlayer(player, KickReason.UNKNOWN);
    }

    boolean kickPlayer(Player player, KickReason reason);

    default void kickAllPlayers() {
        this.kickAllPlayers(KickReason.UNKNOWN);
    }

    void kickAllPlayers(KickReason reason);

    void removeSpectator(Player spectator);

    Block setBlock(LocationXYZ location, XMaterial material);

    Block setBlock(Block block, XMaterial material);

    Instant getGameStartTime();

    Duration getRunningTime();

    Set<? extends Team> getRemainingTeams();

    Set<? extends ArenaPlayer> getPlayers();

    Optional<? extends ArenaPlayer> getAsArenaPlayer(Player player);

    boolean isArenaPlayer(Player player);

    void destroyBed(ArenaPlayer player, Team affected);

    ScoreboardData getGameLobbyScoreboard();

    void setGameLobbyScoreboard(ScoreboardData gameLobbyScoreboard, boolean update);

    ScoreboardData getGameScoreboard();

    void setGameScoreboard(ScoreboardData gameScoreboard, boolean update);

    DeathStatistics getDeathStatistics();

    BedBrokenStatistics getBedBrokenStatistics();

    boolean stop();

    String toString();

}
