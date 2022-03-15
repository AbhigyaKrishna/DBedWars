package org.zibble.dbedwars.game.setup;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.game.NewArenaDataHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class NewSetupSession {

    private static final Messaging MESSAGING = Messaging.get();
    
    private static final Key<String> WAITING_LOCATION = Key.of("waiting_loc");
    private static final Key<String> SPECTATOR_LOCATION = Key.of("spectator_loc");

    private static final SoundVP PROMPT_SOUND = SoundVP.of();
    private static final SoundVP TASK_DONE_SOUND = SoundVP.of();

    private final World world;
    private final Player player;
    private final PlayerMember playerMember;
    private final NewArenaDataHolder newArenaDataHolder;

    private final Map<Key<String>, CancellableWorkload> workloads;
    private final Map<Key<String>, Hologram> holograms;

    private final Map<Location, Tracker<CancellableWorkload>> spawnerWorkloads;
    private final Map<Location, Tracker<Hologram>> spawnerHolograms;

    private final Map<Color, Tracker<CancellableWorkload>> teamWorkloads;
    private final Map<Color, Tracker<Hologram>> teamHolograms;

    private boolean isPreciseEnabled;

    public NewSetupSession(World world, Player player, NewArenaDataHolder newArenaDataHolder) {
        this.world = world;
        this.player = player;
        this.playerMember = MESSAGING.getMessagingMember(player);
        this.newArenaDataHolder = newArenaDataHolder;

        this.workloads = new HashMap<>();
        this.holograms = new HashMap<>();
        this.spawnerWorkloads = new HashMap<>();
        this.spawnerHolograms = new HashMap<>();
        this.teamWorkloads = new HashMap<>();
        this.teamHolograms = new HashMap<>();

        this.isPreciseEnabled = true;
    }

    public void promptDisableMobSpawning() {
        this.playerMember.sendMessage();
        PROMPT_SOUND.play(this.player);
    }

    public void disableMobSpawning() {
        this.world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void promptEntityCleanup() {
        this.playerMember.sendMessage();
        PROMPT_SOUND.play(this.player);
    }

    public void entityCleanup() {
        int count = 0;
        for (Entity entity : this.world.getEntities()) {
            if (SetupUtil.isAllowedEntity(entity)) continue;
            entity.remove();
            count++;
        }
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void promptSetupWaitingLocation() {
        this.playerMember.sendMessage();
        PROMPT_SOUND.play(this.player);
    }

    public void setupWaitingLocation() {
        this.newArenaDataHolder.setWaitingLocation(SetupUtil.preciseXYZYP(this.isPreciseEnabled, this.player.getLocation()));
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void promptSetupSpectatorLocation() {
        this.playerMember.sendMessage();
        PROMPT_SOUND.play(this.player);
    }

    public void setupSpectatorLocation() {
        this.newArenaDataHolder.setSpectatorLocation(SetupUtil.preciseXYZYP(this.isPreciseEnabled, this.player.getLocation()));
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void promptSetupWaitingBoxCorners() {
        this.playerMember.sendMessage();
        PROMPT_SOUND.play(this.player);
    }

    public void setupWaitingBoxCorner1() {
        this.newArenaDataHolder.setLobbyCorner1(SetupUtil.preciseXYZ(this.isPreciseEnabled, this.player.getLocation()));
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void setupWaitingBoxCorner2() {
        this.newArenaDataHolder.setLobbyCorner2(SetupUtil.preciseXYZ(this.isPreciseEnabled, this.player.getLocation()));
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void tryAutoDetectTeams() {

    }

    public void promptAddTeams() {
        this.playerMember.sendMessage();
        PROMPT_SOUND.play(this.player);
    }

    public void addTeam(Color color) {
        this.newArenaDataHolder.addTeam(color);
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void removeTeam(Color color) {
        this.newArenaDataHolder.removeTeam(color);
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void setupTeamSpawn(Color color) {
        this.newArenaDataHolder.getTeamData(color).setSpawnLocation(SetupUtil.preciseXYZYP(this.isPreciseEnabled, this.player.getLocation()));
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void setupTeamBed(Color color) {
        this.newArenaDataHolder.getTeamData(color).setBed(SetupUtil.preciseXYZ(this.isPreciseEnabled, this.player.getLocation()));
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void addTeamSpawner(Color color, DropType dropType) {
        this.newArenaDataHolder.getSpawners().add(NewArenaDataHolder.SpawnerDataHolder.of(dropType, SetupUtil.preciseXYZ(this.isPreciseEnabled, this.player.getLocation())));
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void clearTeamSpawners(Color color) {
        this.newArenaDataHolder.getSpawners().clear();
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void addTeamShop(Color color, ShopType shoptype) {
        this.newArenaDataHolder.getTeamData(color).getShops().add(NewArenaDataHolder.ShopDataHolder.of(shoptype, SetupUtil.preciseXYZYP(this.isPreciseEnabled, this.player.getLocation())));
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void clearTeamShops(Color color) {
        this.newArenaDataHolder.getTeamData(color).getShops().clear();
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void addCommonSpawner(DropType dropType) {
        this.newArenaDataHolder.getSpawners().add(NewArenaDataHolder.SpawnerDataHolder.of(dropType, SetupUtil.preciseXYZ(this.isPreciseEnabled, this.player.getLocation())));
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    public void clearCommonSpawners() {
        this.newArenaDataHolder.getSpawners().clear();
        this.playerMember.sendMessage();
        TASK_DONE_SOUND.play(this.player);
    }

    private void loadSaved() {

    }

    public void save() {

    }

    private static class Tracker<T> {
        private T bed;
        private T spawn;
        private final List<T> spawners = new ArrayList<>();
        private final List<T> shops = new ArrayList<>();
    }

}
