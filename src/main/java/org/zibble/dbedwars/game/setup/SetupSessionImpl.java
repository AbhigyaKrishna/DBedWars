package org.zibble.dbedwars.game.setup;

import com.cryptomorin.xseries.XSound;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.game.setup.SetupSession;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.inventoryframework.menu.inventory.AnvilMenu;

import java.util.ArrayList;
import java.util.List;

public class SetupSessionImpl implements SetupSession {

    private static final Messaging MESSAGING = Messaging.getInstance();
    private static final String TEAM_SETUP_SYMBOL = "[]";

    private final SetupSessionInfoImpl dataHolder;
    private final World world;

    private final Player player;
    private final PlayerMember playerMember;

    public boolean isPreciseEnabled;

    private final List<CancellableWorkload> runningWorkloads;

    protected SetupSessionImpl(World world, Player player) {
        this.player = player;
        this.world = world;
        this.playerMember = MESSAGING.getMessagingMember(player);
        this.runningWorkloads = new ArrayList<>();
        this.dataHolder = new SetupSessionInfoImpl();
    }

    @Override
    public void init() {
        this.player.teleport(world.getSpawnLocation());
        new SoundVP(XSound.ENTITY_PLAYER_LEVELUP, 1, 1).play(this.player);
        this.player.setGameMode(GameMode.CREATIVE);
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
        Message startMessage = ConfigLang.SETUP_START.asMessage();
        startMessage.addPlaceholders(PlaceholderEntry.symbol("world", this.world.getName()));
        this.playerMember.sendMessage(startMessage, true);
        this.promptArenaCustomNameSet();
    }

    @Override
    public void promptArenaCustomNameSet() {
        AnvilMenu menu = new AnvilMenu();
        Message message = ConfigLang.SETUP_ARENA_DISPLAY_NAME_SET.asMessage();
        menu.setOutputClick(s -> {
            this.dataHolder.setArenaCustomName(s);
            message.addPlaceholders(PlaceholderEntry.symbol("arena_custom_name", s));
            this.playerMember.sendMessage(message, true);
        });
        menu.mask("ABC");
        //TODO SET ITEMS
        menu.onClose(player -> {
            this.dataHolder.setArenaCustomName(this.world.getName());
            message.addPlaceholders(PlaceholderEntry.symbol("arena_custom_name", this.world.getName()));
            this.playerMember.sendMessage(ConfigLang.SETUP_ARENA_DISPLAY_NAME_SET.asMessage(), true);
        });
    }

    @Override
    public void promptCleanupWorldEntity() {
        this.playerMember.sendMessage(ConfigLang.SETUP_WORLD_CLEANUP_PROMPT.asMessage(), true);
    }

    @Override
    public void cleanupWorldEntities() {
        this.world.getEntities().forEach(entity -> {
            if (!SetupUtil.isAllowedEntity(entity)) entity.remove();
        });
        this.playerMember.sendMessage(ConfigLang.SETUP_WORLD_CLEANUP_CLEANING.asMessage(), true);
    }

    @Override
    public void disableMobSpawning() {
        this.world.setGameRuleValue("doMobSpawning", "false");
        this.playerMember.sendMessage(ConfigLang.SETUP_WORLD_MOB_SPAWNING_DISABLED.asMessage(), true);
    }

    @Override
    public void promptSetupWaitingLocation() {
        this.playerMember.sendMessage(ConfigLang.SETUP_WAITING_LOCATION_PROMPT.asMessage(), true);
    }

    @Override
    public void setupWaitingLocation(Location location) {
        this.dataHolder.setWaitingLocation(SetupUtil.precise(this, location));
        locationSetTasks(this, location, Color.WHITE, PluginLang.HOLOGRAM_SETUP_WAITING_LOCATION_SET.asMessage());
        locationSetMessages(this, location, Color.WHITE, PluginLang.SETUP_WAITING_LOCATION_SET.asMessage());
    }

    @Override
    public void setupLobbyCorner1(Block location) {
        this.dataHolder.setWaitingLocationCorner1(location);
    }

    @Override
    public void setupLobbyCorner2(Block location) {
        this.dataHolder.setWaitingLocationCorner2(location);
    }

    @Override
    public void promptSetupTeamsMessage() {
        Color[] colors = SetupUtil.findTeams(this);
        StringBuilder sb = new StringBuilder();
        for (Color color : colors) {
            sb.append("<reset>").append(color.getMiniCode()).append(TEAM_SETUP_SYMBOL).append(" ");
        }
        Message message = AdventureMessage.from(sb.toString());
        this.playerMember.sendMessage(message, false);
    }

    @Override
    public void startSetupTeam(Color color) {
        Message message = ConfigLang.SETUP_TEAM_START.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    @Override
    public void tryAutoSetupTeam(Color color) {
        Message message = ConfigLang.SETUP_TEAM_AUTO_SETUP_TRY.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    @Override
    public void setupTeamSpawn(Color color, Location location) {
        this.dataHolder.setTeamSpawn(color, SetupUtil.precise(this, location));
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_BED);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_SPAWN);
    }

    @Override
    public void setupTeamShopNPC(Color color, Location location) {
        this.dataHolder.setShopNPC(color, SetupUtil.precise(this, location));
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_SHOP);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_SHOP);
    }

    @Override
    public void setupTeamUpgradesNPC(Color color, Location location) {
        this.dataHolder.setUpgradesNPC(color, location);
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_UPGRADES);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_UPGRADES);
    }

    @Override
    public void setupBedLocation(Color color, Location location) {
        this.dataHolder.setTeamBed(color, SetupUtil.precise(this, location));
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_BED);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_BED);
    }

    @Override
    public void setupGenLocation(Color color, Location location) {
        this.dataHolder.addGenLocation(color, this.world.getSpawnLocation());
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_GEN);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_GEN);
    }

    @Override
    public void cancel() {
        this.runningWorkloads.forEach(w -> w.setCancelled(true));
        this.runningWorkloads.clear();
    }

    @Override
    public void complete() {
        this.runningWorkloads.forEach(w -> w.setCancelled(true));
        this.runningWorkloads.clear();
    }

    @Override
    public boolean isPreciseEnabled() {
        return this.isPreciseEnabled;
    }

    @Override
    public void setPreciseEnabled(boolean preciseEnabled) {
        isPreciseEnabled = preciseEnabled;
    }


    private static void locationSetTasks(SetupSessionImpl setupSession, Location location, Color color, PluginLang lang) {
        SetupUtil.createHologram(location, setupSession.player, lang.asMessage(PlaceholderEntry.symbol("team_color", color.getMiniCode())));
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, setupSession.player, color.asJavaColor());
        setupSession.runningWorkloads.add(workload);
    }

    public static void locationSetMessages(SetupSessionImpl setupSession, Color color, PluginLang lang) {
        setupSession.playerMember.sendMessage(lang.asMessage(PlaceholderEntry.symbol("team_color", color.getMiniCode())), false);
    }

}
