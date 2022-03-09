package org.zibble.dbedwars.game.setup;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.PreciseLocation;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Duration;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class SetupUtil {

    private static final DBedwars PLUGIN = DBedwars.getInstance();

    public static boolean isAllowedEntity(Entity entity) {
        return entity instanceof Painting || entity instanceof ItemFrame;
    }

    public static Location precise(SetupSession setupSession, Location location) {
        if (!setupSession.autoCorrect) return location;
        final Location[] returnVal = new Location[1];
        PLUGIN.getFeatureManager().runFeature(BedWarsFeatures.PRECISE_LOCATION, PreciseLocation.class, value -> {
            returnVal[0] = value.getPrecise(location);
            return true;
        });
        return returnVal[0];
    }

    public static Color[] findTeams(SetupSession setupSession) {
        return new Color[0];
    }

    public static CancellableWorkload createParticleSpawningTask(Location location, Player player, java.awt.Color color) {
        ParticleEffectASC particleEffect = new ParticleEffectASC(ParticleEffect.REDSTONE,1, 0, color);
        ParticleBuilder builder = particleEffect.build().setLocation(location.clone().add(0, 1, 0));
        return PLUGIN.getThreadHandler().runTaskTimer(() -> builder.display(player), Duration.ofMilliseconds(50));
    }

    public static Hologram createHologram(Location location, Player player, Message text) {
        //TODO INITIALIZE HOLOGRAM WITH ADDING 2 in Y of location
        Hologram hologram = null;
        HologramPage page = hologram.addPage();
        for (Component component : text.asComponent()) {
            page.addNewTextLine(component);
        }
        hologram.changeViewerPage(player.getUniqueId(), 0);
        return hologram;
    }

}
