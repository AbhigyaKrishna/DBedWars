package org.zibble.dbedwars.api.game;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.game.settings.ArenaSettings;
import org.zibble.dbedwars.api.game.spawner.Spawner;
import org.zibble.dbedwars.api.messaging.AbstractMessaging;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.KickReason;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Arena extends AbstractMessaging {

    String getName();

    ArenaSettings getSettings();

    World getWorld();

    ArenaDataHolder getDataHolder();

    ArenaStatus getStatus();

    void setStatus(ArenaStatus status);

    ActionFuture<World> loadWorld();

    ActionFuture<World> load();

    Team getTeam(Color color);

    Set<Team> getTeams();

    List<ArenaSpectator> getSpectators();

    void addSpawner(Spawner spawner);

    void removeSpawner(Spawner spawner);

    List<Spawner> getSpawners();

    Optional<Spawner> getSpawner(LocationXYZ location, float range);

    boolean isCurrentlyRegenerating();

    boolean start();

    boolean end();

    void joinGame(Player player);

    void joinGame(ArenaPlayer player);

    void joinGame(Player player, Team team);

    void joinGame(ArenaPlayer player, Team team);

    boolean kickPlayer(ArenaPlayer player);

    boolean kickPlayer(ArenaPlayer player, KickReason reason);

    void kickAllPlayers();

    void kickAllPlayers(KickReason reason);

    Block setBlock(LocationXYZ location, Material material);

    Block setBlock(Block block, Material material);

    Instant getGameStartTime();

    Duration getRunningTime();

    boolean isSleeping();

    Set<Team> getRemainingTeams();

    Set<ArenaPlayer> getPlayers();

    Optional<ArenaPlayer> getAsArenaPlayer(Player player);

    boolean isArenaPlayer(Player player);

    void destroyBed(ArenaPlayer player, Team affected);

    boolean stop();

    String toString();
}
