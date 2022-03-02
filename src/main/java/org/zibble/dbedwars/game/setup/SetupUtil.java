package org.zibble.dbedwars.game.setup;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.zibble.dbedwars.api.util.Color;

public class SetupUtil {

    public static boolean isAllowedEntity(Entity entity) {
        return entity instanceof Painting || entity instanceof ItemFrame;
    }

    public static Location precise(SetupSession setupSession, Location location) {

    }

    public static Color[] findTeams(SetupSession setupSession) {

    }

}
