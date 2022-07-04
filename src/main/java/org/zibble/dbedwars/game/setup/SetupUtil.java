package org.zibble.dbedwars.game.setup;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.PreciseLocationFeature;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Pair;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class SetupUtil {

    private static final DBedwars PLUGIN = DBedwars.getInstance();

    public static boolean isAllowedEntity(Entity entity) {
        return entity instanceof Painting || entity instanceof ItemFrame || entity instanceof Player;
    }

    public static Location precise(Location location) {
        AtomicReference<Location> loc = new AtomicReference<>(location);
        PLUGIN.getFeatureManager().runFeature(BedWarsFeatures.PRECISE_LOCATION_FEATURE, PreciseLocationFeature.class, value -> {
            loc.set(value.getPrecise(location));
            return true;
        });
        return loc.get();
    }

    public static LocationXYZ preciseXYZ(Location location) {
        return LocationXYZ.valueOf(precise(location));
    }

    public static LocationXYZYP preciseXYZYP(Location location) {
        return LocationXYZYP.valueOf(precise(location));
    }

    public static CancellableWorkload createParticleSpawningTask(Location location, Player player, java.awt.Color color) {
        ParticleEffectASC particleEffect = ParticleEffectASC.of(ParticleEffect.REDSTONE, 25, 0, color);
        ParticleBuilder builder = particleEffect.build().setLocation(location.clone().add(0, 1, 0));
        return PLUGIN.getThreadHandler().runTaskTimer(() -> builder.display(player), Duration.ofTicks(1));
    }

    public static CancellableWorkload createParticleSpawningTask(Location location, Player player, Color color) {
        return createParticleSpawningTask(location, player, color.asJavaColor());
    }

    public static CancellableWorkload createLobbyAreaBoxParticle(BoundingBox box, Player player, World world) {
        ParticleEffectASC particleEffect = ParticleEffectASC.of(ParticleEffect.FLAME, 5, 0);
        List<Vector> locations = box.getOutline(0.5);
        return PLUGIN.getThreadHandler().runTaskTimer(() -> {
            for (Vector location : locations) {
                particleEffect.display(location.toLocation(world), player);
            }
        }, Duration.ofTicks(5));
    }

    public static Hologram createHologram(Location location, Player player, Message text) {
        Hologram hologram = PLUGIN.getHookManager().getHologramFactory().createHologram(location.clone().add(0, 2, 0));
        hologram.show(player);
        HologramPage page = hologram.addPage();
        for (Message message : text.splitToLineMessage()) {
            page.addNewTextLine(message);
        }
        hologram.changeViewerPage(player, page.getPageNumber());
        return hologram;
    }

    public static Color getNearestTeamViaHologram(Location location, SetupSession setupSession, double radix) {
        Color nearest = null;
        double distance = radix;
        for (Map.Entry<Color, Pair<SetupSession.TeamTracker<Hologram>, SetupSession.TeamTracker<CancellableWorkload>>> entry : setupSession.getTeamTasks().entrySet()) {
            if (entry.getValue().getKey().getSpawn() != null && entry.getValue().getKey().getSpawn().getLocation().distance(location) < distance) {
                distance = entry.getValue().getKey().getSpawn().getLocation().distance(location);
                nearest = entry.getKey();
            }
            if (entry.getValue().getKey().getBed() != null && entry.getValue().getKey().getBed().getLocation().distance(location) < distance) {
                distance = entry.getValue().getKey().getBed().getLocation().distance(location);
                nearest = entry.getKey();
            }
            for (Hologram shop : entry.getValue().getKey().getShops()) {
                if (shop.getLocation().distance(location) < distance) {
                    distance = shop.getLocation().distance(location);
                    nearest = entry.getKey();
                }
            }
            for (Hologram spawner : entry.getValue().getKey().getSpawners()) {
                if (spawner.getLocation().distance(location) < distance) {
                    distance = spawner.getLocation().distance(location);
                    nearest = entry.getKey();
                }
            }
        }
        return nearest;
    }

    public static String createPatternForTeams(char c, Set<Color> teams) {
        StringBuilder builder = new StringBuilder();
        for (Color color : teams) {
            builder.append(color.getMiniCode())
                    .append(c)
                    .append(new StringBuilder(color.getMiniCode()).insert(1, "/"))
                    .append(", ");
        }
        return builder.delete(builder.length() - 2, builder.length()).toString();
    }

}
