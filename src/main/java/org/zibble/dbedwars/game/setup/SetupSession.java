package org.zibble.dbedwars.game.setup;

import com.cryptomorin.xseries.XSound;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.configuration.language.Lang;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.ArenaDataHolder;
import org.zibble.dbedwars.guis.setup.ArenaNameGui;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.dbedwars.utils.gamerule.GameRuleType;
import org.zibble.inventoryframework.protocol.Materials;
import org.zibble.inventoryframework.protocol.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SetupSession {

    private static final Messaging MESSAGING = Messaging.getInstance();
    private static final String TEAM_SETUP_SYMBOL = "[]";

    private final ArenaDataHolder dataHolder;
    private final World world;

    private final Player player;
    private final PlayerMember playerMember;

    public boolean autoCorrect;

    private final List<CancellableWorkload> runningWorkloads;

    protected SetupSession(World world, Player player, ArenaDataHolder dataHolder) {
        this.player = player;
        this.world = world;
        this.playerMember = MESSAGING.getMessagingMember(player);
        this.runningWorkloads = new ArrayList<>();
        this.dataHolder = dataHolder;
    }

    public void init() {
        this.player.teleport(world.getSpawnLocation());
        new SoundVP(XSound.ENTITY_PLAYER_LEVELUP, 1, 1).play(this.player);
        this.player.setGameMode(GameMode.CREATIVE);
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
        Message startMessage = ConfigLang.SETUP_START.asMessage();
        startMessage.addPlaceholders(PlaceholderEntry.symbol("world", this.world.getName()));
        this.playerMember.sendMessage(startMessage);
    }

    public void promptArenaCustomNameSet() {
        Message message = ConfigLang.SETUP_ARENA_DISPLAY_NAME_SET.asMessage();
        ArenaNameGui.creator()
                .item(() -> {
                    ItemStack item = new ItemStack(Materials.PAPER);
                    item.displayName(ConfigMessage.from(this.world.getName()).asComponentWithPAPI(this.player)[0]);
                    return item;
                })
                .outputClick((menu, s) -> {
                    this.dataHolder.setCustomName(ConfigMessage.from(s));
                    message.addPlaceholders(PlaceholderEntry.symbol("arena_custom_name", s));
                    this.playerMember.sendMessage(message, true);
                })
                .onClose((menu, player) -> {
                    this.dataHolder.setCustomName(ConfigMessage.from(this.world.getName()));
                    message.addPlaceholders(PlaceholderEntry.symbol("arena_custom_name", this.world.getName()));
                    this.playerMember.sendMessage(ConfigLang.SETUP_ARENA_DISPLAY_NAME_SET.asMessage());
                })
                .open(this.player);
    }

    public void promptDisableMobSpawning() {
        this.playerMember.sendMessage(PluginLang.SETUP_DISABLE_MOB_SPAWNING_PROMPT.asMessage());
    }

    public void disableMobSpawning() {
        GameRuleType.MOB_SPAWNING.apply(this.world, false);
        this.playerMember.sendMessage(ConfigLang.SETUP_WORLD_MOB_SPAWNING_DISABLED.asMessage());
    }

    public void promptCleanupWorldEntity() {
        this.playerMember.sendMessage(ConfigLang.SETUP_WORLD_CLEANUP_PROMPT.asMessage());
    }

    public int cleanupWorldEntities() {
        int count = 0;
        for (Entity entity : this.world.getEntities()) {
            if (!SetupUtil.isAllowedEntity(entity)) {
                entity.remove();
                count++;
            }
        }
        this.playerMember.sendMessage(ConfigLang.SETUP_WORLD_CLEANUP_CLEANING.asMessage());
        return count;
    }

    public void promptSetupWaitingLocation() {
        this.playerMember.sendMessage(ConfigLang.SETUP_WAITING_LOCATION_PROMPT.asMessage());
    }

    public void setupWaitingLocation(Location location) {
        this.dataHolder.setWaitingLocation(LocationXYZYP.valueOf(SetupUtil.precise(this, location)));
        locationSetTasks(this, location, Color.WHITE, PluginLang.HOLOGRAM_SETUP_WAITING_LOCATION_SET.asMessage());
        locationSetMessages(this, location, Color.WHITE, PluginLang.SETUP_WAITING_LOCATION_SET.asMessage());
    }

    public void promptSetupSpectatorLocation() {

    }

    public void setupSpectatorLocation(Location location) {

    }

    public void setupLobbyCorner1(Block block) {
        this.dataHolder.setLobbyCorner1(LocationXYZ.valueOf(block.getLocation()));
    }

    public void setupLobbyCorner2(Block block) {
        this.dataHolder.setLobbyCorner2(LocationXYZ.valueOf(block.getLocation()));
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

    public void startSetupTeam(Color color) {
        Message message = ConfigLang.SETUP_TEAM_START.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    public void tryAutoSetupTeam(Color color) {
        Message message = ConfigLang.SETUP_TEAM_AUTO_SETUP_TRY.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    public boolean isValidTeam(Color color) {
        return this.dataHolder.getTeamData(color) != null;
    }

    public void setupTeamSpawn(Color color, Location location) {
        this.dataHolder.getTeamData(color).setSpawnLocation(LocationXYZYP.valueOf(SetupUtil.precise(this, location)));
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_BED);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_SPAWN);
    }

    public void setupTeamShopNPC(Color color, Location location) {
        this.dataHolder.getTeamData(color).setShopNPC(LocationXYZYP.valueOf(SetupUtil.precise(this, location)));
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_SHOP);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_SHOP);
    }

    public void setupTeamUpgradesNPC(Color color, Location location) {
        this.dataHolder.getTeamData(color).setUpgradesNPC(LocationXYZYP.valueOf(SetupUtil.precise(this, location)));
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_UPGRADES);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_UPGRADES);
    }

    public void setupTeamBed(Color color, Location location) {
        this.dataHolder.getTeamData(color).setBed(LocationXYZ.valueOf(SetupUtil.precise(this, location)));
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_BED);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_BED);
    }

    public void setupTeamGen(Color color, Location location, DropType dropType) {
        this.dataHolder.getTeamData(color).getSpawners().add(ArenaDataHolder.SpawnerDataHolder.of(dropType, LocationXYZ.valueOf(location)));
        locationSetTasks(this, location, color, PluginLang.HOLOGRAM_SETUP_TEAM_GEN);
        locationSetMessages(this, color, PluginLang.SETUP_TEAM_GEN);
    }

    public void cancel() {
        for (CancellableWorkload task : this.runningWorkloads) {
            task.setCancelled(true);
        }
        this.runningWorkloads.clear();
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

    private static void locationSetTasks(SetupSession setupSession, Location location, Color color, Lang lang) {
        SetupUtil.createHologram(location, setupSession.player, lang.asMessage(PlaceholderEntry.symbol("team_color", color.getMiniCode())));
        CancellableWorkload workload = SetupUtil.createParticleSpawningTask(location, setupSession.player, color.asJavaColor());
        setupSession.runningWorkloads.add(workload);
    }

    private static void locationSetMessages(SetupSession setupSession, Color color, Lang lang) {
        setupSession.playerMember.sendMessage(lang.asMessage(PlaceholderEntry.symbol("team_color", color.getMiniCode())), false);
    }

}
