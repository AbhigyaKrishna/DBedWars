package org.zibble.dbedwars.game.setup;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.configuration.configurable.ConfigurableArena;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.ArenaDataHolderImpl;
import org.zibble.dbedwars.game.arena.view.ShopInfoImpl;
import org.zibble.dbedwars.utils.Util;
import org.zibble.dbedwars.utils.gamerule.GameRuleType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class SetupSession {

    private static final Messaging MESSAGING = Messaging.get();

    private static final Key WAITING_LOCATION = Key.of("waiting_loc");
    private static final Key SPECTATOR_LOCATION = Key.of("spectator_loc");
    private static final Key LOBBY_AREA_BOX = Key.of("lobby_area_box");

    private static final SoundVP PROMPT_SOUND = SoundVP.of(XSound.BLOCK_NOTE_BLOCK_BASEDRUM);
    private static final SoundVP TASK_DONE_SOUND = SoundVP.of(XSound.BLOCK_NOTE_BLOCK_PLING);

    private final World world;
    private final Player player;
    private final PlayerMember playerMember;
    private final ArenaDataHolderImpl arenaDataHolder;

    private final Map<Key, Pair<Hologram, CancellableWorkload>> defaultTasks;
    private final List<Pair<Hologram, CancellableWorkload>> spawnerTasks;
    private final Map<Color, Pair<TeamTracker<Hologram>, TeamTracker<CancellableWorkload>>> teamTasks;

    public SetupSession(World world, Player player, ArenaDataHolderImpl arenaDataHolder) {
        this.world = world;
        this.player = player;
        this.playerMember = MESSAGING.getMessagingMember(player);
        this.arenaDataHolder = arenaDataHolder;

        this.defaultTasks = new HashMap<>();
        this.spawnerTasks = new ArrayList<>();
        this.teamTasks = new HashMap<>();
        this.setupDefaults();
    }

    public void setupDefaults() {
        MissingArenaData data = new MissingArenaData(this);
        Location location;
        if (data.hasWaitingLocation()) {
            location = this.arenaDataHolder.getWaitingLocation().toBukkit(this.world);
            CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, Color.WHITE);
            Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_WAITING_LOCATION_HOLOGRAM.asMessage());
            this.defaultTasks.put(WAITING_LOCATION, Pair.of(hologram, workload));
        }
        if (data.hasSpectatorLocation()) {
            location = this.arenaDataHolder.getSpectatorLocation().toBukkit(this.world);
            CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, Color.WHITE);
            Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_SPECTATOR_LOCATION_HOLOGRAM.asMessage());
            this.defaultTasks.put(SPECTATOR_LOCATION, Pair.of(hologram, workload));
        }
        if (data.hasLobbyArea()) {
            this.defaultTasks.put(LOBBY_AREA_BOX, Pair.of(null, SetupUtil.createLobbyAreaBoxParticle(this.arenaDataHolder.getLobbyArea(), this.player, this.world)));
        }
        for (ArenaDataHolderImpl.SpawnerDataHolderImpl spawner : this.arenaDataHolder.getSpawners()) {
            location = spawner.getLocation().toBukkit(this.world);
            CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, Color.WHITE);
            Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_COMMON_SPAWNER_HOLOGRAM.asMessage());
            this.spawnerTasks.add(Pair.of(hologram, workload));
        }
        for (Map.Entry<Color, ArenaDataHolderImpl.TeamDataHolderImpl> entry : this.arenaDataHolder.getTeamData().entrySet()) {
            TeamTracker<Hologram> hologramTracker = new TeamTracker<>();
            TeamTracker<CancellableWorkload> taskTracker = new TeamTracker<>();
            location = entry.getValue().getSpawnLocation().toBukkit(this.world);
            taskTracker.spawn = SetupUtil.createParticleSpawningTask(location, this.player, entry.getKey());
            hologramTracker.spawn = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_SPAWN_HOLOGRAM.asMessage());
            location = entry.getValue().getBed().toBukkit(this.world);
            taskTracker.bed = SetupUtil.createParticleSpawningTask(location, this.player, entry.getKey());
            hologramTracker.bed = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_BED_HOLOGRAM.asMessage());
            for (ArenaDataHolderImpl.SpawnerDataHolderImpl spawner : entry.getValue().getSpawners()) {
                location = spawner.getLocation().toBukkit(this.world);
                taskTracker.spawners.add(SetupUtil.createParticleSpawningTask(location, this.player, entry.getKey()));
                hologramTracker.spawners.add(SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_SPAWNER_HOLOGRAM.asMessage()));
            }
            for (ArenaDataHolderImpl.ShopDataHolderImpl shop : entry.getValue().getShops()) {
                location = shop.getLocation().toBukkit(this.world);
                taskTracker.shops.add(SetupUtil.createParticleSpawningTask(location, this.player, entry.getKey()));
                hologramTracker.shops.add(SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_SHOP_HOLOGRAM.asMessage()));
            }

            this.teamTasks.put(entry.getKey(), Pair.of(hologramTracker, taskTracker));
        }
    }

    public void promptWholeList() {
        MissingArenaData data = new MissingArenaData(this);
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
                        "<yellow><hover:show_text:'/bw setup setbed <gold>(color)</gold>'><click:suggest_command:'/bw setup setbed'>Set team bed</click></hover></yellow> <gray>(" + SetupUtil.createPatternForTeams('X', teamMissingBed) + ")</gray>",
                teamMissingSpawn.isEmpty() ? "<gray><st><hover:show_text:'/bw setup setspawn <gold>(color)</gold>'><click:suggest_command:'/bw setup setspawn'>Set team spawn</click></hover></st></gray> <green>(DONE)</green>" :
                        "<yellow><hover:show_text:'/bw setup setspawn <gold>(color)</gold>'><click:suggest_command:'/bw setup setspawn'>Set team spawn</click></hover></yellow> <gray>(" + SetupUtil.createPatternForTeams('X', teamMissingSpawn) + ")</gray>",
                "<yellow><hover:show_text:'/bw setup addshop <gold>[shop] (color)</gold>'><click:suggest_command:'/bw setup addshop '>Set team shop</click></hover></yellow>",
                "<yellow><hover:show_text:'/bw setup addspawner <gold>[spawner] (color)</gold>'><click:suggest_command:'/bw setup addspawner '>Add spawners</click></hover></yellow>",
                "<yellow><hover:show_text:'/bw setup save'><click:run_command:'/bw setup save'>Save Arena</click></hover></yellow>"
        };
        AdventureMessage message = AdventureMessage.from(lines, PlaceholderEntry.symbol("custom_name", this.getArenaDataHolder().getCustomName() != null ? Util.convertMessage(this.getArenaDataHolder().getCustomName(), AdventureMessage.empty()).getMessage() : ""));

        this.playerMember.sendMessage(message);
        PROMPT_SOUND.play(this.player);
    }

    public void disableMobSpawning() {
        GameRuleType.MOB_SPAWNING.apply(this.world, false);
        this.playerMember.sendMessage(PluginLang.DISABLE_MOB_SPAWNING_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void entityCleanup() {
        int count = 0;
        for (Entity entity : this.world.getEntities()) {
            if (SetupUtil.isAllowedEntity(entity)) continue;
            entity.remove();
            count++;
        }
        this.playerMember.sendMessage(PluginLang.ENTITY_CLEANUP_DONE.asMessage(PlaceholderEntry.symbol("count", String.valueOf(count))));
        TASK_DONE_SOUND.play(this.player);
    }

    public void setCustomName(Message name) {
        this.arenaDataHolder.setCustomName(name);
        TASK_DONE_SOUND.play(this.player);
        this.playerMember.sendMessage(PluginLang.SETUP_COMMON_SPAWNER_DONE.asMessage(PlaceholderEntry.symbol("name", AdventureMessage.from(name.asComponent()).getMessage())));
    }

    public void setupWaitingLocation(Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, Color.WHITE);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_WAITING_LOCATION_HOLOGRAM.asMessage());
        this.putAndDestroyOld(workload, hologram, WAITING_LOCATION);
        this.arenaDataHolder.setWaitingLocation(SetupUtil.preciseXYZYP(location));
        this.playerMember.sendMessage(PluginLang.SETUP_WAITING_LOCATION_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void setupSpectatorLocation(Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, Color.WHITE);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_SPECTATOR_LOCATION_HOLOGRAM.asMessage());
        this.putAndDestroyOld(workload, hologram, SPECTATOR_LOCATION);
        this.arenaDataHolder.setSpectatorLocation(SetupUtil.preciseXYZYP(location));
        this.playerMember.sendMessage(PluginLang.SETUP_SPECTATOR_LOCATION_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
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
            this.putAndDestroyOld(SetupUtil.createLobbyAreaBoxParticle(selection, this.player, this.world), null, LOBBY_AREA_BOX);
            this.playerMember.sendMessage(PluginLang.SETUP_WAITING_AREA_DONE.asMessage());
            TASK_DONE_SOUND.play(this.player);
        } else {
            this.playerMember.sendMessage(PluginLang.SETUP_SESSION_NO_SELECTION_FOUND.asMessage());
        }
    }

    public boolean isValidTeam(Color color) {
        return this.arenaDataHolder.getTeamData(color) != null;
    }

    public void addTeam(Color color) {
        this.arenaDataHolder.addTeam(color);
        this.teamTasks.put(color, Pair.of(new TeamTracker<>(), new TeamTracker<>()));
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
        Pair<TeamTracker<Hologram>, TeamTracker<CancellableWorkload>> pair = this.teamTasks.get(color);
        CancellableWorkload old = pair.getValue().spawn;
        if (old != null) old.setCancelled(true);
        pair.getValue().spawn = workload;
        Hologram oldHologram = pair.getKey().spawn;
        if (oldHologram != null) oldHologram.destroy();
        pair.getKey().spawn = hologram;
        this.arenaDataHolder.getTeamData(color).setSpawnLocation(SetupUtil.preciseXYZYP(location));
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SPAWN_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void setupTeamBed(Color color, Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, color);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_BED_HOLOGRAM.asMessage());

        Pair<TeamTracker<Hologram>, TeamTracker<CancellableWorkload>> pair = this.teamTasks.get(color);
        CancellableWorkload old = pair.getValue().bed;
        if (old != null) old.setCancelled(true);
        pair.getValue().bed = workload;

        Hologram oldHologram = pair.getKey().bed;
        if (oldHologram != null) oldHologram.destroy();
        pair.getKey().bed = hologram;

        this.arenaDataHolder.getTeamData(color).setBed(SetupUtil.preciseXYZ(location));
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_BED_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void addTeamSpawner(Color color, DropInfo dropType, Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, color);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_SPAWNER_HOLOGRAM.asMessage());
        Pair<TeamTracker<Hologram>, TeamTracker<CancellableWorkload>> pair = this.teamTasks.get(color);
        pair.getValue().spawners.add(workload);
        pair.getKey().spawners.add(hologram);

        this.arenaDataHolder.getTeamData(color).getSpawners().add(ArenaDataHolderImpl.SpawnerDataHolderImpl.of(dropType, SetupUtil.preciseXYZ(location)));
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SPAWNER_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void clearTeamSpawners(Color color) {
        Pair<TeamTracker<Hologram>, TeamTracker<CancellableWorkload>> pair = this.teamTasks.get(color);
        for (CancellableWorkload spawner : pair.getValue().spawners) {
            spawner.setCancelled(true);
        }
        for (Hologram hologram : pair.getKey().spawners) {
            hologram.destroy();
        }

        pair.getValue().spawners.clear();
        pair.getKey().spawners.clear();
        this.arenaDataHolder.getTeamData(color).getSpawners().clear();
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SPAWNER_CLEAR.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void addTeamShop(Color color, ShopInfoImpl shopType, Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, color);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_TEAM_SHOP_HOLOGRAM.asMessage());
        Pair<TeamTracker<Hologram>, TeamTracker<CancellableWorkload>> pair = this.teamTasks.get(color);
        pair.getValue().shops.add(workload);
        pair.getKey().shops.add(hologram);

        this.arenaDataHolder.getTeamData(color).getShops().add(ArenaDataHolderImpl.ShopDataHolderImpl.of(shopType, SetupUtil.preciseXYZYP(location)));
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SHOP_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void clearTeamShops(Color color) {
        Pair<TeamTracker<Hologram>, TeamTracker<CancellableWorkload>> pair = this.teamTasks.get(color);
        for (CancellableWorkload workload : pair.getValue().shops) {
            workload.setCancelled(true);
        }
        for (Hologram hologram : pair.getKey().shops) {
            hologram.destroy();
        }
        pair.getValue().shops.clear();
        pair.getKey().shops.clear();
        this.arenaDataHolder.getTeamData(color).getShops().clear();
        this.playerMember.sendMessage(PluginLang.SETUP_TEAM_SHOP_CLEAR.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void addCommonSpawner(DropInfo dropType, Location location) {
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, this.player, Color.WHITE);
        Hologram hologram = SetupUtil.createHologram(location, this.player, PluginLang.SETUP_COMMON_SPAWNER_HOLOGRAM.asMessage());
        this.spawnerTasks.add(Pair.of(hologram, workload));
        this.arenaDataHolder.getSpawners().add(ArenaDataHolderImpl.SpawnerDataHolderImpl.of(dropType, SetupUtil.preciseXYZ(location)));
        this.playerMember.sendMessage(PluginLang.SETUP_COMMON_SPAWNER_DONE.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void clearCommonSpawners() {
        for (Pair<Hologram, CancellableWorkload> workload : this.spawnerTasks) {
            workload.getKey().destroy();
            workload.getValue().setCancelled(true);
        }
        this.spawnerTasks.clear();
        this.arenaDataHolder.getSpawners().clear();
        this.playerMember.sendMessage(PluginLang.SETUP_COMMON_SPAWNER_CLEAR.asMessage());
        TASK_DONE_SOUND.play(this.player);
    }

    public void saveAndUnload() {
        ConfigurableArena cfg = new ConfigurableArena(this.arenaDataHolder);
        File file = new File(PluginFiles.Folder.ARENA_DATA_SETTINGS, this.arenaDataHolder.getId() + ".yml");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            cfg.save(config);
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Pair<Hologram, CancellableWorkload> pair : this.defaultTasks.values()) {
            if (pair.getKey() != null)
                pair.getKey().destroy();
            if (pair.getValue() != null)
                pair.getValue().setCancelled(true);
        }

        for (Pair<Hologram, CancellableWorkload> pair : this.spawnerTasks) {
            if (pair.getKey() != null)
                pair.getKey().destroy();
            if (pair.getValue() != null)
                pair.getValue().setCancelled(true);
        }

        for (Pair<TeamTracker<Hologram>, TeamTracker<CancellableWorkload>> pair : this.teamTasks.values()) {
            pair.getValue().callAll(wl -> wl.setCancelled(true));
            pair.getKey().callAll(Hologram::destroy);
        }

        this.defaultTasks.clear();
        this.spawnerTasks.clear();
        this.teamTasks.clear();

        for (Player player : this.world.getPlayers()) {
            player.teleport(DBedwars.getInstance().getGameManager().getLobbySpawn());
        }

        DBedwars.getInstance().getHookManager().getWorldAdaptor().unloadWorld(this.world.getName(), true);
        this.playerMember.sendMessage(PluginLang.SETUP_SESSION_ARENA_SAVED_SUCCESSFUL.asMessage());
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

    public Map<Color, Pair<TeamTracker<Hologram>, TeamTracker<CancellableWorkload>>> getTeamTasks() {
        return teamTasks;
    }

    private void putAndDestroyOld(CancellableWorkload workload, Hologram hologram, Key spectatorLocation) {
        Pair<Hologram, CancellableWorkload> old = this.defaultTasks.put(spectatorLocation, Pair.of(hologram, workload));
        if (old != null) {
            if (old.getKey() != null)
                old.getKey().destroy();
            if (old.getValue() != null)
                old.getValue().setCancelled(true);
        }
    }

    protected static class TeamTracker<T> {

        private final List<T> spawners = new ArrayList<>();
        private final List<T> shops = new ArrayList<>();
        private T bed;
        private T spawn;

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

        public void callAll(Consumer<T> consumer) {
            if (this.bed != null)
                consumer.accept(this.bed);
            if (this.spawn != null)
                consumer.accept(this.spawn);
            for (T spawner : this.spawners) {
                consumer.accept(spawner);
            }
            for (T shop : this.shops) {
                consumer.accept(shop);
            }
        }

    }

}
