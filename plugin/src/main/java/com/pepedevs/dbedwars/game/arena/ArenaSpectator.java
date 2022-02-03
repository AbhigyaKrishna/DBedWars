package com.pepedevs.dbedwars.game.arena;

import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArenaSpectator extends PlayerMember implements com.pepedevs.dbedwars.api.game.ArenaSpectator {

    private final UUID uuid;
    private final String name;
    private final Arena arena;

    public ArenaSpectator(Player player, Arena arena) {
        this(player, arena, false);
    }

    public ArenaSpectator(Player player, Arena arena, boolean teleportSpawn) {
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

}
