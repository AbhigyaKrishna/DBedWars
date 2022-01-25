package com.pepedevs.dbedwars.item;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaStatus;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.task.BedBugDisplayNameUpdateTask;
import com.pepedevs.radium.events.EventUtils;
import com.pepedevs.radium.item.ActionItem;
import com.pepedevs.radium.utils.xseries.XMaterial;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class BedBugSnowball extends PluginActionItem {

    public static final FixedMetadataValue BED_BUG_BALL_META =
            new FixedMetadataValue(DBedwars.getInstance(), true);
    private final DBedwars plugin;
    private final ConfigurableCustomItems.ConfigurableBedBug cfgBedBug;

    public BedBugSnowball(DBedwars plugin) {
        super(Lang.getTranslator().translate(plugin.getConfigHandler().getCustomItems().getBedBug().getItemName()),
                Lang.getTranslator().translate(
                        plugin.getConfigHandler().getCustomItems().getBedBug().getItemLore() == null ? new ArrayList<>()
                        : plugin.getConfigHandler().getCustomItems().getBedBug().getItemLore()),
                XMaterial.SNOWBALL.parseMaterial());
        this.plugin = plugin;
        this.cfgBedBug = plugin.getConfigHandler().getCustomItems().getBedBug();
    }

    @Override
    public void onActionPerform(
            Player player,
            ActionItem.EnumAction enumAction,
            PlayerInteractEvent playerInteractEvent) {
        if (!EventUtils.isRightClick(playerInteractEvent.getAction())) return;
        Arena arena = plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena == null
                || arena.getStatus() != ArenaStatus.RUNNING
                || !arena.getAsArenaPlayer(player).isPresent()
                || arena.getAsArenaPlayer(player).get().isSpectator()) return;
        playerInteractEvent.setCancelled(true);
        Snowball bedBugBall = player.launchProjectile(Snowball.class);
        bedBugBall.setMetadata("isDBedWarsBedBugBall", BED_BUG_BALL_META);
        bedBugBall.setMetadata(
                "thrower",
                new FixedMetadataValue(
                        plugin, arena.getAsArenaPlayer(player).get().getTeam().getColor()));
        BwItemStack.removeItem(player, this.toItemStack());
    }

    public void onLand(ProjectileHitEvent event) {
        Entity bedBugBall = event.getEntity();
        Silverfish bedBug = (Silverfish) bedBugBall.getWorld().spawnEntity(
                bedBugBall.getLocation().clone().add(0, 1, 0),
                EntityType.SILVERFISH);
        Team throwingTeam = plugin.getGameManager().getArena(bedBugBall.getWorld().getName())
                .getTeam(Color.valueOf(bedBugBall.getMetadata("thrower").get(0).asString()));
        plugin.getNMSAdaptor()
                .getBedwarsBedBug(bedBug, throwingTeam)
                .clearDefaultPathfinding()
                .addCustomDefaults()
                .initTargets(1);
        plugin.getThreadHandler().submitAsync(new BedBugDisplayNameUpdateTask(bedBug, throwingTeam, cfgBedBug));
    }

}
