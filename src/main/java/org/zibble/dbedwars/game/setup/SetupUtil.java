package org.zibble.dbedwars.game.setup;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.PreciseLocationFeature;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.utils.Util;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

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
        for (Map.Entry<Color, SetupSession.TeamTracker<Hologram>> entry : setupSession.getTeamHolograms().entrySet()) {
            if (entry.getValue().getSpawn() != null && entry.getValue().getSpawn().getLocation().distance(location) < distance) {
                distance = entry.getValue().getSpawn().getLocation().distance(location);
                nearest = entry.getKey();
            }
            if (entry.getValue().getBed() != null && entry.getValue().getBed().getLocation().distance(location) < distance) {
                distance = entry.getValue().getBed().getLocation().distance(location);
                nearest = entry.getKey();
            }
            for (Hologram shop : entry.getValue().getShops()) {
                if (shop.getLocation().distance(location) < distance) {
                    distance = shop.getLocation().distance(location);
                    nearest = entry.getKey();
                }
            }
            for (Hologram spawner : entry.getValue().getSpawners()) {
                if (spawner.getLocation().distance(location) < distance) {
                    distance = spawner.getLocation().distance(location);
                    nearest = entry.getKey();
                }
            }
        }
        return nearest;
    }

    public static Message createPromptMessage(SetupSession session) {
        MissingArenaData data = new MissingArenaData(session);
        Set<Color> teamMissingBed = data.getTeamsWithMissingBed();
        Set<Color> teamMissingSpawn = data.getTeamsWithMissingSpawn();
        String[] lines = new String[]{
                data.hasWaitingLocation() ? "<gray><st><hover:show_text:'<aqua>/bw setup lobby</aqua>'><click:run_command:'/bw setup lobby'>Set waiting location</click></hover></st></gray>" :
                        "<yellow><hover:show_text:'<aqua>/bw setup lobby</aqua>'><click:run_command:'/bw setup lobby'>Set waiting location</click></hover></yellow> <red><bold>[REQUIRED]</bold></red>",
                data.hasSpectatorLocation() ? "<gray><st><hover:show_text:'<aqua>/bw setup spectator</aqua>'><click:run_command:'/bw setup spectator'>Set spectator location</click></hover></st></gray>" :
                        "<yellow><hover:show_text:'<aqua>/bw setup spectator</aqua>'><click:run_command:'/bw setup spectator'>Set spectator location</click></hover></yellow> <gray>(OPTIONAL)</gray>",
                data.hasCustomName() ? "<gray><hover:show_text:'<aqua>/bw setup name <gold>[name]</gold></aqua>'><click:suggest_command:'/bw setup name '>Set custom name</click></hover></gray> <green>[<custom_name>]</green>" :
                        "<yellow><hover:show_text:'<aqua>/bw setup name <gold>[name]</gold></aqua>'><click:suggest_command:'/bw setup name '>Set custom name</click></hover></yellow> <gray>(OPTIONAL)</gray>",
                data.hasLobbyArea() ? "<gray><st><hover:show_text:'<aqua>/bw setup lobbyarea <gold>(1/2)</gold></aqua>'><click:suggest_command:'/bw setup lobbyarea'>Set lobby corners</click></hover></st></gray>" :
                        "<yellow><hover:show_text:'<aqua>/bw setup lobbyarea <gold>(1/2)</gold></aqua>'><click:suggest_command:'/bw setup lobbyarea'>Set lobby corners (" + ((data.hasLobbyCorner1() ? 1 : 0) + (data.hasLobbyCorner2() ? 1 : 0)) + "/2)</click></hover></yellow> <gray>(OPTIONAL)</gray>",
                "<yellow><hover:show_text:'<aqua>/bw setup addteam <gold><team_color></gold></aqua>'><click:suggest_command:'/bw setup addteam '>Add arena team</click></hover></yellow>",
                "<yellow><hover:show_text:'<aqua>/bw setup removeteam <gold><team_color></gold></aqua>'><click:suggest_command:'/bw setup removeteam '>Remove arena team</click></hover></yellow>",
                teamMissingBed.isEmpty() ? "<gray><st><hover:show_text:'/bw setup setbed <gold>(color)</gold>'><click:suggest_command:'/bw setup setbed'>Set team bed</click></hover></st></gray> <green>(DONE)</green>" :
                        "<yellow><hover:show_text:'/bw setup setbed <gold>(color)</gold>'><click:suggest_command:'/bw setup setbed'>Set team bed</click></hover></yellow> <gray>(" + createPatternForTeams('x', teamMissingBed) + ")</gray>",
                teamMissingSpawn.isEmpty() ? "<gray><st><hover:show_text:'/bw setup setspawn <gold>(color)</gold>'><click:suggest_command:'/bw setup setspawn'>Set team spawn</click></hover></st></gray> <green>(DONE)</green>" :
                        "<yellow><hover:show_text:'/bw setup setspawn <gold>(color)</gold>'><click:suggest_command:'/bw setup setspawn'>Set team spawn</click></hover></yellow> <gray>(" + createPatternForTeams('x', teamMissingSpawn) + ")</gray>",
                "<yellow><hover:show_text:'/bw setup addshop <gold>[shop] (color)</gold>'><click:suggest_command:'/bw setup addshop '>Set team shop</click></hover></yellow>",
                "<yellow><hover:show_text:'/bw setup addspawner <gold>[spawner] (color)</gold>'><click:suggest_command:'/bw setup addspawner '>Add spawners</click></hover></yellow>",
        };
        return AdventureMessage.from(lines, PlaceholderEntry.symbol("custom_name", session.getArenaDataHolder().getCustomName() != null ? Util.convertMessage(session.getArenaDataHolder().getCustomName(), AdventureMessage.empty()).getMessage() : ""));
    }

    private static String createPatternForTeams(char c, Set<Color> teams) {
        StringBuilder builder = new StringBuilder();
        for (Color color : teams) {
            builder.append(color.getMiniCode())
                    .append(c)
                    .append(new StringBuilder(color.getMiniCode()).insert(1, "\\"))
                    .append(", ");
        }
        return builder.delete(builder.length() - 2, builder.length()).toString();
    }

}
