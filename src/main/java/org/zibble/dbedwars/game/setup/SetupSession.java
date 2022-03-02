package org.zibble.dbedwars.game.setup;

import com.cryptomorin.xseries.XSound;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.inventoryframework.menu.inventory.AnvilMenu;

public class SetupSession {

    private static final Messaging MESSAGING = Messaging.getInstance();
    private static final String TEAM_SETUP_SYMBOL = "[]";

    private SetupSessionDataHolder dataHolder;
    private final World world;

    private final Player player;
    private final PlayerMember playerMember;

    public boolean isPreciseEnabled;

    protected SetupSession(World world, Player player) {
        this.player = player;
        this.world = world;
        this.playerMember = MESSAGING.getMessagingMember(player);
    }

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

    public void promptCleanupWorldEntity() {
        this.playerMember.sendMessage(ConfigLang.SETUP_WORLD_CLEANUP_PROMPT.asMessage(), true);
    }

    public void cleanupWorldEntities() {
        this.world.getEntities().forEach(entity -> {
            if (!SetupUtil.isAllowedEntity(entity)) entity.remove();
        });
        this.playerMember.sendMessage(ConfigLang.SETUP_WORLD_CLEANUP_CLEANING.asMessage(), true);
    }

    public void disableMobSpawning() {
        this.world.setGameRuleValue("doMobSpawning", "false");
        this.playerMember.sendMessage(ConfigLang.SETUP_WORLD_MOB_SPAWNING_DISABLED.asMessage(), true);
    }

    public void promptSetupWaitingLocation() {
        this.playerMember.sendMessage(ConfigLang.SETUP_WAITING_LOCATION_PROMPT.asMessage(), true);
    }

    public void setupWaitingLocation(Location location) {
        this.dataHolder.setWaitingLocation(SetupUtil.precise(this, location));
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

    public void setupTeamSpawn(Color color) {
        Message message = ConfigLang.SETUP_TEAM_SPAWN.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    public void setupTeamShopNPC(Color color) {
        Message message = ConfigLang.SETUP_TEAM_SHOP.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    public void setupTeamUpgradesNPC(Color color) {
        Message message = ConfigLang.SETUP_TEAM_UPGRADES.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    public void setupBedLocation(Color color) {
        Message message = ConfigLang.SETUP_TEAM_BED.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    public void setupGenLocation(Color color) {
        Message message = ConfigLang.SETUP_TEAM_GEN.asMessage();
        message.addPlaceholders(PlaceholderEntry.symbol("team_color", color.getMiniCode()));
        this.playerMember.sendMessage(message, true);
    }

    public void cancel() {

    }

    public void complete() {

    }

    public boolean isPreciseEnabled() {
        return this.isPreciseEnabled;
    }

    public void setPreciseEnabled(boolean preciseEnabled) {
        isPreciseEnabled = preciseEnabled;
    }
}
