package org.zibble.dbedwars.game.setup;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.ArenaDataHolderImpl;
import org.zibble.dbedwars.game.arena.view.ShopInfoImpl;
import org.zibble.dbedwars.utils.gamerule.GameRuleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupSession {

    private static final Messaging MESSAGING = Messaging.get();
    
    private static final Key<String> WAITING_LOCATION = Key.of("waiting_loc");
    private static final Key<String> SPECTATOR_LOCATION = Key.of("spectator_loc");

    private static final SoundVP PROMPT_SOUND = SoundVP.of(XSound.BLOCK_NOTE_BLOCK_BASEDRUM);
    private static final SoundVP TASK_DONE_SOUND = SoundVP.of(XSound.BLOCK_NETHER_BRICKS_BREAK);

    private final World world;
    private final Player player;
    private final PlayerMember playerMember;
    private final ArenaDataHolderImpl arenaDataHolder;

    private final Map<Key<String>, CancellableWorkload> workloads;
    private final Map<Key<String>, Hologram> holograms;

    private final List<CancellableWorkload> spawnerWorkloads;
    private final List<Hologram> spawnerHolograms;

    private final Map<Color, TeamTracker<CancellableWorkload>> teamWorkloads;
    private final Map<Color, TeamTracker<Hologram>> teamHolograms;

    private boolean isPreciseEnabled;

    public SetupSession(World world, Player player, ArenaDataHolderImpl arenaDataHolder) {
        this.world = world;
        this.player = player;
        this.playerMember = MESSAGING.getMessagingMember(player);
        this.arenaDataHolder = arenaDataHolder;

        this.workloads = new HashMap<>();
        this.holograms = new HashMap<>();
        this.spawnerWorkloads = new ArrayList<>();
        this.spawnerHolograms = new ArrayList<>();
        this.teamWorkloads = new HashMap<>();
        this.teamHolograms = new HashMap<>();

        this.isPreciseEnabled = true;
    }

    public void promptWholeList() {
        this.playerMember.sendMessage(SetupUtil.createPromptMessage(this));
        PROMPT_SOUND.play(this.player);
    }

    public void promptDisableMobSpawning() {
        this.playerMember.sendMessage(PluginLang.DISABLE_MOB_SPAWNING_PROMPT.asMessage());
        PROMPT_SOUND.play(this.player);
    }

    public void disableMobSpawning() {
        GameRuleType.MOB_SPAWNING.apply(this.world, false);
        this.playerMember.sendMessage(PluginLang.DISABLE_MOB_SPAWNING_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void promptEntityCleanup() {
        this.playerMember.sendMessage(PluginLang.ENTITY_CLEANUP_PROMPT.asMessage());
        PROMPT_SOUND.play(this.player);
    }

    public void entityCleanup() {
        int count = 0;
        for (Entity entity : this.world.getEntities()) {
            if (SetupUtil.isAllowedEntity(entity)) continue;
            entity.remove();
            count++;
        }
        this.playerMember.sendMessage(PluginLang.ENTITY_CLEANUP_DONE.asMessage(PlaceholderEntry.of("count", String.valueOf(count))));
        TASK_DONE_SOUND.play(this.player);
    }

    public void promptSetupWaitingLocation() {
        this.playerMember.sendMessage(PluginLang.SETUP_WAITING_LOCATION_PROMPT.asMessage());
        PROMPT_SOUND.play(this.player);
    }

    public void setCustomName(Message name) {
        this.arenaDataHolder.setCustomName(name);
        TASK_DONE_SOUND.play(this.player);
        this.playerMember.sendMessage(PluginLang.SETUP_COMMON_SPAWNER_DONE.asMessage(PlaceholderEntry.symbol("name", AdventureMessage.from(name.asComponent()).getMessage())));
    }

    public void setupWaitingLocation(Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, Color.WHITE);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_WAITING_LOCATION_HOLOGRAM.asMessage());
        CancellableWorkload old = this.workloads.put(WAITING_LOCATION, workload);
        if (old != null) old.setCancelled(true);
        Hologram oldHologram = this.holograms.put(WAITING_LOCATION, hologram);
        if (oldHologram != null) oldHologram.despawn();
        this.arenaDataHolder.setWaitingLocation(SetupUtil.preciseXYZYP(location));
        this.playerMember.sendMessage(PluginLang.SETUP_WAITING_LOCATION_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void promptSetupSpectatorLocation() {
        this.playerMember.sendMessage(PluginLang.SETUP_SPECTATOR_LOCATION_PROMPT.asMessage());
        PROMPT_SOUND.play(this.player);
    }

    public void setupSpectatorLocation(Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, Color.WHITE);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_SPECTATOR_LOCATION_HOLOGRAM.asMessage());
        CancellableWorkload old = this.workloads.put(SPECTATOR_LOCATION, workload);
        if (old != null) old.setCancelled(true);
        Hologram oldHologram = this.holograms.put(SPECTATOR_LOCATION, hologram);
        if (oldHologram != null) oldHologram.despawn();
        this.arenaDataHolder.setSpectatorLocation(SetupUtil.preciseXYZYP(location));
        this.playerMember.sendMessage(PluginLang.SETUP_SPECTATOR_LOCATION_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void promptSetupWaitingBoxCorners() {
        this.playerMember.sendMessage(PluginLang.SETUP_WAITING_LOCATION_CORNERS_PROMPT.asMessage());
        PROMPT_SOUND.play(this.player);
    }

    public void setupWaitingBoxCorner1(Location location) {
        DBedwars.getInstance().getHookManager().getAreaSelectionHook().setFirstLocation(this.player, LocationXYZ.valueOf(location));
        this.playerMember.sendMessage(PluginLang.SETUP_WAITING_LOCATION_CORNERS_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void setupWaitingBoxCorner2(Location location) {
        DBedwars.getInstance().getHookManager().getAreaSelectionHook().setSecondLocation(this.player, LocationXYZ.valueOf(location));
        this.playerMember.sendMessage(PluginLang.SETUP_WAITING_LOCATION_CORNERS_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void setupWaitingBoxArea() {
        BoundingBox selection = DBedwars.getInstance().getHookManager().getAreaSelectionHook().getSelection(this.player);
        if (selection != null) {
            this.arenaDataHolder.setLobbyArea(selection);
            this.playerMember.sendMessage(PluginLang.SETUP_WAITING_AREA_DONE.asMessage());
            TASK_DONE_SOUND.play(this.player);
        } else {
            this.playerMember.sendMessage(PluginLang.SETUP_SESSION_NO_SELECTION_FOUND.asMessage());
        }
    }

    public void promptAddTeams() {
        this.playerMember.sendMessage(PluginLang.ADD_TEAMS_PROMPT.asMessage());
        PROMPT_SOUND.play(this.player);
    }

    public boolean isValidTeam(Color color) {
        return this.arenaDataHolder.getTeamData(color) != null;
    }

    public void addTeam(Color color) {
        this.arenaDataHolder.addTeam(color);
        this.teamWorkloads.put(color, new TeamTracker<>());
        this.teamHolograms.put(color, new TeamTracker<>());
        this.playerMember.sendMessage(PluginLang.ADD_TEAM_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void removeTeam(Color color) {
        this.arenaDataHolder.removeTeam(color);
        this.playerMember.sendMessage(PluginLang.REMOVE_TEAM_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void setupTeamSpawn(Color color, Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, color);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_SPAWN_HOLOGRAM.asMessage());
        CancellableWorkload old = this.teamWorkloads.get(color).spawn;
        if (old != null) old.setCancelled(true);
        this.teamWorkloads.get(color).spawn = workload;
        Hologram oldHologram = this.teamHolograms.get(color).spawn;
        if (oldHologram != null) oldHologram.despawn();
        this.teamHolograms.get(color).spawn = hologram;
        this.arenaDataHolder.getTeamData(color).setSpawnLocation(SetupUtil.preciseXYZYP(location));
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SPAWN_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void setupTeamBed(Color color, Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, color);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_BED_HOLOGRAM.asMessage());

        CancellableWorkload old = this.teamWorkloads.get(color).bed;
        if (old != null) old.setCancelled(true);
        this.teamWorkloads.get(color).bed = workload;

        Hologram oldHologram = this.teamHolograms.get(color).bed;
        if (oldHologram != null) oldHologram.despawn();
        this.teamHolograms.get(color).bed = hologram;

        this.arenaDataHolder.getTeamData(color).setBed(SetupUtil.preciseXYZ(location));
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_BED_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void addTeamSpawner(Color color, DropInfo dropType, Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, color);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_SPAWNER_HOLOGRAM.asMessage());
        this.teamWorkloads.get(color).spawners.add(workload);
        this.teamHolograms.get(color).spawners.add(hologram);

        this.arenaDataHolder.getSpawners().add(ArenaDataHolderImpl.SpawnerDataHolderImpl.of(dropType, SetupUtil.preciseXYZ(location)));
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SPAWNER_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void clearTeamSpawners(Color color) {
        for (CancellableWorkload spawner : this.teamWorkloads.get(color).spawners) {
            spawner.setCancelled(true);
        }
        for (Hologram hologram : this.teamHolograms.get(color).spawners) {
            hologram.despawn();
        }

        this.teamWorkloads.get(color).spawners.clear();
        this.teamHolograms.get(color).spawners.clear();
        this.arenaDataHolder.getSpawners().clear();
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SPAWNER_CLEAR.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void addTeamShop(Color color, ShopInfoImpl shopType, Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, color);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_SHOP_HOLOGRAM.asMessage());
        this.teamWorkloads.get(color).shops.add(workload);
        this.teamHolograms.get(color).shops.add(hologram);

        this.arenaDataHolder.getTeamData(color).getShops().add(ArenaDataHolderImpl.ShopDataHolderImpl.of(shopType, SetupUtil.preciseXYZYP(location)));
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SHOP_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void clearTeamShops(Color color) {
        for (CancellableWorkload workload : this.teamWorkloads.get(color).shops) {
            workload.setCancelled(true);
        }
        for (Hologram hologram : this.teamHolograms.get(color).shops) {
            hologram.despawn();
        }
        this.teamWorkloads.get(color).shops.clear();
        this.teamHolograms.get(color).shops.clear();
        this.arenaDataHolder.getTeamData(color).getShops().clear();
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SHOP_CLEAR.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void addCommonSpawner(DropInfo dropType, Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, Color.WHITE);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_COMMON_SPAWNER_HOLOGRAM.asMessage());
        this.spawnerWorkloads.add(workload);
        this.spawnerHolograms.add(hologram);
        this.arenaDataHolder.getSpawners().add(ArenaDataHolderImpl.SpawnerDataHolderImpl.of(dropType, SetupUtil.preciseXYZ(location)));
        this.playerMember.sendMessage(PluginLang.SETUP_COMMON_SPAWNER_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void clearCommonSpawners() {
        for (CancellableWorkload workload : this.spawnerWorkloads) {
            workload.setCancelled(true);
        }
        for (Hologram hologram : this.spawnerHolograms) {
            hologram.despawn();
        }
        this.spawnerWorkloads.clear();
        this.spawnerHolograms.clear();
        this.arenaDataHolder.getSpawners().clear();
        this.playerMember.sendMessage(PluginLang.SETUP_COMMON_SPAWNER_CLEAR.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    private void loadSaved() {

    }

    public void save() {

    }

    public boolean isPreciseEnabled() {
        return isPreciseEnabled;
    }

    public void setPreciseEnabled(boolean preciseEnabled) {
        isPreciseEnabled = preciseEnabled;
    }

    public ArenaDataHolderImpl getArenaDataHolder() {
        return arenaDataHolder;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerMember getPlayerMember() {
        return playerMember;
    }

    public Map<Color, TeamTracker<Hologram>> getTeamHolograms() {
        return teamHolograms;
    }

    protected static class TeamTracker<T> {

        private T bed;
        private T spawn;
        private final List<T> spawners = new ArrayList<>();
        private final List<T> shops = new ArrayList<>();

        public T getBed() {
            return bed;
        }

        public T getSpawn() {
            return spawn;
        }

        public List<T> getSpawners() {
            return spawners;
        }

        public List<T> getShops() {
            return shops;
        }

    }

}
