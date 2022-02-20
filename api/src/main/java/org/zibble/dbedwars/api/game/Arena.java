package org.zibble.dbedwars.api.game;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.game.settings.ArenaSettings;
import org.zibble.dbedwars.api.game.spawner.Spawner;
import org.zibble.dbedwars.api.messaging.AbstractMessaging;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.KickReason;
import org.zibble.dbedwars.api.util.LocationXYZ;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Arena extends AbstractMessaging {

    ArenaSettings getSettings();

    World getWorld();

    void setWorld(World world);

    ArenaStatus getStatus();

    void setStatus(ArenaStatus status);

    boolean saveWorld(String worldFolder, boolean overwriteCache);

    boolean saveData(boolean overwriteData);

    void reloadData();

    ActionFuture<World> loadWorld();

    ActionFuture<World> load();

    void enable(boolean flag);

    Team getTeam(Color color);

    List<Team> getTeams();

    List<ArenaPlayer> getSpectators();

    void addSpawner(Spawner spawner);

    void removeSpawner(Spawner spawner);

    List<Spawner> getSpawners();

    Optional<Spawner> getSpawner(LocationXYZ location, float range);

    boolean isCurrentlyRegenerating();

    boolean isConfigured();

    boolean isEnabled();

    boolean start();

    boolean end();

    void addPlayer(Player player);

    void addPlayer(ArenaPlayer player);

    void addPlayer(Player player, Team team);

    void addPlayer(ArenaPlayer player, Team team);

    boolean kickPlayer(ArenaPlayer player);

    boolean kickPlayer(ArenaPlayer player, KickReason reason);

    void kickAllPlayers();

    void kickAllPlayers(KickReason reason);

    Block setBlock(LocationXYZ location, Material material);

    Block setBlock(Block block, Material material);

    long getGameStartTime();

    long getRunningTime();

    boolean isSleeping();

    Set<ArenaPlayer> getPlayersInTeam(Team team);

    Set<Team> getRemainingTeams();

    Set<ArenaPlayer> getPlayers();

    Optional<ArenaPlayer> getAsArenaPlayer(Player player);

    boolean isArenaPlayer(Player player);

    void destroyBed(ArenaPlayer player, Team affected);

    void setIngameScoreboard(ArenaPlayer player);

    void setLobbyScoreboard(Player player);

    boolean stop();

    String toString();
}