package org.zibble.dbedwars.game.setup;

import com.cryptomorin.xseries.XSound;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.language.Lang;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.ArenaDataHolder;
import org.zibble.dbedwars.guis.setup.ArenaNameGui;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.dbedwars.utils.gamerule.GameRuleType;
import org.zibble.inventoryframework.protocol.ItemMaterials;
import org.zibble.inventoryframework.protocol.item.StackItem;

import java.util.*;

public class SetupSession {

    private static final Messaging MESSAGING = Messaging.getInstance();
    private static final String TEAM_SETUP_SYMBOL = "[]";

    private static final Key<String> WAITING_LOCATION = Key.of("waiting_location");
    private static final Key<String> SPECTATOR_LOCATION = Key.of("spectator_location");

    private final ArenaDataHolder dataHolder;
    private final World world;

    private final Player player;
    private final PlayerMember playerMember;

    public boolean autoCorrect;

    private final Map<Key<String>, CancellableWorkload> particleTrackers;
    private final Map<Key<String>, Hologram> hologramTrackers;

    private final EnumMap<Color, TeamParticleTracker> teamParticleTrackers;
    private final EnumMap<Color, TeamHologramTracker> teamHologramTrackers;

    protected SetupSession(World world, Player player, ArenaDataHolder dataHolder) {
        this.player = player;
        this.world = world;
        this.playerMember = MESSAGING.getMessagingMember(player);
        this.dataHolder = dataHolder;

        this.particleTrackers = new HashMap<>();
        this.hologramTrackers = new HashMap<>();

        this.teamParticleTrackers = new EnumMap<>(Color.class);
        this.teamHologramTrackers = new EnumMap<>(Color.class);

    }

    public void init() {
        this.player.teleport(world.getSpawnLocation());
        new SoundVP(XSound.ENTITY_PLAYER_LEVELUP, 1, 1).play(this.player);
        this.player.setGameMode(GameMode.CREATIVE);
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
        Message startMessage = PluginLang.SETUP_START.asMessage();
        startMessage.addPlaceholders(PlaceholderEntry.symbol("world", this.world.getName()));
        this.playerMember.sendMessage(startMessage);
    }

    public void promptArenaCustomNameSet() {
        Message message = PluginLang.SETUP_ARENA_DISPLAY_NAME_SET.asMessage();
        ArenaNameGui.creator()
                .item(() -> {
                    StackItem item = new StackItem(ItemMaterials.PAPER);
                    item.setDisplayName(ConfigMessage.from(this.world.getName()).asComponentWithPAPI(this.player)[0]);
                    return item;
                })
                .outputClick((menu, s) -> {
                    this.dataHolder.setCustomName(ConfigMessage.from(s));
                    message.addPlaceholders(PlaceholderEntry.symbol("arena_custom_name", s));
                    this.playerMember.sendMessage(message, true);
                })
                .addCloseAction((menu, player) -> {
                    this.dataHolder.setCustomName(ConfigMessage.from(this.world.getName()));
                    message.addPlaceholders(PlaceholderEntry.symbol("arena_custom_name", this.world.getName()));
                    this.playerMember.sendMessage(message);
                })
                .open(this.player);
    }

    public void promptDisableMobSpawning() {
        this.playerMember.sendMessage(PluginLang.SETUP_DISABLE_MOB_SPAWNING_PROMPT.asMessage());
    }

    public void disableMobSpawning() {
        GameRuleType.MOB_SPAWNING.apply(this.world, false);
        this.playerMember.sendMessage(PluginLang.SETUP_WORLD_MOB_SPAWNING_DISABLED.asMessage());
    }

    public void promptCleanupWorldEntity() {
        this.playerMember.sendMessage(PluginLang.SETUP_WORLD_CLEANUP_PROMPT.asMessage());
    }

    public int cleanupWorldEntities() {
        int count = 0;
        for (Entity entity : this.world.getEntities()) {
            if (SetupUtil.isAllowedEntity(entity)) continue;
            entity.remove();
            count++;
        }
        this.playerMember.sendMessage(PluginLang.SETUP_WORLD_CLEANUP_CLEANING.asMessage());
        return count;
    }

    public void promptSetupWaitingLocation() {
        this.playerMember.sendMessage(PluginLang.SETUP_WAITING_LOCATION_PROMPT.asMessage());
    }

    public void setupWaitingLocation(Location location) {
        this.dataHolder.setWaitingLocation(LocationXYZYP.valueOf(SetupUtil.precise(this, location)));
        Pair<Hologram, CancellableWorkload> pair = this.locationSetTasks(location, Color.WHITE, PluginLang.HOLOGRAM_SETUP_WAITING_LOCATION_SET);
        Hologram old = this.hologramTrackers.put(WAITING_LOCATION, pair.getKey());
        //DESPAWN OLD
        CancellableWorkload oldWorkload = this.particleTrackers.put(WAITING_LOCATION, pair.getValue());
        if (oldWorkload != null) oldWorkload.setCancelled(true);
        this.locationSetMessages(location, Color.WHITE, PluginLang.SETUP_WAITING_LOCATION_SET);
    }

    public void promptSetupSpectatorLocation() {
        this.playerMember.sendMessage(PluginLang.SETUP_SPECTATOR_LOCATION_PROMPT.asMessage());
    }

