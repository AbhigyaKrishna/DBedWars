package org.zibble.dbedwars.game.arena;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaSpectator;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.messaging.member.PlayerMember;

import java.util.UUID;

public class ArenaSpectatorImpl extends PlayerMember implements ArenaSpectator {

    protected final UUID uuid;
    protected final String name;
    protected final Arena arena;

    public ArenaSpectatorImpl(Player player, Arena arena) {
        this(player, arena, false);
    }

    public ArenaSpectatorImpl(Player player, Arena arena, boolean teleportSpawn) {
        super(player);
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.arena = arena;
        if (teleportSpawn) {
            // Teleport to spawn
            this.teleport(this.arena.getSettings().getSpectatorLocation());
        }
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public void teleport(double x, double y, double z) {
        this.getPlayer().teleport(new Location(this.arena.getWorld(), x, y, z));
    }

    @Override
    public void teleport(Location location) {
        this.getPlayer().teleport(location);
    }

    @Override
    public void teleport(LocationXYZ location) {
        location.teleport(this.arena.getWorld(), this.getPlayer());
    }

    @Override
    public void teleport(LocationXYZYP location) {
        location.teleport(this.arena.getWorld(), this.getPlayer());
    }

    @Override
    public GameMode getGameMode() {
        return this.getPlayer().getGameMode();
    }

}
