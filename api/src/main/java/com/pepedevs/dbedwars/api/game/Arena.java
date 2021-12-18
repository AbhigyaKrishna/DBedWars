package com.pepedevs.dbedwars.api.game;

import com.pepedevs.dbedwars.api.game.settings.ArenaSettings;
import com.pepedevs.dbedwars.api.game.spawner.Spawner;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.KickReason;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface Arena {

    ArenaSettings getSettings();

    World getWorld();

    void setWorld(World world);

    ArenaStatus getStatus();

    void setStatus(ArenaStatus status);

    boolean saveWorld(String worldFolder, boolean overwriteCache);

    boolean saveData(boolean overwriteData);

    void reloadData();

    World loadWorld();

    void load();

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

    void broadcast(String msg);

    void broadcast(String msg, Predicate<ArenaPlayer> condition);

    void broadcast(BaseComponent[] components);

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

}