    public void setupSpectatorLocation(Location location) {
        this.dataHolder.setSpectatorLocation(SetupUtil.preciseXYZYP(this, location));
        Pair<Hologram, CancellableWorkload> pair = this.locationSetTasks(location, Color.WHITE, PluginLang.HOLOGRAM_SETUP_SPECTATOR);
        Hologram old = this.hologramTrackers.put(SPECTATOR_LOCATION, pair.getKey());
        //DESPAWN OLD
        CancellableWorkload oldWorkload = this.particleTrackers.put(SPECTATOR_LOCATION, pair.getValue());
        if (oldWorkload != null) oldWorkload.setCancelled(true);
        this.locationSetMessages(location, Color.WHITE, PluginLang.MESSAGE_SETUP_SPECTATOR);
    }

    public void setupLobbyCorner1(Block block) {
        this.dataHolder.setLobbyCorner1(LocationXYZ.valueOf(block.getLocation()));
        this.locationSetMessages(block.getLocation(), Color.WHITE, PluginLang.MESSAGE_SETUP_LOBBY_CORNER_1);
    }

    public void setupLobbyCorner2(Block block) {
        this.dataHolder.setLobbyCorner2(LocationXYZ.valueOf(block.getLocation()));
        this.locationSetMessages(block.getLocation(), Color.WHITE, PluginLang.MESSAGE_SETUP_LOBBY_CORNER_2);
    }

    public void promptSetupTeamsMessage() {
        Color[] colors = SetupUtil.findTeams(this);
        StringBuilder sb = new StringBuilder();
        for (Color color : colors) {
            sb.append("<reset>").append(color.getMiniCode()).append(TEAM_SETUP_SYMBOL).append(" ");
        }
        Message message = AdventureMessage.from(sb.toString());
        this.playerMember.sendMessage(message, false);
    }

    public void tryAutoSetupTeam(Color color) {
        Message message = PluginLang.SETUP_TEAM_AUTO_SETUP_TRY.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    public boolean isValidTeam(Color color) {
        return this.dataHolder.getTeamData(color) != null;
    }

    public void setupTeamSpawn(Color color, Location location) {
        this.dataHolder.getTeamData(color).setBed(LocationXYZ.valueOf(SetupUtil.precise(this, location)));
        Pair<Hologram, CancellableWorkload> pair = this.locationSetTasks(location, color, PluginLang.HOLOGRAM_SETUP_TEAM_SPAWN);
        TeamHologramTracker teamHologramTracker = this.teamHologramTrackers.get(color);
        //DESPAWN ALL
        teamHologramTracker.spawn = pair.getKey();
        //SPAWN
        TeamParticleTracker teamParticleTracker = this.teamParticleTrackers.get(color);
        teamParticleTracker.spawn.setCancelled(true);
        teamParticleTracker.spawn = pair.getValue();
        this.locationSetMessages(location, color, PluginLang.MESSAGE_SETUP_TEAM_SPAWN);
    }

    public void setupTeamBed(Color color, Location location) {
        this.dataHolder.getTeamData(color).setBed(LocationXYZ.valueOf(SetupUtil.precise(this, location)));
        Pair<Hologram, CancellableWorkload> pair = this.locationSetTasks(location, color, PluginLang.HOLOGRAM_SETUP_TEAM_BED);
        TeamHologramTracker teamHologramTracker = this.teamHologramTrackers.get(color);
        //despawn old
        teamHologramTracker.bed = pair.getKey();
        //SPAWN
        TeamParticleTracker teamParticleTracker = this.teamParticleTrackers.get(color);
        teamParticleTracker.bed.setCancelled(true);
        teamParticleTracker.bed = pair.getValue();
        this.locationSetMessages(location, color, PluginLang.MESSAGE_SETUP_TEAM_BED);
    }

    public void setupTeamShop(Color color, Location location, ShopType shopType) {

    }

    public void setupTeamGen(Color color, Location location, DropType dropType) {
        this.dataHolder.getTeamData(color).getSpawners().add(ArenaDataHolder.SpawnerDataHolder.of(dropType, LocationXYZ.valueOf(location)));
        Pair<Hologram, CancellableWorkload> pair = this.locationSetTasks(location, color, PluginLang.HOLOGRAM_SETUP_TEAM_GEN);
        //TODO
        this.locationSetMessages(location, color, PluginLang.MESSAGE_SETUP_TEAM_GEN);
    }

    public void cancel() {
        //DESPAWN ALL
    }

    public void complete() {
        this.cancel();
    }

    public boolean isAutoCorrect() {
        return this.autoCorrect;
    }

    public void setAutoCorrect(boolean autoCorrect) {
        this.autoCorrect = autoCorrect;
    }

    private Pair<Hologram, CancellableWorkload> locationSetTasks(Location location, Color color, Lang lang) {
        return Pair.of(SetupUtil.createHologram(location, this.player, lang.asMessage(PlaceholderEntry.symbol("team_color", color.getMiniCode()))),
                SetupUtil.createParticleSpawningTask(location, this.player, color.asJavaColor()));
    }

    private void locationSetMessages(Location location, Color color, Lang lang) {
        this.playerMember.sendMessage(lang.asMessage(PlaceholderEntry.symbol("team_color", color.getMiniCode())), false);
    }

    private static class TeamParticleTracker {
        private CancellableWorkload spawn;
        private CancellableWorkload bed;
        private final Set<CancellableWorkload> spawners = new HashSet<>();
        private final Set<CancellableWorkload> shops = new HashSet<>();
    }

    private static class TeamHologramTracker {
        private Hologram spawn;
        private Hologram bed;
        private final Set<Hologram> spawners = new HashSet<>();
        private final Set<Hologram> shops = new HashSet<>();
    }

}
