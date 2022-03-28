package org.zibble.dbedwars.api.game;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;

import java.util.UUID;

public interface ArenaSpectator extends PlayerMember {

    UUID getUUID();

    String getName();

    Arena getArena();

    Player getPlayer();

    void teleport(double x, double y, double z);

    void teleport(Location location);

    void teleport(LocationXYZ location);

    void teleport(LocationXYZYP location);

    GameMode getGameMode();

    void hide();

    void show();

}
